package com.zhuangxv.bot.event.message;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.zhuangxv.bot.event.BaseEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GroupMessageEvent extends BaseEvent {

    @JSONField(name = "message_type")
    private String messageType;

    @JSONField(name = "sub_type")
    private String subType;

    @JSONField(name = "message_id")
    private Integer messageId;

    @JSONField(name = "group_id")
    private Long groupId;

    @JSONField(name = "user_id")
    private Long userId;

    @JSONField(name = "anonymous")
    private JSONObject anonymous;

    @JSONField(name = "message")
    private JSONArray message;

    @JSONField(name = "raw_message")
    private String rawMessage;

    @JSONField(name = "font")
    private Integer font;

    @JSONField(name = "sender")
    private JSONObject sender;

    public static boolean isSupport(JSONObject jsonObject) {
        return ("message".equals(jsonObject.getString("post_type"))
                && "group".equals(jsonObject.getString("message_type")));
    }

}
