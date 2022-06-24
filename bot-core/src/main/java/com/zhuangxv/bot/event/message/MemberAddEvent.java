package com.zhuangxv.bot.event.message;

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
public class MemberAddEvent extends BaseEvent {

    @JSONField(name = "notice_type")
    private String noticeType;

    @JSONField(name = "group_id")
    private Long groupId;

    @JSONField(name = "user_id")
    private Long userId;

    public static boolean isSupport(JSONObject jsonObject) {
        return ("notice".equals(jsonObject.getString("post_type"))
                && "group_increase".equals(jsonObject.getString("notice_type")));
    }

}
