package com.zhuangxv.bot.message.support;

import com.alibaba.fastjson.JSON;
import com.zhuangxv.bot.message.Message;
import lombok.Data;

/**
 * @author xiaoxu
 * @since 2022-05-24 10:19
 */
@Data
public class FaceMessage implements Message {

    private String id;

    public FaceMessage(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "face[" + id + "]";
    }

    @Override
    public String toMessageString() {
        return String.format("{\"type\":\"%s\",\"data\":%s}", "face", JSON.toJSONString(this));
    }

}
