package com.zhuangxv.bot.injector.support;

import com.zhuangxv.bot.event.BaseEvent;
import com.zhuangxv.bot.injector.ObjectInjector;
import com.zhuangxv.bot.message.MessageChain;

public class MessageStringInjector implements ObjectInjector<String> {
    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    public String getObject(BaseEvent baseEvent, MessageChain messageChain) {
        return messageChain.toString();
    }
}
