package com.zhuangxv.bot.injector.support;

import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.event.BaseEvent;
import com.zhuangxv.bot.event.message.GroupRecallEvent;
import com.zhuangxv.bot.event.message.MessageEvent;
import com.zhuangxv.bot.injector.ObjectInjector;

/**
 * @author xiaoxu
 * @since 2022-05-24 10:19
 */
public class MessageIdInjector implements ObjectInjector<Integer> {
    @Override
    public Class<Integer> getClassType() {
        return Integer.class;
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
