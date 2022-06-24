package com.zhuangxv.bot.core.network;

import com.zhuangxv.bot.config.BotConfig;
import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.core.component.BotDispatcher;

import java.util.Map;

/**
 * @author xiaoxu
 * @since 2022/5/19 16:33
 */
public interface BotNetwork {

    void init(BotConfig botConfig, Map<Long, Bot> bots, BotDispatcher botDispatcher);

}
