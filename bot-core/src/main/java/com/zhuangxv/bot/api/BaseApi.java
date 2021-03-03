package com.zhuangxv.bot.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public interface BaseApi {

    String getAction();

    Object getParams();

    String getEcho();

    default String buildJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("action", this.getAction());
        map.put("params", this.getParams());
        map.put("echo", this.getEcho());
        return JSON.toJSONString(map);
    }

}
