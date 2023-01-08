package com.zhuangxv.bot.message.support;

import com.alibaba.fastjson.JSON;
import com.zhuangxv.bot.message.Message;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaoxu
 * @since 2022-05-24 10:19
 */
@Data
@NoArgsConstructor
public class AtMessage implements Message {

    private String qq;

    public AtMessage(String qq) {
        this.qq = qq;
    }

    @Override
    public String toString() {
        return "@" + qq + " ";
    }

    @Override
    public String toMessageString() {
        return String.format("{\"type\":\"%s\",\"data\":%s}", "at", JSON.toJSONString(this));
    }

}
