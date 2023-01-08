package com.zhuangxv.bot.core.component;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author xiaoxu
 * @since 2022-05-24 10:19
 */
@Data
public class HandlerMethod {

    private Class<?> type;

    private Object object;

    private Method method;

}
