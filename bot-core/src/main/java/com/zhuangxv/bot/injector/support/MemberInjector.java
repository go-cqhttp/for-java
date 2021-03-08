package com.zhuangxv.bot.injector.support;

import com.zhuangxv.bot.contact.support.Member;
import com.zhuangxv.bot.event.message.GroupMessageEvent;
import com.zhuangxv.bot.event.message.MessageEvent;
import com.zhuangxv.bot.injector.MessageObjectInjector;
import com.zhuangxv.bot.message.MessageChain;

public class MemberInjector implements MessageObjectInjector<Member> {
    @Override
    public Class<Member> getType() {
        return Member.class;
    }

    @Override
    public Member getObject(MessageEvent messageEvent, MessageChain messageChain) {
        if (!(messageEvent instanceof GroupMessageEvent)) {
            return null;
        }
        return new Member(messageEvent.getUserId());
    }
}
