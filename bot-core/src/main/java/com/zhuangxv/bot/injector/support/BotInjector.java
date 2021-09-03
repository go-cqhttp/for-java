package com.zhuangxv.bot.injector.support;

import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.event.BaseEvent;
import com.zhuangxv.bot.injector.ObjectInjector;

public class BotInjector implements ObjectInjector<Bot> {
    @Override
    public Class<Bot> getClassType() {
        return Bot.class;
    }

    @Override
    public String[] getType() {
        return new String[]{"all"};
    }

    @Override
    public Bot getObject(BaseEvent event, Bot bot) {
        return bot;
    }
}
