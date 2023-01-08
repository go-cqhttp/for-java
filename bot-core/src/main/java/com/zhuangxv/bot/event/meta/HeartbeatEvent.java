package com.zhuangxv.bot.event.meta;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.zhuangxv.bot.event.BaseEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author xiaoxu
 * @since 2022-05-24 10:19
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HeartbeatEvent extends BaseEvent {

    @JSONField(name = "meta_event_type")
    private String metaEventType;

    @JSONField(name = "interval")
    private Long interval;

    public static boolean isSupport(JSONObject jsonObject) {
        return ("meta_event".equals(jsonObject.getString("post_type"))
                && "heartbeat".equals(jsonObject.getString("meta_event_type")));
    }

}
