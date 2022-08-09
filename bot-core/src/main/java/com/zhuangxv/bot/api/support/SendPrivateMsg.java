package com.zhuangxv.bot.api.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import com.zhuangxv.bot.api.BaseApi;
import com.zhuangxv.bot.message.MessageChain;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaoxu
 * @since 2022-05-24 10:19
 */
public class SendPrivateMsg extends BaseApi {

    private final SendPrivateMsg.Param param;

    public SendPrivateMsg(long userId, MessageChain messageChain) {
        this.param = new SendPrivateMsg.Param();
        this.param.setUserId(userId);
        this.param.setMessage(JSON.parseArray(messageChain.toMessageString()));
        this.param.setAutoEscape(false);
    }

    public SendPrivateMsg(long userId, MessageChain messageChain, boolean autoEscape) {
        this.param = new SendPrivateMsg.Param();
        this.param.setUserId(userId);
        this.param.setMessage(JSON.parseArray(messageChain.toMessageString()));
        this.param.setAutoEscape(autoEscape);
    }

    @Override
    public boolean needSleep() {
        return true;
    }

    @Override
    public String getAction() {
        return "send_private_msg";
    }

    @Override
    public Object getParams() {
        return param;
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
