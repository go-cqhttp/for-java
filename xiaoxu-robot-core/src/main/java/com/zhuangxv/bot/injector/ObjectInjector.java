package com.zhuangxv.bot.injector;

import com.zhuangxv.bot.event.BaseEvent;
import com.zhuangxv.bot.message.MessageChain;

public interface ObjectInjector<T> {

    Class<T> getType();

    T getObject(BaseEvent baseEvent, MessageChain messageChain);

}