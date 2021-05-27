package com.zhuangxv.bot.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhuangxv.bot.api.ApiResult;
import com.zhuangxv.bot.api.support.*;
import com.zhuangxv.bot.config.BotConfig;
import com.zhuangxv.bot.contact.support.Friend;
import com.zhuangxv.bot.exception.BotException;
import com.zhuangxv.bot.message.MessageChain;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaoxu
 * @since 2021/5/27 10:36
 */
@Slf4j
public class Bot {

    private final List<Friend> friends = new ArrayList<>();
    private final BotConfig botConfig;
    private final BotClient botClient;

    protected Bot(BotConfig botConfig, BotDispatcher botDispatcher) {
        this.botConfig = botConfig;
        this.botClient = new BotClient(botConfig, botDispatcher, this);
    }

    public BotClient getBotClient() {
        return this.botClient;
    }

    public void flushFriends() {
        log.info(String.format("[%s]正在刷新好友列表.", this.botConfig.getBotName()));
        ApiResult apiResult = this.botClient.invokeApi(new GetFriends());
        JSONArray resultArray = getArray(apiResult.getData());
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
        return getObject(apiResult.getData()).getIntValue("message_id");
    }

    public int sendTempMessage(long userId, long groupId, MessageChain messageChain) {
        ApiResult apiResult = this.botClient.invokeApi(new SendTempMsg(userId, groupId, messageChain));
        return getObject(apiResult.getData()).getIntValue("message_id");
    }

    public void groupBan(long groupId) {
        this.botClient.invokeApi(new GroupBan(groupId, true));
    }

    public void groupPardon(long groupId) {
        this.botClient.invokeApi(new GroupBan(groupId, false));
    }

    public JSONObject getMemberInfo(long groupId, long userId) {
        ApiResult apiResult = this.botClient.invokeApi(new GetMemberInfo(groupId, userId));
        return getObject(apiResult.getData());
    }

    public int sendPrivateMessage(long userId, MessageChain messageChain) {
        ApiResult apiResult = this.botClient.invokeApi(new SendPrivateMsg(userId, messageChain));
        return getObject(apiResult.getData()).getIntValue("message_id");
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
