package com.zhuangxv.bot.handler;

import com.alibaba.fastjson.JSONObject;
import com.zhuangxv.bot.core.Bot;

public interface EventHandler {

    void handle(JSONObject jsonObject, Bot bot);

}
