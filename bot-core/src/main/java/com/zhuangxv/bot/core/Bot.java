package com.zhuangxv.bot.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhuangxv.bot.api.ApiResult;
import com.zhuangxv.bot.api.support.*;
import com.zhuangxv.bot.config.BotConfig;
import com.zhuangxv.bot.exception.BotException;
import com.zhuangxv.bot.message.CacheMessage;
import com.zhuangxv.bot.message.MessageChain;
import com.zhuangxv.bot.message.support.ForwardNodeMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xiaoxu
 * @since 2021/5/27 10:36
 */
@Slf4j
public class Bot {

    private final Map<Long, Friend> friends = new ConcurrentHashMap<>();
    private final Map<Long, Group> groups = new ConcurrentHashMap<>();
    private final Map<Long, Map<Long, Member>> groupMembers = new ConcurrentHashMap<>();
    private final Map<String, Map<Integer, CacheMessage>> cacheMessageChain = new HashMap<>();
    private final Lock cacheMessageChainLock = new ReentrantLock();
    public final CompletableFuture<Long> completableFuture = new CompletableFuture<>();
    private final BotConfig botConfig;
    private final BotClient botClient;

    protected Bot(BotConfig botConfig, BotDispatcher botDispatcher) {
        this.botConfig = botConfig;
        this.botClient = new BotClient(botConfig, botDispatcher, this);
    }

    public BotClient getBotClient() {
        return this.botClient;
    }

    public void pushGroupCacheMessageChain(Long groupId, Integer messageId, CacheMessage cacheMessage) {
        this.pushCacheMessageChain("group", groupId, messageId, cacheMessage);

    }

    public void pushUserCacheMessageChain(Long userId, Integer messageId, CacheMessage cacheMessage) {
        this.pushCacheMessageChain("user", userId, messageId, cacheMessage);
    }

    private void pushCacheMessageChain(String prefix, Long id, Integer messageId, CacheMessage cacheMessage) {
        this.cacheMessageChainLock.lock();
        try {
            Map<Integer, CacheMessage> messageChainMap = this.cacheMessageChain.computeIfAbsent(prefix + id, key -> new LinkedHashMap<>());
            messageChainMap.put(messageId, cacheMessage);
        } finally {
            this.cacheMessageChainLock.unlock();
        }
    }

    public List<CacheMessage> getGroupCacheMessageChain(Long groupId, Integer messageId, Integer size) {
        return this.getCacheMessageChain("group", groupId, messageId, size);
    }

    public List<CacheMessage> getUserCacheMessageChain(Long groupId, Integer messageId, Integer size) {
        return this.getCacheMessageChain("user", groupId, messageId, size);
    }

    private List<CacheMessage> getCacheMessageChain(String prefix, long id, Integer messageId, Integer size) {
        this.cacheMessageChainLock.lock();
        try {
            List<CacheMessage> result = new ArrayList<>();
            Map<Integer, CacheMessage> messageChainMap = this.cacheMessageChain.get(prefix + id);
            if (messageChainMap == null) {
                return result;
            }
            if (messageChainMap.isEmpty()) {
                return result;
            }
            List<Integer> messageIds = new ArrayList<>(messageChainMap.keySet());
            boolean find = false;
            for (int i = messageIds.size() - 1; i >= 0; i--) {
                Integer messageIdTemp = messageIds.get(i);
                if (!find) {
                    if (messageId.equals(messageIdTemp)) {
                        find = true;
                    }
                }
                if (find) {
                    result.add(messageChainMap.get(messageIdTemp));
                    if (result.size() >= size) {
                        break;
                    }
                }
            }
            Collections.reverse(result);
            return result;
        } finally {
            this.cacheMessageChainLock.unlock();
        }
    }

    public void flushFriends() {
        log.info(String.format("[%s]正在刷新好友列表.", this.botConfig.getBotName()));
        ApiResult apiResult = this.botClient.invokeApi(new GetFriends());
        JSONArray resultArray = this.getArray(apiResult.getData());
        for (int i = 0; i < resultArray.size(); i++) {
            JSONObject resultObject = resultArray.getJSONObject(i);
            long userId = resultObject.getLongValue("user_id");
            String nickname = resultObject.getString("nickname");
            String remark = resultObject.getString("remark");
            this.friends.put(userId, new Friend(userId, nickname, remark, this));
        }
        log.info(String.format("[%s]刷新好友列表完成,共有好友%d个.", this.botConfig.getBotName(), this.friends.size()));
    }

    public Collection<Group> flushGroups() {
        log.info(String.format("[%s]正在刷新群列表.", this.botConfig.getBotName()));
        ApiResult apiResult = this.botClient.invokeApi(new GetGroups());
        JSONArray resultArray = this.getArray(apiResult.getData());
        for (int i = 0; i < resultArray.size(); i++) {
            JSONObject resultObject = resultArray.getJSONObject(i);
            long groupId = resultObject.getLongValue("group_id");
            String groupName = resultObject.getString("group_name");
            this.groups.put(groupId, new Group(groupId, groupName, this));
        }
        log.info(String.format("[%s]刷新群列表完成,共有群%d个.", this.botConfig.getBotName(), this.groups.size()));
        return this.groups.values();
    }

    public void flushGroupMembers(Group group) {
        ApiResult apiResult = this.botClient.invokeApi(new GetGroupMembers(group.getGroupId()));
        JSONArray resultArray = this.getArray(apiResult.getData());
        Map<Long, Member> members = this.groupMembers.computeIfAbsent(group.getGroupId(), key -> new ConcurrentHashMap<>());
        for (int i = 0; i < resultArray.size(); i++) {
            JSONObject resultObject = resultArray.getJSONObject(i);
            long userId = resultObject.getLongValue("user_id");
            String nickname = resultObject.getString("nickname");
            String card = resultObject.getString("card");
            String sex = resultObject.getString("sex");
            int age = resultObject.getIntValue("age");
            String area = resultObject.getString("area");
            Date joinTime = resultObject.getDate("join_time");
            Date lastSentTime = resultObject.getDate("last_sent_time");
            String level = resultObject.getString("level");
            String role = resultObject.getString("role");
            boolean unfriendly = resultObject.getBoolean("unfriendly");
            String title = resultObject.getString("title");
            Date titleExpireTime = resultObject.getDate("title_expire_time");
            boolean cardChangeable = resultObject.getBoolean("card_changeable");
            members.put(userId, new Member(userId, group.getGroupId(), nickname, card, sex, age, area, joinTime, lastSentTime, level, role, unfriendly, title, titleExpireTime, cardChangeable, this));
        }
        log.info(String.format("[%s]刷新群%s的成员列表完成,共有群成员%d个", this.botConfig.getBotName(), group.getGroupName(), members.size()));
    }

    public boolean isFriend(long userId) throws InterruptedException, ExecutionException {
        if (!this.completableFuture.isDone()) {
            this.completableFuture.get();
        }
        return this.friends.containsKey(userId);
    }

    public Friend getFriend(long userId) {
        try {
            if (!this.completableFuture.isDone()) {
                this.completableFuture.get();
            }
            return this.friends.get(userId);
        } catch (Exception e) {
            return null;
        }
    }

    public Collection<Friend> getFriends() {
        try {
            if (!this.completableFuture.isDone()) {
                this.completableFuture.get();
            }
            return this.friends.values();
        } catch (Exception e) {
            return null;
        }
    }

    public Group getGroup(long groupId) {
        try {
            if (!this.completableFuture.isDone()) {
                this.completableFuture.get();
            }
            Group group = this.groups.get(groupId);
            if (group == null) {
                ApiResult apiResult = this.botClient.invokeApi(new GetGroup(groupId));
                JSONObject resultObject = this.getObject(apiResult.getData());
                String groupName = resultObject.getString("group_name");
                group = new Group(groupId, groupName, this);
                this.groups.put(groupId, group);
                return group;
            }
            return group;
        } catch (Exception e) {
            return null;
        }
    }

    public Collection<Group> getGroups() {
        try {
            if (!this.completableFuture.isDone()) {
                this.completableFuture.get();
            }
            return this.groups.values();
        } catch (Exception e) {
            return null;
        }
    }

    public Member getMember(long groupId, long userId) {
        try {
            if (!this.completableFuture.isDone()) {
                this.completableFuture.get();
            }
            Map<Long, Member> groupMembers = this.groupMembers.get(groupId);
            if (groupMembers.isEmpty()) {
                this.flushGroupMembers(this.getGroup(groupId));
                groupMembers = this.groupMembers.get(groupId);
            }
            Member member = groupMembers.get(userId);
            if (member == null) {
                ApiResult apiResult = this.botClient.invokeApi(new GetMemberInfo(groupId, userId));
                JSONObject resultObject = this.getObject(apiResult.getData());
                String nickname = resultObject.getString("nickname");
                String card = resultObject.getString("card");
                String sex = resultObject.getString("sex");
                int age = resultObject.getIntValue("age");
                String area = resultObject.getString("area");
                Date joinTime = resultObject.getDate("join_time");
                Date lastSentTime = resultObject.getDate("last_sent_time");
                String level = resultObject.getString("level");
                String role = resultObject.getString("role");
                boolean unfriendly = resultObject.getBoolean("unfriendly");
                String title = resultObject.getString("title");
                Date titleExpireTime = resultObject.getDate("title_expire_time");
                boolean cardChangeable = resultObject.getBoolean("card_changeable");
                member = new Member(userId, groupId, nickname, card, sex, age, area, joinTime, lastSentTime, level, role, unfriendly, title, titleExpireTime, cardChangeable, this);
                groupMembers.put(userId, member);
            }
            return member;
        } catch (Exception e) {
            return null;
        }
    }

    public Collection<Member> getMembers(long groupId) {
        try {
            if (!this.completableFuture.isDone()) {
                this.completableFuture.get();
            }
            Map<Long, Member> groupMembers = this.groupMembers.get(groupId);
            if (groupMembers.isEmpty()) {
                this.flushGroupMembers(this.getGroup(groupId));
                groupMembers = this.groupMembers.get(groupId);
            }
            return groupMembers.values();
        } catch (Exception e) {
            return null;
        }
    }

    private JSONObject getObject(Object object) {
        if (!(object instanceof JSONObject)) {
            throw new BotException(String.format("[%s]调用api失败：解析结果出错。", this.botConfig.getBotName()));
        }
        return (JSONObject) object;
    }

    private JSONArray getArray(Object object) {
        if (!(object instanceof JSONArray)) {
            throw new BotException(String.format("[%s]调用api失败：解析结果出错。", this.botConfig.getBotName()));
        }
        return (JSONArray) object;
    }

    public int sendGroupMessage(long groupId, MessageChain messageChain) {
        ApiResult apiResult = this.botClient.invokeApi(new SendGroupMsg(groupId, messageChain));
        return this.getObject(apiResult.getData()).getIntValue("message_id");
    }

    public int sendGroupForwardMessage(long groupId, List<ForwardNodeMessage> messageList) {
        ApiResult apiResult = this.botClient.invokeApi(new SendGroupForwardMsg(groupId, messageList));
        return this.getObject(apiResult.getData()).getIntValue("message_id");
    }

    public int sendTempMessage(long userId, long groupId, MessageChain messageChain) {
        ApiResult apiResult = this.botClient.invokeApi(new SendTempMsg(userId, groupId, messageChain));
        return this.getObject(apiResult.getData()).getIntValue("message_id");
    }

    public void groupBan(long groupId) {
        this.botClient.invokeApi(new GroupBan(groupId, true));
    }

    public void groupPardon(long groupId) {
        this.botClient.invokeApi(new GroupBan(groupId, false));
    }

    public void memberBan(long groupId, long userId, long duration) {
        this.botClient.invokeApi(new Ban(groupId, userId, duration));
    }

    public void memberPardon(long groupId, long userId) {
        this.botClient.invokeApi(new Ban(groupId, userId, 0));
    }

    public int sendPrivateMessage(long userId, MessageChain messageChain) {
        ApiResult apiResult = this.botClient.invokeApi(new SendPrivateMsg(userId, messageChain));
        return this.getObject(apiResult.getData()).getIntValue("message_id");
    }

    public void deleteMsg(long messageId) {
        this.botClient.invokeApi(new DeleteMsg(messageId));
    }

    public void setGroupCard(long groupId, long userId, String card) {
        this.botClient.invokeApi(new SetGroupCard(groupId, userId, card));
    }

    public void setGroupSpecialTitle(long userId, String specialTitle, Number duration, long groupId) {
        this.botClient.invokeApi(new SetGroupSpecialTitle(userId, specialTitle, duration, groupId));
    }
}
