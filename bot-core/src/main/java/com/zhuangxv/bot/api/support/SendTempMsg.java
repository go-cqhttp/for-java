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
public class SendTempMsg extends BaseApi {

    private final SendTempMsg.Param param;

    public SendTempMsg(long userId, long groupId, MessageChain messageChain) {
        this.param = new SendTempMsg.Param();
        this.param.setUserId(userId);
        this.param.setGroupId(groupId);
        this.param.setMessage(JSON.parseArray(messageChain.toMessageString()));
        this.param.setAutoEscape(false);
    }

    public SendTempMsg(long userId, long groupId, MessageChain messageChain, boolean autoEscape) {
        this.param = new SendTempMsg.Param();
        this.param.setUserId(userId);
        this.param.setGroupId(groupId);
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

        @JSONField(name = "group_id")
        private long groupId;

        @JSONField(name = "message")
        private JSONArray message;

        @JSONField(name = "auto_escape")
        private boolean autoEscape;

    }
}
