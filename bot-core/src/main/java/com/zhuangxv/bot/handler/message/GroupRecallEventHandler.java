package com.zhuangxv.bot.handler.message;

import com.alibaba.fastjson.JSONObject;
import com.zhuangxv.bot.annotation.GroupMessageHandler;
import com.zhuangxv.bot.annotation.GroupRecallHandler;
import com.zhuangxv.bot.contact.support.Group;
import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.core.BotFactory;
import com.zhuangxv.bot.core.HandlerMethod;
import com.zhuangxv.bot.event.message.GroupMessageEvent;
import com.zhuangxv.bot.event.message.GroupRecallEvent;
import com.zhuangxv.bot.handler.EventHandler;
import com.zhuangxv.bot.message.Message;
import com.zhuangxv.bot.message.MessageChain;
import com.zhuangxv.bot.message.MessageTypeHandle;
import com.zhuangxv.bot.util.ArrayUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GroupRecallEventHandler implements EventHandler {

    @Override
    public void handle(JSONObject jsonObject, Bot bot) {
        if (!GroupRecallEvent.isSupport(jsonObject)) {
            return;
        }
        GroupRecallEvent groupRecallEvent = jsonObject.toJavaObject(GroupRecallEvent.class);
        log.debug(groupRecallEvent.toString());
        List<Object> resultList = BotFactory.handleMethod(bot, groupRecallEvent, handlerMethod -> {
            if (!handlerMethod.getMethod().isAnnotationPresent(GroupRecallHandler.class)) {
                return false;
            }
            GroupRecallHandler groupRecallHandler = handlerMethod.getMethod().getAnnotation(GroupRecallHandler.class);
            if (groupRecallHandler.groupIds().length > 0 && !ArrayUtils.contain(groupRecallHandler.groupIds(), groupRecallEvent.getGroupId())) {
                return false;
            }
            if (groupRecallHandler.senderIds().length > 0 && !ArrayUtils.contain(groupRecallHandler.senderIds(), groupRecallEvent.getOperatorId())) {
                return false;
            }
            return true;
        }, "recallMessage");
        for (Object result : resultList) {
            if (result instanceof Message) {
                new Group(groupRecallEvent.getGroupId(), bot).sendMessage((Message) result);
            }
            if (result instanceof MessageChain) {
                new Group(groupRecallEvent.getGroupId(), bot).sendMessage((MessageChain) result);
            }
        }
    }

}
