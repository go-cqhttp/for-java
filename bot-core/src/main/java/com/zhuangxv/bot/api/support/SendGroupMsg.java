package com.zhuangxv.bot.api.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import com.zhuangxv.bot.api.BaseApi;
import com.zhuangxv.bot.message.MessageChain;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

public class SendGroupMsg implements BaseApi {

    private final SendGroupMsg.Param param;

    public static SendGroupMsg buildApi(long groupId, MessageChain messageChain) {
        return buildApi(groupId, messageChain, false);
    }

    public static SendGroupMsg buildApi(long groupId, MessageChain messageChain, boolean autoEscape) {
        SendGroupMsg.Param param = new SendGroupMsg.Param();
        param.setGroupId(groupId);
        param.setMessage(JSON.parseArray(messageChain.toMessageString()));
        param.setAutoEscape(autoEscape);
        return new SendGroupMsg(param);
    }

    private SendGroupMsg(SendGroupMsg.Param param) {
        this.param = param;
    }

    @Override
    public String getAction() {
        return "send_group_msg";
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

        @JSONField(name = "group_id")
        private long groupId;

        @JSONField(name = "message")
        private JSONArray message;

        @JSONField(name = "auto_escape")
        private boolean autoEscape;

    }
}
