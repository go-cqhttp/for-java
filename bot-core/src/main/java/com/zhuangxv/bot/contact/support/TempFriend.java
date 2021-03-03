package com.zhuangxv.bot.contact.support;

import com.zhuangxv.bot.api.ApiResult;
import com.zhuangxv.bot.api.support.SendGroupMsg;
import com.zhuangxv.bot.api.support.SendPrivateMsg;
import com.zhuangxv.bot.contact.Contact;
import com.zhuangxv.bot.core.BotApplication;
import com.zhuangxv.bot.exception.BotException;
import com.zhuangxv.bot.message.Message;
import com.zhuangxv.bot.message.MessageChain;

public class TempFriend implements Contact {

    private final long userId;

    public TempFriend(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return this.userId;
    }

    @Override
    public int sendMessage(MessageChain messageChain) {
        SendPrivateMsg baseApi = SendPrivateMsg.buildApi(this.userId, messageChain);
        ApiResult apiResult = BotApplication.invokeApi(baseApi);
        if (apiResult == null || !"ok".equals(apiResult.getStatus())) {
            throw new BotException("调用api出错: " + apiResult);
        }
        return apiResult.getData().getIntValue("message_id");
    }

}
