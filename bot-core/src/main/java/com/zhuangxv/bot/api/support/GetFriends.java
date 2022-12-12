package com.zhuangxv.bot.api.support;

import com.zhuangxv.bot.api.BaseApi;

/**
 * @author xiaoxu
 * @since 2022-05-24 10:19
 */
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

}
