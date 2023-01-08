package com.zhuangxv.bot.core;

import com.zhuangxv.bot.message.MessageChain;

/**
 * @author xiaoxu
 * @since 2022-05-24 10:19
 */
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
