package com.zhuangxv.bot.support;

import com.zhuangxv.bot.core.*;
import com.zhuangxv.bot.handler.message.GroupMessageEventHandler;
import com.zhuangxv.bot.handler.message.GroupRecallEventHandler;
import com.zhuangxv.bot.handler.message.PrivateMessageEventHandler;
import com.zhuangxv.bot.handler.meta.HeartbeatEventHandler;
import com.zhuangxv.bot.injector.support.*;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author zhuan
 */
public class BotApplicationRegistrar implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{
                BotFactory.class.getName(),
                BotDispatcher.class.getName(),
                SnowFlakeIdGenerator.class.getName(),
                HeartbeatEventHandler.class.getName(),
                PrivateMessageEventHandler.class.getName(),
                GroupMessageEventHandler.class.getName(),
                GroupRecallEventHandler.class.getName(),
                RecallMessageInjector.class.getName(),
                BotInit.class.getName(),
                MessageStringInjector.class.getName(),
                GroupInjector.class.getName(),
                MessageChainInjector.class.getName(),
                TempFriendInjector.class.getName(),
                MemberInjector.class.getName(),
                GroupMessageEventInjector.class.getName(),
                MessageIdInjector.class.getName(),
                MessageIdIntInjector.class.getName(),
                BotInjector.class.getName(),
        };
    }

}
