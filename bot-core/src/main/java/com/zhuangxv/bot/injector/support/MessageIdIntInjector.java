package com.zhuangxv.bot.injector.support;

import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.event.BaseEvent;
import com.zhuangxv.bot.event.message.GroupRecallEvent;
import com.zhuangxv.bot.event.message.MessageEvent;
import com.zhuangxv.bot.injector.ObjectInjector;
import com.zhuangxv.bot.message.MessageChain;

public class MessageIdIntInjector implements ObjectInjector<Integer> {
    @Override
    public Class<Integer> getClassType() {
        return int.class;
    }

    @Override
    public String[] getType() {
        return new String[]{"message", "recallMessage"};
    }

    @Override
    public Integer getObject(BaseEvent event, Bot bot) {
        if (event instanceof MessageEvent) {
            return ((MessageEvent) event).getMessageId();
        }
        if (event instanceof GroupRecallEvent) {
            return ((GroupRecallEvent) event).getMessageId();
        }
        return null;
    }
}
