package com.zhuangxv.bot.event;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class BaseEvent {

    @JSONField(name = "post_type")
    private String eventType;

    @JSONField(name = "self_id")
    private Long botQq;

    @JSONField(name = "time")
    private Long time;

}
