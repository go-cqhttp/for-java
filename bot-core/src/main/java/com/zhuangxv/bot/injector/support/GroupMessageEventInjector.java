package com.zhuangxv.bot.injector.support;

import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.event.message.GroupMessageEvent;
import com.zhuangxv.bot.event.message.MessageEvent;
import com.zhuangxv.bot.injector.MessageObjectInjector;
import com.zhuangxv.bot.message.MessageChain;

public class GroupMessageEventInjector implements MessageObjectInjector<GroupMessageEvent> {
    @Override
    public Class<GroupMessageEvent> getType() {
        return GroupMessageEvent.class;
    }

    @Override
    public GroupMessageEvent getObject(MessageEvent messageEvent, MessageChain messageChain, Bot bot) {
        if (!(messageEvent instanceof GroupMessageEvent)) {
            return null;
        }
        return (GroupMessageEvent) messageEvent;
    }
}
