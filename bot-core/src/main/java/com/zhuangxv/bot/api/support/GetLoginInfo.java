package com.zhuangxv.bot.api.support;

import com.zhuangxv.bot.api.BaseApi;

/**
 * @author xiaoxu
 * @since 2022/5/19 17:03
 */
public class GetLoginInfo extends BaseApi {

    @Override
    public String getAction() {
        return "get_login_info";
    }

    @Override
    public Object getParams() {
        return null;
    }

}
