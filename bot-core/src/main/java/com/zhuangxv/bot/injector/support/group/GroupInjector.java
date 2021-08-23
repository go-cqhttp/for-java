package com.zhuangxv.bot.injector.support.group;

import com.zhuangxv.bot.contact.support.Group;
import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.event.BaseEvent;
import com.zhuangxv.bot.event.message.GroupMessageEvent;
import com.zhuangxv.bot.event.message.GroupRecallEvent;
import com.zhuangxv.bot.event.message.MessageEvent;
import com.zhuangxv.bot.injector.ObjectInjector;
import com.zhuangxv.bot.message.MessageChain;

public class GroupInjector implements ObjectInjector<Group> {
    @Override
    public Class<Group> getClassType() {
        return Group.class;
    }

    @Override
    public String[] getType() {
        return new String[]{"message", "recallMessage"};
    }

    @Override
    public Group getObject(BaseEvent event, Bot bot) {
        if (event instanceof GroupMessageEvent) {
            return new Group(((GroupMessageEvent) event).getGroupId(), bot);
        }
        if (event instanceof GroupRecallEvent) {
            return new Group(((GroupRecallEvent) event).getGroupId(), bot);
        }
        return null;
    }
}
