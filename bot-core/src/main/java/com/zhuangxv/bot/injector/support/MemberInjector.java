package com.zhuangxv.bot.injector.support;

import com.zhuangxv.bot.contact.support.Member;
import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.event.BaseEvent;
import com.zhuangxv.bot.event.message.GroupMessageEvent;
import com.zhuangxv.bot.event.message.MessageEvent;
import com.zhuangxv.bot.injector.ObjectInjector;
import com.zhuangxv.bot.message.MessageChain;

public class MemberInjector implements ObjectInjector<Member> {
    @Override
    public Class<Member> getClassType() {
        return Member.class;
    }

    @Override
    public String[] getType() {
        return new String[]{"message"};
    }

    @Override
    public Member getObject(BaseEvent event, Bot bot) {
        if (event instanceof GroupMessageEvent) {
            return new Member(((GroupMessageEvent) event).getUserId(), ((GroupMessageEvent) event).getGroupId(), bot);
        }
        return null;
    }
}
