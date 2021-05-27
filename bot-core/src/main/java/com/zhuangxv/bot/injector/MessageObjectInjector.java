package com.zhuangxv.bot.injector;

import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.event.BaseEvent;
import com.zhuangxv.bot.event.message.MessageEvent;
import com.zhuangxv.bot.message.MessageChain;

public interface MessageObjectInjector<T> {

    Class<T> getType();

    T getObject(MessageEvent messageEvent, MessageChain messageChain, Bot bot);

}