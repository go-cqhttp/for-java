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
public class GetGroupMembers extends BaseApi {

    private final GetGroupMembers.Param param;

    public GetGroupMembers(long groupId) {
        this.param = new GetGroupMembers.Param();
        this.param.setGroupId(groupId);
    }

    @Override
    public String getAction() {
        return "get_group_member_list";
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
    }
}
