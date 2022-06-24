package com.zhuangxv.bot.event;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class BaseEvent {

    @JSONField(name = "post_type")
    private String postType;

    @JSONField(name = "self_id")
    private Long selfId;

    @JSONField(name = "time")
    private Long time;

}
