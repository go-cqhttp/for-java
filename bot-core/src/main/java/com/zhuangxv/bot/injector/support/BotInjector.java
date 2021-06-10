package com.zhuangxv.bot.injector.support;

import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.event.message.MessageEvent;
import com.zhuangxv.bot.injector.MessageObjectInjector;
import com.zhuangxv.bot.message.MessageChain;

public class BotInjector implements MessageObjectInjector<Bot> {
    @Override
    public Class<Bot> getType() {
        return Bot.class;
    }

    @Override
    public Bot getObject(MessageEvent messageEvent, MessageChain messageChain, Bot bot) {
        return bot;
    }
}
