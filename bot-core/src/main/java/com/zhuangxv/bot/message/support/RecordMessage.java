package com.zhuangxv.bot.message.support;

import com.alibaba.fastjson.JSON;
import com.zhuangxv.bot.message.Message;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecordMessage implements Message {

    private String file;

    private String url;

    public RecordMessage(String file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "record[" + file + "]";
    }

    @Override
    public String toMessageString() {
        return String.format("{\"type\":\"%s\",\"data\":%s}", "record", JSON.toJSONString(this));
    }

}
