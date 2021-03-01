package com.zhuangxv.bot.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhuangxv.bot.api.ApiResult;
import com.zhuangxv.bot.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BotDispatcher {

    private final Map<String, EventHandler> eventHandlerMap;

    private ExecutorService executorService;

    @PostConstruct
    public void init() {
        this.executorService = Executors.newFixedThreadPool(4);
    }

    public void handle(String message) {
        JSONObject jsonObject = JSON.parseObject(message);
        if (jsonObject.containsKey("echo") && jsonObject.containsKey("status") && jsonObject.containsKey("retcode") && jsonObject.containsKey("data")) {
            ApiResult apiResult = JSON.parseObject(message, ApiResult.class);
            BotApplication.setApiResult(apiResult.getEcho(), apiResult);
            return;
        }
        this.executorService.submit(() -> {
            try {
                for (EventHandler eventHandler : eventHandlerMap.values()) {
                    eventHandler.handle(jsonObject);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }

}
