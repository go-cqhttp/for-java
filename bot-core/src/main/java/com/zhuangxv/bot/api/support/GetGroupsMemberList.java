package com.zhuangxv.bot.api.support;

import com.alibaba.fastjson.annotation.JSONField;
import com.zhuangxv.bot.api.BaseApi;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

public class GetGroupsMemberList extends BaseApi {

    private final GetGroupsMemberList.Param param;

    public GetGroupsMemberList(long groupId) {
        this.param = new GetGroupsMemberList.Param();
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
