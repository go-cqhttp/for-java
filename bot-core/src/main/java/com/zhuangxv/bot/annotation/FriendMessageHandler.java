package com.zhuangxv.bot.annotation;

import java.lang.annotation.*;

/**
 * @author xiaoxu
 * @date 2020-08-07 16:00
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FriendMessageHandler {
    /**
     * 匹配正则
     */
    String regex() default "none";

    /**
     * 限制发言人
     */
    long[] senderIds() default {};

    long[] excludeSenderIds() default {};

}
