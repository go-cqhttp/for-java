package com.zhuangxv.bot.injector.support;

import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.event.BaseEvent;
import com.zhuangxv.bot.injector.ObjectInjector;

/**
 * @author xiaoxu
 * @since 2022-05-24 10:19
 */
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
