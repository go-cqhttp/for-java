package com.zhuangxv.bot.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GroupMessageHandler {

    /**
     * 匹配正则
     */
    String regex() default "none";

    /**
     * 限制某个群
     */
    long groupId() default 0;

    /**
     * 限制发言人
     */
    long senderId() default 0;

    /**
     * 是否被@
     */
    boolean isAt() default false;

}
