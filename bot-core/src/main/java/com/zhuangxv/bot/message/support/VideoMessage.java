package com.zhuangxv.bot.message.support;

import com.alibaba.fastjson.JSON;
import com.zhuangxv.bot.message.Message;
import lombok.Data;

@Data
public class VideoMessage implements Message {

    private String file;

    private String cover;

    @Override
    public String toString() {
        return "video[" + file + "]";
    }

    @Override
    public String toMessageString() {
        return String.format("{\"type\":\"%s\",\"data\":%s}", "video", JSON.toJSONString(this));
    }

}
