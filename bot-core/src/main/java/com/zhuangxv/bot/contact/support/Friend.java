package com.zhuangxv.bot.contact.support;

import com.zhuangxv.bot.contact.Contact;
import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.message.MessageChain;

/**
 * @author xiaoxu
 * @since 2021/5/27 11:36
 */
public class Friend implements Contact {

    private final long userId;
    private final Bot bot;

    private String nickname;

    private String remark;

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public long getUserId() {
        return userId;
    }

    public Friend(long userId, Bot bot) {
        this.userId = userId;
        this.bot = bot;
    }

    @Override
    public int sendMessage(MessageChain messageChain) {
        return this.bot.sendPrivateMessage(this.userId, messageChain);
    }
}
