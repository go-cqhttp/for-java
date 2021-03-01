package com.zhuangxv.bot.injector.support;

import com.zhuangxv.bot.contact.support.Group;
import com.zhuangxv.bot.contact.support.TempFriend;
import com.zhuangxv.bot.event.BaseEvent;
import com.zhuangxv.bot.event.message.GroupMessageEvent;
import com.zhuangxv.bot.event.message.PrivateMessageEvent;
import com.zhuangxv.bot.injector.ObjectInjector;
import com.zhuangxv.bot.message.MessageChain;

public class TempFriendInjector implements ObjectInjector<TempFriend> {
    @Override
    public Class<TempFriend> getType() {
        return TempFriend.class;
    }

    @Override
    public TempFriend getObject(BaseEvent baseEvent, MessageChain messageChain) {
        if (!(baseEvent instanceof PrivateMessageEvent)) {
            return null;
        }
        if (!("group".equals(((PrivateMessageEvent) baseEvent).getSubType()))) {
            return null;
        }
        return new TempFriend(((PrivateMessageEvent) baseEvent).getUserId());
    }
}
