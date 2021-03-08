package com.zhuangxv.bot.injector.support;

import com.zhuangxv.bot.event.message.MessageEvent;
import com.zhuangxv.bot.injector.MessageObjectInjector;
import com.zhuangxv.bot.message.MessageChain;

public class MessageChainInjector implements MessageObjectInjector<MessageChain> {
    @Override
    public Class<MessageChain> getType() {
        return MessageChain.class;
    }

    @Override
    public MessageChain getObject(MessageEvent messageEvent, MessageChain messageChain) {
        return messageChain;
    }
}
