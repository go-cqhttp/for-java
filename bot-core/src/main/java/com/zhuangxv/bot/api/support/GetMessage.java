package com.zhuangxv.bot.api.support;

import com.alibaba.fastjson.annotation.JSONField;
import com.zhuangxv.bot.api.BaseApi;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaoxu
 * @since 2022-05-24 10:19
 */
public class GetMessage extends BaseApi {

    private final GetMessage.Param param;

    public GetMessage(int messageId) {
        this.param = new GetMessage.Param();
        this.param.setMessageId(messageId);
    }

    @Override
    public String getAction() {
        return "get_msg";
    }

    @Override
    public Object getParams() {
        return param;
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Param {

        @JSONField(name = "message_id")
        private int messageId;

    }
}
