package com.zhuangxv.bot.injector.support.group;

import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.event.BaseEvent;
import com.zhuangxv.bot.event.message.GroupUserAddEvent;
import com.zhuangxv.bot.injector.ObjectInjector;
import com.zhuangxv.bot.injector.object.UserAddMessage;

public class UserAddMessageInjector implements ObjectInjector<UserAddMessage> {
    @Override
    public Class<UserAddMessage> getClassType() {
        return UserAddMessage.class;
    }

    @Override
    public String[] getType() {
        return new String[]{"userAddMessage"};
    }

    @Override
    public UserAddMessage getObject(BaseEvent event, Bot bot) {
        if (event instanceof GroupUserAddEvent) {
            GroupUserAddEvent groupUserAddEvent = (GroupUserAddEvent) event;
            UserAddMessage userAddMessage = new UserAddMessage();
            userAddMessage.setUserId(groupUserAddEvent.getUserId());
            userAddMessage.setOperatorId(groupUserAddEvent.getOperatorId());
            return userAddMessage;
        }
        return null;
    }
}
