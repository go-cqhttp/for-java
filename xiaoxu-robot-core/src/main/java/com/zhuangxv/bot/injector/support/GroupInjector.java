package com.zhuangxv.bot.injector.support;

import com.zhuangxv.bot.contact.support.Group;
import com.zhuangxv.bot.event.BaseEvent;
import com.zhuangxv.bot.event.message.GroupMessageEvent;
import com.zhuangxv.bot.injector.ObjectInjector;
import com.zhuangxv.bot.message.MessageChain;

public class GroupInjector implements ObjectInjector<Group> {
    @Override
    public Class<Group> getType() {
        return Group.class;
    }

    @Override
    public Group getObject(BaseEvent baseEvent, MessageChain messageChain) {
        if (!(baseEvent instanceof GroupMessageEvent)) {
            return null;
        }
        return new Group(((GroupMessageEvent) baseEvent).getGroupId());
    }
}
