package com.zhuangxv.bot.api.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import com.zhuangxv.bot.api.BaseApi;
import com.zhuangxv.bot.message.MessageChain;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

public class SendPrivateMsg implements BaseApi {

    private final SendPrivateMsg.Param param;

    public static SendPrivateMsg buildApi(long groupId, MessageChain messageChain) {
        return buildApi(groupId, messageChain, false);
    }

    public static SendPrivateMsg buildApi(long userId, MessageChain messageChain, boolean autoEscape) {
        SendPrivateMsg.Param param = new SendPrivateMsg.Param();
        param.setUserId(userId);
        param.setMessage(JSON.parseArray(messageChain.toMessageString()));
        param.setAutoEscape(autoEscape);
        return new SendPrivateMsg(param);
    }

    private SendPrivateMsg(SendPrivateMsg.Param param) {
        this.param = param;
    }

    @Override
    public String getAction() {
        return "send_private_msg";
    }

    @Override
    public Object getParams() {
        return param;
    }

    @Override
    public String getEcho() {
        return "no";
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Param {

        @JSONField(name = "user_id")
        private long userId;

        @JSONField(name = "message")
        private JSONArray message;

        @JSONField(name = "auto_escape")
        private boolean autoEscape;

    }
}
