package com.zhuangxv.bot.api.support;

import com.zhuangxv.bot.api.BaseApi;
import com.zhuangxv.bot.core.Bot;

public class GetFriends extends BaseApi {

    public GetFriends() {

    }

    @Override
    public String getAction() {
        return "get_friend_list";
    }

    @Override
    public Object getParams() {
        return "";
    }

    @Override
    public boolean needSleep() {
        return false;
    }
}
