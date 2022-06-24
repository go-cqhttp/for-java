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
public class Ban extends BaseApi {

    private final Ban.Param param;

    public Ban(long groupId, long userId, long duration) {
        this.param = new Ban.Param();
        this.param.setGroupId(groupId);
        this.param.setUserId(userId);
        this.param.setDuration(duration);
    }

    @Override
    public String getAction() {
        return "set_group_ban";
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

        @JSONField(name = "duration")
        private long duration;

    }
}
