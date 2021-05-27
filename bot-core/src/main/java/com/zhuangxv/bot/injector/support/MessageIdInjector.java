package com.zhuangxv.bot.injector.support;

import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.event.message.MessageEvent;
import com.zhuangxv.bot.injector.MessageObjectInjector;
import com.zhuangxv.bot.message.MessageChain;

public class MessageIdInjector implements MessageObjectInjector<Integer> {
    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }

    @Override
    public Integer getObject(MessageEvent messageEvent, MessageChain messageChain, Bot bot) {
        return messageEvent.getMessageId();
    }
}
