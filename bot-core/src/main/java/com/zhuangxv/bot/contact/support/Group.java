package com.zhuangxv.bot.contact.support;

import com.zhuangxv.bot.api.ApiResult;
import com.zhuangxv.bot.api.support.GroupBan;
import com.zhuangxv.bot.api.support.SendGroupMsg;
import com.zhuangxv.bot.api.support.SendPrivateMsg;
import com.zhuangxv.bot.contact.Contact;
import com.zhuangxv.bot.core.BotApplication;
import com.zhuangxv.bot.exception.BotException;
import com.zhuangxv.bot.message.Message;
import com.zhuangxv.bot.message.MessageChain;

public class Group implements Contact {

    private final long groupId;

    public Group(long groupId) {
        this.groupId = groupId;
    }

    public long getGroupId() {
        return groupId;
    }

    @Override
    public int sendMessage(MessageChain messageChain) {
        ApiResult apiResult = BotApplication.invokeApi(SendGroupMsg.buildApi(this.groupId, messageChain));
        if (apiResult == null || !"ok".equals(apiResult.getStatus())) {
            throw new BotException("调用api出错: " + apiResult);
        }
        return apiResult.getData().getIntValue("message_id");
    }

    public int sendTempMessage(long userId, Message message) {
        MessageChain messageChain = new MessageChain();
        messageChain.add(message);
        return this.sendTempMessage(userId, messageChain);
    }

    public int sendTempMessage(long userId, MessageChain messageChain) {
        ApiResult apiResult = BotApplication.invokeApi(SendPrivateMsg.buildApi(userId, messageChain));
        if (apiResult == null || !"ok".equals(apiResult.getStatus())) {
            throw new BotException("调用api出错: " + apiResult);
        }
        return apiResult.getData().getIntValue("message_id");
    }

    public void groupBan() {
        ApiResult apiResult = BotApplication.invokeApi(GroupBan.buildApi(this.groupId, true));
        if (apiResult == null || !"ok".equals(apiResult.getStatus())) {
            throw new BotException("调用api出错: " + apiResult);
        }
    }

    public void groupPardon() {
        ApiResult apiResult = BotApplication.invokeApi(GroupBan.buildApi(this.groupId, false));
        if (apiResult == null || !"ok".equals(apiResult.getStatus())) {
            throw new BotException("调用api出错: " + apiResult);
        }
    }

}
