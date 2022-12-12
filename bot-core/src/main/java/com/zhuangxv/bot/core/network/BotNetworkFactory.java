package com.zhuangxv.bot.core.network;

import com.zhuangxv.bot.config.BotConfig;
import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.core.component.BotDispatcher;
import com.zhuangxv.bot.core.network.ws.WsNetwork;
import com.zhuangxv.bot.core.network.wsr.WsrNetwork;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @author xiaoxu
 * @since 2022/5/19 16:24
 */
@Slf4j
public class BotNetworkFactory {

    public static void initBotNetwork(List<BotConfig> botConfigs, Map<Long, Bot> bots, BotDispatcher botDispatcher) {
        for (BotConfig botConfig : botConfigs) {
            BotNetwork botNetwork;
            if ("ws".equals(botConfig.getType())) {
                botNetwork = new WsNetwork();
            } else if ("ws-reverse".equals(botConfig.getType())) {
                botNetwork = new WsrNetwork();
            } else if ("http".equals(botConfig.getType())) {
                log.warn(String.format("%s is not supported.", botConfig.getType()));
                return;
            } else {
                log.warn(String.format("%s is not supported.", botConfig.getType()));
                return;
            }
            botNetwork.init(botConfig, bots, botDispatcher);
        }
    }

}
