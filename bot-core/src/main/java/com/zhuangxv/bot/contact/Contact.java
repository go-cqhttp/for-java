package com.zhuangxv.bot.contact;

import com.zhuangxv.bot.message.Message;
import com.zhuangxv.bot.message.MessageChain;

public interface Contact {

    default int sendMessage(Message message) {
        MessageChain messageChain = new MessageChain();
        messageChain.add(message);
        return this.sendMessage(messageChain);
    }

    int sendMessage(MessageChain messageChain);

}
