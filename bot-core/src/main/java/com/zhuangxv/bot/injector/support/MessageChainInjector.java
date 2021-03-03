package com.zhuangxv.bot.injector.support;

import com.zhuangxv.bot.event.BaseEvent;
import com.zhuangxv.bot.injector.ObjectInjector;
import com.zhuangxv.bot.message.MessageChain;

public class MessageChainInjector implements ObjectInjector<MessageChain> {
    @Override
    public Class<MessageChain> getType() {
        return MessageChain.class;
    }

    @Override
    public MessageChain getObject(BaseEvent baseEvent, MessageChain messageChain) {
        return messageChain;
    }
}
