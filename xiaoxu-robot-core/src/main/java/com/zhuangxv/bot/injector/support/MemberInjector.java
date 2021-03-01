package com.zhuangxv.bot.injector.support;

import com.zhuangxv.bot.contact.support.Member;
import com.zhuangxv.bot.contact.support.TempFriend;
import com.zhuangxv.bot.event.BaseEvent;
import com.zhuangxv.bot.event.message.GroupMessageEvent;
import com.zhuangxv.bot.event.message.PrivateMessageEvent;
import com.zhuangxv.bot.injector.ObjectInjector;
import com.zhuangxv.bot.message.MessageChain;

public class MemberInjector implements ObjectInjector<Member> {
    @Override
    public Class<Member> getType() {
        return Member.class;
    }

    @Override
    public Member getObject(BaseEvent baseEvent, MessageChain messageChain) {
        if (!(baseEvent instanceof GroupMessageEvent)) {
            return null;
        }
        return new Member(((GroupMessageEvent) baseEvent).getUserId());
    }
}
