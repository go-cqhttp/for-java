package com.zhuangxv.bot.handler;

import com.alibaba.fastjson.JSONObject;

public interface EventHandler {

    void handle(JSONObject jsonObject);

}
