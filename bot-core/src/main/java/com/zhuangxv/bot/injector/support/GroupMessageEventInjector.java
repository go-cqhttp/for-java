package com.zhuangxv.bot.injector.support;

import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.event.BaseEvent;
import com.zhuangxv.bot.event.message.GroupMessageEvent;
import com.zhuangxv.bot.event.message.MessageEvent;
import com.zhuangxv.bot.injector.ObjectInjector;
import com.zhuangxv.bot.message.MessageChain;

public class GroupMessageEventInjector implements ObjectInjector<GroupMessageEvent> {
    @Override
    public Class<GroupMessageEvent> getClassType() {
        return GroupMessageEvent.class;
    }

    @Override
    public String[] getType() {
        return new String[]{"message"};
    }

    @Override
    public GroupMessageEvent getObject(BaseEvent event, Bot bot) {
        if (event instanceof GroupMessageEvent) {
            return (GroupMessageEvent) event;
        }
        return null;
    }
}
