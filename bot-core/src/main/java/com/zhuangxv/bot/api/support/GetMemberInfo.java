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
public class GetMemberInfo extends BaseApi {

    private final GetMemberInfo.Param param;

    public GetMemberInfo(long groupId, long userId) {
        this.param = new GetMemberInfo.Param();
        this.param.setGroupId(groupId);
        this.param.setUserId(userId);
        this.param.setNoCache(false);
    }

    public GetMemberInfo(long groupId, long userId, boolean noCache) {
        this.param = new GetMemberInfo.Param();
        this.param.setGroupId(groupId);
        this.param.setUserId(userId);
        this.param.setNoCache(noCache);
    }

    @Override
    public String getAction() {
        return "get_group_member_info";
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

        @JSONField(name = "no_cache")
        private boolean noCache = true;

    }
}
