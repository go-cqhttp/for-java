package com.zhuangxv.bot.injector.support;

import com.zhuangxv.bot.contact.support.Group;
import com.zhuangxv.bot.event.BaseEvent;
import com.zhuangxv.bot.event.message.GroupMessageEvent;
import com.zhuangxv.bot.injector.ObjectInjector;
import com.zhuangxv.bot.message.MessageChain;

public class GroupMessageEventInjector implements ObjectInjector<GroupMessageEvent> {
    @Override
    public Class<GroupMessageEvent> getType() {
        return GroupMessageEvent.class;
    }

    @Override
    public GroupMessageEvent getObject(BaseEvent baseEvent, MessageChain messageChain) {
        if (!(baseEvent instanceof GroupMessageEvent)) {
            return null;
        }
        return (GroupMessageEvent) baseEvent;
    }
}
