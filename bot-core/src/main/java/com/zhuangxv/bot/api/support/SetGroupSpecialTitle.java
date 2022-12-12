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
public class SetGroupSpecialTitle extends BaseApi {

    private final SetGroupSpecialTitle.Param param;

    public SetGroupSpecialTitle(long userId, String specialTitle, Number duration, long groupId){
        this.param = new SetGroupSpecialTitle.Param();
        this.param.setUserId(userId);
        this.param.setSpecialTitle(specialTitle);
        this.param.setDuration(duration);
        this.param.setGroupId(groupId);
    }
    @Override
    public String getAction() {
        return "set_group_special_title";
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

        @JSONField(name = "special_title")
        private String specialTitle;

        @JSONField(name = "duration")
        private Number duration;
    }
}
