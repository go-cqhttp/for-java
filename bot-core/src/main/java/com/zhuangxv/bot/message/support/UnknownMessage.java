package com.zhuangxv.bot.message.support;

import com.alibaba.fastjson.JSON;
import com.zhuangxv.bot.message.Message;
import lombok.Data;

/**
 * @author xiaoxu
 * @since 2022-05-24 10:19
 */
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
