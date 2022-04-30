package com.zhuangxv.bot.handler.message;

import com.alibaba.fastjson.JSONObject;
import com.zhuangxv.bot.annotation.GroupUserAddHandler;
import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.core.BotFactory;
import com.zhuangxv.bot.event.message.GroupUserAddEvent;
import com.zhuangxv.bot.handler.EventHandler;
import com.zhuangxv.bot.message.Message;
import com.zhuangxv.bot.message.MessageChain;
import com.zhuangxv.bot.util.ArrayUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GroupUserAddEventHandler implements EventHandler {

    @Override
    public void handle(JSONObject jsonObject, Bot bot) {
        if (!GroupUserAddEvent.isSupport(jsonObject)) {
            return;
        }
        GroupUserAddEvent groupUserAddEvent = jsonObject.toJavaObject(GroupUserAddEvent.class);
        log.debug(groupUserAddEvent.toString());
        List<Object> resultList = BotFactory.handleMethod(bot, groupUserAddEvent, handlerMethod -> {
            if (!handlerMethod.getMethod().isAnnotationPresent(GroupUserAddHandler.class)) {
                return false;
            }
            GroupUserAddHandler groupUserAddHandler = handlerMethod.getMethod().getAnnotation(GroupUserAddHandler.class);
            if (groupUserAddHandler.groupIds().length > 0 && !ArrayUtils.contain(groupUserAddHandler.groupIds(), groupUserAddEvent.getGroupId())) {
                return false;
            }
            if (ArrayUtils.contain(groupUserAddHandler.excludeGroupIds(), groupUserAddEvent.getGroupId())) {
                return false;
            }
            if (groupUserAddHandler.senderIds().length > 0 && !ArrayUtils.contain(groupUserAddHandler.senderIds(), groupUserAddEvent.getOperatorId())) {
                return false;
            }
            if (ArrayUtils.contain(groupUserAddHandler.excludeSenderIds(), groupUserAddEvent.getOperatorId())) {
                return false;
            }
            return true;
        }, "userAddMessage");
        for (Object result : resultList) {
            try {
                if (result instanceof Message) {
                    bot.getGroup(groupUserAddEvent.getGroupId()).sendMessage((Message) result);
                }
                if (result instanceof MessageChain) {
                    bot.getGroup(groupUserAddEvent.getGroupId()).sendMessage((MessageChain) result);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

}
