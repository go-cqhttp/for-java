package com.zhuangxv.bot.core;

import com.zhuangxv.bot.message.Message;
import com.zhuangxv.bot.message.MessageChain;

/**
 * @author xiaoxu
 * @since 2022-05-24 10:19
 */
public interface Contact {

    default int sendMessage(Message message) {
        MessageChain messageChain = new MessageChain();
        messageChain.add(message);
        return this.sendMessage(messageChain);
    }

    int sendMessage(MessageChain messageChain);

}
