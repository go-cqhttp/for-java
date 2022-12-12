package com.zhuangxv.bot.handler.message;

import com.alibaba.fastjson.JSONObject;
import com.zhuangxv.bot.annotation.FriendMessageHandler;
import com.zhuangxv.bot.annotation.TempMessageHandler;
import com.zhuangxv.bot.core.TempFriend;
import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.core.component.BotFactory;
import com.zhuangxv.bot.event.message.PrivateMessageEvent;
import com.zhuangxv.bot.handler.EventHandler;
import com.zhuangxv.bot.message.CacheMessage;
import com.zhuangxv.bot.message.Message;
import com.zhuangxv.bot.message.MessageChain;
import com.zhuangxv.bot.message.MessageTypeHandle;
import com.zhuangxv.bot.util.ArrayUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author xiaoxu
 * @since 2022-05-24 10:19
 */
@Slf4j
public class PrivateMessageEventHandler implements EventHandler {
    @Override
    public void handle(JSONObject jsonObject, Bot bot) {
        if (!PrivateMessageEvent.isSupport(jsonObject)) {
            return;
        }
        PrivateMessageEvent privateMessageEvent = jsonObject.toJavaObject(PrivateMessageEvent.class);
        MessageChain messageChain = new MessageChain();
        for (int i = 0; i < privateMessageEvent.getMessage().size(); i++) {
            messageChain.add(MessageTypeHandle.getMessage(privateMessageEvent.getMessage().getJSONObject(i)));
        }
        log.debug(messageChain.toMessageString());
        CacheMessage cacheMessage = new CacheMessage();
        cacheMessage.setSenderId(privateMessageEvent.getUserId());
        cacheMessage.setMessageChain(messageChain);
        bot.pushUserCacheMessageChain(privateMessageEvent.getUserId(), privateMessageEvent.getMessageId(), cacheMessage);
        List<Object> resultList;
        if ("group".equals(privateMessageEvent.getSubType())) {
            resultList = BotFactory.handleMethod(bot, privateMessageEvent, handlerMethod -> {
                if (!handlerMethod.getMethod().isAnnotationPresent(TempMessageHandler.class)) {
                    return false;
                }
                TempMessageHandler tempMessageHandler = handlerMethod.getMethod().getAnnotation(TempMessageHandler.class);
                if (tempMessageHandler.bot() != 0 && tempMessageHandler.bot() != privateMessageEvent.getSelfId()) {
                    return false;
                }
                if (tempMessageHandler.senderIds().length > 0 && !ArrayUtils.contain(tempMessageHandler.senderIds(), privateMessageEvent.getUserId())) {
                    return false;
                }
                if (ArrayUtils.contain(tempMessageHandler.excludeSenderIds(), privateMessageEvent.getUserId())) {
                    return false;
                }
                return tempMessageHandler.regex().equals("none") || messageChain.toString().matches(tempMessageHandler.regex());
            }, "message");
        } else if ("friend".equals(privateMessageEvent.getSubType())) {
            resultList = BotFactory.handleMethod(bot, privateMessageEvent, handlerMethod -> {
                if (!handlerMethod.getMethod().isAnnotationPresent(FriendMessageHandler.class)) {
                    return false;
                }
                FriendMessageHandler friendMessageHandler = handlerMethod.getMethod().getAnnotation(FriendMessageHandler.class);
                if (friendMessageHandler.bot() != 0 && friendMessageHandler.bot() != privateMessageEvent.getSelfId()) {
                    return false;
                }
                if (friendMessageHandler.senderIds().length > 0 && !ArrayUtils.contain(friendMessageHandler.senderIds(), privateMessageEvent.getUserId())) {
                    return false;
                }
                if (ArrayUtils.contain(friendMessageHandler.excludeSenderIds(), privateMessageEvent.getUserId())) {
                    return false;
                }
                return friendMessageHandler.regex().equals("none") || messageChain.toString().matches(friendMessageHandler.regex());
            }, "message");
        } else {
            return;
        }
        for (Object result : resultList) {
            if (result instanceof Message) {
                new TempFriend(privateMessageEvent.getUserId(), bot).sendMessage((Message) result);
            }
            if (result instanceof MessageChain) {
                new TempFriend(privateMessageEvent.getUserId(), bot).sendMessage((MessageChain) result);
            }
        }
    }
}
