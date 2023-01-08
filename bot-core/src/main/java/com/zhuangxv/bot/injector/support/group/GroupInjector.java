package com.zhuangxv.bot.injector.support.group;

import com.zhuangxv.bot.annotation.MemberAddHandler;
import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.core.Group;
import com.zhuangxv.bot.event.BaseEvent;
import com.zhuangxv.bot.event.message.GroupMessageEvent;
import com.zhuangxv.bot.event.message.GroupRecallEvent;
import com.zhuangxv.bot.event.message.MemberAddEvent;
import com.zhuangxv.bot.injector.ObjectInjector;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiaoxu
 * @since 2022-05-24 10:19
 */
@Slf4j
public class GroupInjector implements ObjectInjector<Group> {
    @Override
    public Class<Group> getClassType() {
        return Group.class;
    }

    @Override
    public String[] getType() {
        return new String[]{"message", "recallMessage", "memberAddMessage"};
    }

    @Override
    public Group getObject(BaseEvent event, Bot bot) {
        try {
            if (event instanceof GroupMessageEvent) {
                return bot.getGroup(((GroupMessageEvent) event).getGroupId());
            }
            if (event instanceof GroupRecallEvent) {
                return bot.getGroup(((GroupRecallEvent) event).getGroupId());
            }
            if (event instanceof MemberAddEvent) {
                return bot.getGroup(((MemberAddEvent) event).getGroupId());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
