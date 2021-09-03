package com.zhuangxv.bot.injector;

import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.event.BaseEvent;

public interface ObjectInjector<T> {

    String[] getType();

    Class<T> getClassType();

    T getObject(BaseEvent event, Bot bot);

}