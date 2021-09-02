package com.zhuangxv.bot.contact.support;

import com.zhuangxv.bot.contact.Contact;
import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.message.MessageChain;

import java.util.List;

public class Group implements Contact {

    private final long groupId;
    private final Bot bot;

    public Group(long groupId, Bot bot) {
        this.groupId = groupId;
        this.bot = bot;
    }

    public long getGroupId() {
        return groupId;
    }

    @Override
    public int sendMessage(MessageChain messageChain) {
        return this.bot.sendGroupMessage(this.groupId, messageChain);
    }

    public int sendTempMessage(long userId, MessageChain messageChain) {
        return this.getMember(userId).sendMessage(messageChain);
    }

    public void groupBan() {
        this.bot.groupBan(this.groupId);
    }

    public void groupPardon() {
        this.bot.groupPardon(this.groupId);
    }

    public Member getMember(long userId) {
        return new Member(userId, this.groupId, this.bot);
    }

    public List<GroupsMember> getGroupsMember() {
        return this.bot.getGroupsMember(this.groupId);
    }

    public void setGroupSpecialTitle(long userId, String specialTitle, Number duration) {
        this.bot.setGroupSpecialTitle(userId, specialTitle, duration, this.groupId);
    }
}
