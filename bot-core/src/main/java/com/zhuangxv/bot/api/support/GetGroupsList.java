package com.zhuangxv.bot.api.support;

import com.zhuangxv.bot.api.BaseApi;

public class GetGroupsList extends BaseApi {

    @Override
    public String getAction() {
        return "get_group_list";
    }

    @Override
    public Object getParams() {
        return null;
    }
}
