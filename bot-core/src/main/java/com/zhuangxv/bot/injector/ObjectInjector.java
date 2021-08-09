package com.zhuangxv.bot.injector;

import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.event.BaseEvent;
import com.zhuangxv.bot.event.message.MessageEvent;
import com.zhuangxv.bot.message.MessageChain;

public interface ObjectInjector<T> {

    String[] getType();

    Class<T> getClassType();

    T getObject(BaseEvent event, Bot bot);

}