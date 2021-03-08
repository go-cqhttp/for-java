package com.zhuangxv.bot.injector.support;

import com.zhuangxv.bot.contact.support.Group;
import com.zhuangxv.bot.event.message.GroupMessageEvent;
import com.zhuangxv.bot.event.message.MessageEvent;
import com.zhuangxv.bot.injector.MessageObjectInjector;
import com.zhuangxv.bot.message.MessageChain;

public class GroupInjector implements MessageObjectInjector<Group> {
    @Override
    public Class<Group> getType() {
        return Group.class;
    }

    @Override
    public Group getObject(MessageEvent messageEvent, MessageChain messageChain) {
        if (!(messageEvent instanceof GroupMessageEvent)) {
            return null;
        }
        return new Group(((GroupMessageEvent) messageEvent).getGroupId());
    }
}
