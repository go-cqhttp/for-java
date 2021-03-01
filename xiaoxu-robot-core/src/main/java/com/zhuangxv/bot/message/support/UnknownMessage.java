package com.zhuangxv.bot.message.support;

import com.alibaba.fastjson.JSON;
import com.zhuangxv.bot.message.Message;
import lombok.Data;

@Data
public class UnknownMessage implements Message {

    private String json;

    @Override
    public String toString() {
        return "json[" + json + "]";
    }

    @Override
    public String toMessageString() {
        return this.json;
    }
}
