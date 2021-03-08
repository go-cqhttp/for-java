package com.zhuangxv.bot.injector.support;

import com.zhuangxv.bot.contact.support.TempFriend;
import com.zhuangxv.bot.event.message.MessageEvent;
import com.zhuangxv.bot.event.message.PrivateMessageEvent;
import com.zhuangxv.bot.injector.MessageObjectInjector;
import com.zhuangxv.bot.message.MessageChain;

public class TempFriendInjector implements MessageObjectInjector<TempFriend> {
    @Override
    public Class<TempFriend> getType() {
        return TempFriend.class;
    }

    @Override
    public TempFriend getObject(MessageEvent messageEvent, MessageChain messageChain) {
        if (!(messageEvent instanceof PrivateMessageEvent)) {
            return null;
        }
        if (!("group".equals(((PrivateMessageEvent) messageEvent).getSubType()))) {
            return null;
        }
        return new TempFriend(messageEvent.getUserId());
    }
}
