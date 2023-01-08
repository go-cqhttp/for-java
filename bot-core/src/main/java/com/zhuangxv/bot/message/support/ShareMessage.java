package com.zhuangxv.bot.message.support;

import com.alibaba.fastjson.JSON;
import com.zhuangxv.bot.message.Message;
import lombok.Data;

/**
 * @author xiaoxu
 * @since 2022-05-24 10:19
 */
@Data
public class ShareMessage implements Message {

    private String url;

    private String title;

    private String content;

    private String image;

    @Override
    public String toString() {
        return "share[" + title + "]";
    }

    @Override
    public String toMessageString() {
        return String.format("{\"type\":\"%s\",\"data\":%s}", "share", JSON.toJSONString(this));
    }

}
