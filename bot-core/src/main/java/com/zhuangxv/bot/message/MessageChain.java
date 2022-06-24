package com.zhuangxv.bot.message;

import com.alibaba.fastjson.JSON;
import com.zhuangxv.bot.message.support.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author xiaoxu
 * @since 2022-05-24 10:19
 */
public class MessageChain extends ArrayList<Message> {

    @Override
    public String toString() {
        return this.stream().map(Object::toString).collect(Collectors.joining());
    }

    public String toMessageString() {
        return this.stream().map(Message::toMessageString).collect(Collectors.joining(",", "[", "]"));
    }

    public MessageChain at(long qq) {
        this.add(new AtMessage(String.valueOf(qq)));
        return this;
    }

    public MessageChain atAll() {
        this.add(new AtMessage("all"));
        return this;
    }

    public MessageChain text(String message) {
        this.add(new TextMessage(message));
        return this;
    }

    public MessageChain image(String file) {
        this.add(new ImageMessage(file));
        return this;
    }

    public MessageChain reply(int messageId) {
        this.add(new ReplyMessage(messageId));
        return this;
    }

    public MessageChain record(String file) {
        this.add(new RecordMessage(file));
        return this;
    }
    public MessageChain face(String id) {
        this.add(new FaceMessage(id));
        return this;
    }

    public MessageChain copy() {
        MessageChain messageChain = new MessageChain();
        messageChain.addAll(this.stream().map(message -> MessageTypeHandle.getMessage(JSON.parseObject(message.toMessageString()))).collect(Collectors.toList()));
        return messageChain;
    }

}
