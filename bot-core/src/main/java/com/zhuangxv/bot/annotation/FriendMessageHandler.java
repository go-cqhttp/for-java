package com.zhuangxv.bot.annotation;

import java.lang.annotation.*;

/**
 * @author xiaoxu
 * @since 2020-08-07 16:00
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FriendMessageHandler {

    /**
     * 限制bot 参数为bot qq  0为不限制
     */
    long bot() default 0;

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
