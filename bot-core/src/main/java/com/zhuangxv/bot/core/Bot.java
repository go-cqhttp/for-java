package com.zhuangxv.bot.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhuangxv.bot.api.ApiResult;
import com.zhuangxv.bot.api.support.*;
import com.zhuangxv.bot.config.BotConfig;
import com.zhuangxv.bot.contact.support.Friend;
import com.zhuangxv.bot.exception.BotException;
import com.zhuangxv.bot.message.CacheMessage;
import com.zhuangxv.bot.message.Message;
import com.zhuangxv.bot.message.MessageChain;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author xiaoxu
 * @since 2021/5/27 10:36
 */
@Slf4j
public class Bot {

    private final List<Friend> friends = new ArrayList<>();
    private final Map<String, Map<Integer, CacheMessage>> cacheMessageChain = new HashMap<>();
    private final Lock cacheMessageChainLock = new ReentrantLock();
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
            Friend friend = new Friend(resultObject.getLong("user_id"), this);
            friend.setNickname(resultObject.getString("nickname"));
            friend.setRemark(resultObject.getString("remark"));
            this.friends.add(friend);
        }
        log.info(String.format("[%s]刷新好友列表完成,共有好友%d个.", this.botConfig.getBotName(), this.friends.size()));
    }

    public boolean isFriend(long userId) {
        for (Friend friend : this.friends) {
            if (friend.getUserId() == userId) {
                return true;
            }
        }
        return false;
    }

    public int sendGroupMessage(long groupId, MessageChain messageChain) {
        ApiResult apiResult = this.botClient.invokeApi(new SendGroupMsg(groupId, messageChain));
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

    public JSONObject getMemberInfo(long groupId, long userId) {
        ApiResult apiResult = this.botClient.invokeApi(new GetMemberInfo(groupId, userId));
        return this.getObject(apiResult.getData());
    }

    public int sendPrivateMessage(long userId, MessageChain messageChain) {
        ApiResult apiResult = this.botClient.invokeApi(new SendPrivateMsg(userId, messageChain));
        return this.getObject(apiResult.getData()).getIntValue("message_id");
    }

    public void deleteMsg(long messageId) {
        this.botClient.invokeApi(new DeleteMsg(messageId));
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

}
