package com.zhuangxv.bot.contact.support;

import com.zhuangxv.bot.contact.Contact;
import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.message.MessageChain;

public class TempFriend implements Contact {

    private final long userId;
    private final Bot bot;

    public TempFriend(long userId, Bot bot) {
        this.userId = userId;
        this.bot = bot;
    }

    public long getUserId() {
        return this.userId;
    }

    @Override
    public int sendMessage(MessageChain messageChain) {
        return this.bot.sendPrivateMessage(this.userId, messageChain);
    }

}
