package com.zhuangxv.bot.injector.support.group;

import com.zhuangxv.bot.core.Member;
import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.event.BaseEvent;
import com.zhuangxv.bot.event.message.GroupMessageEvent;
import com.zhuangxv.bot.event.message.MemberAddEvent;
import com.zhuangxv.bot.injector.ObjectInjector;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiaoxu
 * @since 2022-05-24 10:19
 */
@Slf4j
public class MemberInjector implements ObjectInjector<Member> {
    @Override
    public Class<Member> getClassType() {
        return Member.class;
    }

    @Override
    public String[] getType() {
        return new String[]{"message", "memberAddMessage"};
    }

    @Override
    public Member getObject(BaseEvent event, Bot bot) {
        try {
            if (event instanceof GroupMessageEvent) {
                return bot.getMember(((GroupMessageEvent) event).getGroupId(), ((GroupMessageEvent) event).getUserId());
            }
            if (event instanceof MemberAddEvent) {
                return bot.getMember(((MemberAddEvent) event).getGroupId(), ((MemberAddEvent) event).getUserId());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
