package com.zhuangxv.bot.core.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhuangxv.bot.api.ApiResult;
import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.core.network.ws.WsBotClient;
import com.zhuangxv.bot.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xiaoxu
 * @since 2022-05-24 10:19
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BotDispatcher {

    private final Map<String, EventHandler> eventHandlerMap;

    private ExecutorService executorService;

    @PostConstruct
    public void init() {
        this.executorService = Executors.newFixedThreadPool(4);
    }

    public void handle(String message) {
        try {
            JSONObject jsonObject = JSON.parseObject(message);
            if (jsonObject.containsKey("echo") && jsonObject.containsKey("status") && jsonObject.containsKey("retcode") && jsonObject.containsKey("data")) {
                try {
                    ApiResult apiResult = JSON.parseObject(message, ApiResult.class);
                    CompletableFuture<ApiResult> completableFuture = WsBotClient.getCompletableFutureMap().get(apiResult.getEcho());
                    if (completableFuture != null) {
                        completableFuture.complete(apiResult);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                return;
            }
            Bot bot = BotFactory.getBots().get(jsonObject.getLong("self_id"));
            if (bot == null) {
                return;
            }
            this.executorService.submit(() -> {
                try {
                    for (EventHandler eventHandler : eventHandlerMap.values()) {
                        eventHandler.handle(jsonObject, bot);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
