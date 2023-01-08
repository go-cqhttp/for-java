package com.zhuangxv.bot.event.message;

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
public class MessageEvent extends BaseEvent {

    @JSONField(name = "message_id")
    private Integer messageId;

    @JSONField(name = "message_type")
    private String messageType;

    @JSONField(name = "user_id")
    private Long userId;

}
