package com.zhuangxv.bot.contact.support;

import com.alibaba.fastjson.JSONObject;
import com.zhuangxv.bot.contact.Contact;
import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.message.MessageChain;

public class Member implements Contact {

    private final long userId;
    private final long groupId;
    private final Bot bot;

    public Member(long userId, long groupId, Bot bot) {
        this.userId = userId;
        this.groupId = groupId;
        this.bot = bot;
    }

    public long getUserId() {
        return this.userId;
    }

    public String getCardName() {
        JSONObject memberInfo = this.bot.getMemberInfo(this.groupId, this.userId);
        String result = memberInfo.getString("card");
        if (result == null || result.equals("")) {
            result = memberInfo.getString("nickname");
        }
        return result;
    }

    public void ban(long duration) {
        this.bot.memberBan(this.groupId, this.userId, duration);
    }

    public void pardon() {
        this.bot.memberPardon(this.groupId, this.userId);
    }

    @Override
    public int sendMessage(MessageChain messageChain) {
        if (this.bot.isFriend(this.userId)) {
            return this.bot.sendPrivateMessage(this.userId, messageChain);
        } else {
            return this.bot.sendTempMessage(this.userId, this.groupId, messageChain);
        }
    }

}
