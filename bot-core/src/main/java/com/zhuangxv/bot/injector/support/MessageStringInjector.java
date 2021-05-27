package com.zhuangxv.bot.injector.support;

import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.event.message.MessageEvent;
import com.zhuangxv.bot.injector.MessageObjectInjector;
import com.zhuangxv.bot.message.MessageChain;

public class MessageStringInjector implements MessageObjectInjector<String> {
    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    public String getObject(MessageEvent messageEvent, MessageChain messageChain, Bot bot) {
        return messageChain.toString();
    }
}
