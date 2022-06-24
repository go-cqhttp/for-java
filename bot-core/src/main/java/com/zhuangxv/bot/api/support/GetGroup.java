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
public class GetGroup extends BaseApi {

    private final GetGroup.Param param;

    public GetGroup(long groupId) {
        this.param = new GetGroup.Param();
        this.param.setGroupId(groupId);
    }

    @Override
    public String getAction() {
        return "get_group_info";
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
