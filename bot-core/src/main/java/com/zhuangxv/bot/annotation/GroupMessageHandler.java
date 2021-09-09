package com.zhuangxv.bot.annotation;

import com.zhuangxv.bot.utilEnum.IgnoreItselfEnum;

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

    /**
     * 是否被@
     */
    boolean isAt() default false;

    /**
     * 忽略自身
     */
    IgnoreItselfEnum ignoreItself() default IgnoreItselfEnum.IGNORE_ITSELF;

}
