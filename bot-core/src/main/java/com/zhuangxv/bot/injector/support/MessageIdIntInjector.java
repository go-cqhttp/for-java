package com.zhuangxv.bot.injector.support;

import com.zhuangxv.bot.event.message.MessageEvent;
import com.zhuangxv.bot.injector.MessageObjectInjector;
import com.zhuangxv.bot.message.MessageChain;

public class MessageIdIntInjector implements MessageObjectInjector<Integer> {
    @Override
    public Class<Integer> getType() {
        return int.class;
    }

    @Override
    public Integer getObject(MessageEvent messageEvent, MessageChain messageChain) {
        return messageEvent.getMessageId();
    }
}
