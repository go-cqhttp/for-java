package com.zhuangxv.bot.injector.support.friend;

import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.core.Friend;
import com.zhuangxv.bot.core.TempFriend;
import com.zhuangxv.bot.event.BaseEvent;
import com.zhuangxv.bot.event.message.PrivateMessageEvent;
import com.zhuangxv.bot.injector.ObjectInjector;

/**
 * @author xiaoxu
 * @since 2022-05-24 10:19
 */
public class FriendInjector implements ObjectInjector<Friend> {
    @Override
    public Class<Friend> getClassType() {
        return Friend.class;
    }

    @Override
    public String[] getType() {
        return new String[]{"message"};
    }

    @Override
    public Friend getObject(BaseEvent event, Bot bot) {
        if (event instanceof PrivateMessageEvent) {
            PrivateMessageEvent privateMessageEvent = (PrivateMessageEvent) event;
            return bot.getFriend(privateMessageEvent.getUserId());
        }
        return null;
    }
}
