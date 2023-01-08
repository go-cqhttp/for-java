package com.zhuangxv.bot.message;

import com.alibaba.fastjson.JSONObject;
import com.zhuangxv.bot.message.support.*;
import org.springframework.util.StringUtils;

/**
 * @author xiaoxu
 * @since 2022-05-24 10:19
 */
public class MessageTypeHandle {

    public static Message getMessage(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        String type = jsonObject.getString("type");
        if (StringUtils.isEmpty(type)) {
            return null;
        }
        Class<? extends Message> messageClass;
        switch (type) {
            case "text":
                messageClass = TextMessage.class;
                break;
            case "face":
                messageClass = FaceMessage.class;
                break;
            case "record":
                messageClass = RecordMessage.class;
                break;
            case "image":
                messageClass = ImageMessage.class;
                break;
            case "at":
                messageClass = AtMessage.class;
                break;
            case "video":
                messageClass = VideoMessage.class;
                break;
            case "share":
                messageClass = ShareMessage.class;
                break;
            case "reply":
                messageClass = ReplyMessage.class;
                break;
            default:
                return new UnknownMessage() {{
                    setJson(jsonObject.toJSONString());
                }};
        }
        return jsonObject.getObject("data", messageClass);
    }

}
