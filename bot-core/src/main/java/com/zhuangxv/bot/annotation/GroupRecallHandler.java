package com.zhuangxv.bot.annotation;

import java.lang.annotation.*;

/**
 * @author xiaoxu
 * @since 2021/8/9 11:15 上午
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GroupRecallHandler {

    /**
     * 限制某个群
     */
    long[] groupIds() default {};

    /**
     * 排除某个群
     */
    long[] excludeGroupIds() default {};

    /**
     * 限制发言人
     */
    long[] senderIds() default {};

    /**
     * 排除发言人
     */
    long[] excludeSenderIds() default {};

}
