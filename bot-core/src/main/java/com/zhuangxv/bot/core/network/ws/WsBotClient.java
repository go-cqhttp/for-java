package com.zhuangxv.bot.core.network.ws;

import com.zhuangxv.bot.api.ApiResult;
import com.zhuangxv.bot.api.BaseApi;
import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.core.network.BotClient;
import com.zhuangxv.bot.exception.BotException;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xiaoxu
 * @since 2021/5/27 14:19
 */
@Slf4j
public class WsBotClient implements BotClient {

    private final Lock lock = new ReentrantLock();
    private static final Map<String, CompletableFuture<ApiResult>> completableFutureMap = new ConcurrentHashMap<>();
    private final Channel channel;

    private long lastInvokeTime;

    public WsBotClient(Channel channel) {
        this.channel = channel;
    }

    public static Map<String, CompletableFuture<ApiResult>> getCompletableFutureMap() {
        return completableFutureMap;
    }

    public Channel getChannel() {
        return channel;
    }

    @Override
    public void heartbeat() {
        this.channel.writeAndFlush(new PingWebSocketFrame());
    }

    @Override
    public ApiResult invokeApi(BaseApi baseApi, Bot bot) {
        this.lock.lock();
        try {
            if (baseApi.needSleep() && System.currentTimeMillis() - lastInvokeTime < 1500) {
                try {
                    Thread.sleep(1500 - (System.currentTimeMillis() - lastInvokeTime));
                } catch (InterruptedException e) {
                    throw new BotException(String.format("[%s]调用api出错: %s", bot.getBotName(), e.getMessage()));
                }
                channel.writeAndFlush(new TextWebSocketFrame(baseApi.buildJson()));
            } else {
                channel.writeAndFlush(new TextWebSocketFrame(baseApi.buildJson()));
            }
            CompletableFuture<ApiResult> completableFuture = new CompletableFuture<>();
            completableFutureMap.put(baseApi.getEcho(), completableFuture);
            ApiResult apiResult = getApiResult(baseApi.getEcho());
            lastInvokeTime = System.currentTimeMillis();
            if (apiResult == null || !"ok".equals(apiResult.getStatus())) {
                throw new BotException(String.format("[%s]调用api出错: %s", bot.getBotName(), apiResult));
            }
            return apiResult;
        } finally {
            this.lock.unlock();
        }
    }

    private ApiResult getApiResult(String echo) {
        CompletableFuture<ApiResult> completableFuture = completableFutureMap.get(echo);
        if (completableFuture == null) {
            return null;
        }
        try {
            ApiResult apiResult = completableFuture.get(1, TimeUnit.MINUTES);
            completableFutureMap.remove(echo);
            return apiResult;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

}
