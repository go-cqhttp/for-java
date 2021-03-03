package com.zhuangxv.bot.api.support;

import com.alibaba.fastjson.annotation.JSONField;
import com.zhuangxv.bot.api.BaseApi;
import com.zhuangxv.bot.message.MessageChain;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

public class GroupBan implements BaseApi {

    private final GroupBan.Param param;

    public static GroupBan buildApi(long groupId, boolean enable) {
        GroupBan.Param param = new GroupBan.Param();
        param.setGroupId(groupId);
        param.setEnable(enable);
        return new GroupBan(param);
    }

    private GroupBan(GroupBan.Param param) {
        this.param = param;
    }

    @Override
    public String getAction() {
        return "set_group_whole_ban";
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

        @JSONField(name = "enable")
        private boolean enable = true;

    }
}
