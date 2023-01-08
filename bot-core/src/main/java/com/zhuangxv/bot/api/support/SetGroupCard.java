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
public class SetGroupCard extends BaseApi {

    private final SetGroupCard.Param param;

    public SetGroupCard(long groupId, long userId, String card) {
        this.param = new SetGroupCard.Param();
        this.param.setGroupId(groupId);
        this.param.setUserId(userId);
        this.param.setCard(card);
    }

    @Override
    public String getAction() {
        return "set_group_card";
    }

    @Override
    public Object getParams() {
        return param;
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Param {

        @JSONField(name = "group_id")
        private long groupId;

        @JSONField(name = "user_id")
        private long userId;

        @JSONField(name = "card")
        private String card;

    }
}
