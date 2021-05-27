package com.zhuangxv.bot.api;

import com.alibaba.fastjson.JSON;
import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.core.BotFactory;
import com.zhuangxv.bot.core.IdGenerator;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseApi {

    public abstract String getAction();

    public abstract Object getParams();

    public boolean needSleep() {
        return true;
    }

    private String echo = null;

    public String getEcho() {
        if (StringUtils.isEmpty(echo)) {
            IdGenerator idGenerator = BotFactory.getBeanByClass(IdGenerator.class);
            assert idGenerator != null;
            this.echo = idGenerator.createStrId();
        }
        return this.echo;
    }

    public String buildJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("action", this.getAction());
        map.put("params", this.getParams());
        map.put("echo", this.getEcho());
        return JSON.toJSONString(map);
    }

}
