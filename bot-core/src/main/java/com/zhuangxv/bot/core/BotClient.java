package com.zhuangxv.bot.core;

import com.zhuangxv.bot.api.ApiResult;
import com.zhuangxv.bot.api.BaseApi;
import com.zhuangxv.bot.config.BotConfig;
import com.zhuangxv.bot.exception.BotException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.unix.DomainSocketAddress;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xiaoxu
 * @since 2021/5/27 14:19
 */
@Slf4j
public class BotClient {

    private final Bootstrap clientBootstrap = new Bootstrap();
    private final Lock lock = new ReentrantLock();
    protected final Map<String, CompletableFuture<ApiResult>> completableFutureMap = new ConcurrentHashMap<>();

    private final BotConfig botConfig;

    private Channel channel;

    private long lastInvokeTime;

    protected BotClient(BotConfig botConfig, BotDispatcher botDispatcher, Bot bot) {
        this.botConfig = botConfig;
        this.clientBootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline()
                                .addLast(new HttpClientCodec())
                                .addLast(new HttpObjectAggregator(1024 * 8))
                                .addLast(new WebSocketHandler(botConfig, botDispatcher, bot));
                    }
                });
    }

    public Channel getChannel() {
        if (channel == null || !channel.isActive() || !channel.pipeline().get(WebSocketHandler.class).getWebSocketClientHandshaker().isHandshakeComplete()) {
            throw new BotException(String.format("[%s]连接失败", this.botConfig.getBotName()));
        }
        return channel;
    }

    public void connection() {
        if (channel != null && channel.isActive()) {
            return;
        }
        URI wsUri = null;
        try {
            wsUri = new URI(this.botConfig.getWebsocketUrl());
        } catch (URISyntaxException e) {
            throw new BotException("websocket url 格式错误.");
        }
        ChannelFuture channelFuture = clientBootstrap.connect(wsUri.getHost(), wsUri.getPort());
        channelFuture.addListener((ChannelFutureListener) futureListener -> {
            if (futureListener.isSuccess()) {
                channel = futureListener.channel();
            } else {
                log.error(String.format("[%s]Failed to connect to go-cqhttp, try connect after 10s", this.botConfig.getBotName()));
                futureListener.channel().eventLoop().schedule(this::connection, 10, TimeUnit.SECONDS);
            }
        });
    }

    public ApiResult invokeApi(BaseApi baseApi) {
        this.lock.lock();
        try {
            if (baseApi.needSleep() && System.currentTimeMillis() - lastInvokeTime < 3000) {
                try {
                    Thread.sleep(System.currentTimeMillis() - lastInvokeTime);
                } catch (InterruptedException e) {
                    throw new BotException(String.format("[%s]调用api出错: %s", this.botConfig.getBotName(), e.getMessage()));
                }
                channel.writeAndFlush(new TextWebSocketFrame(baseApi.buildJson()));
                lastInvokeTime = System.currentTimeMillis();
            } else {
                channel.writeAndFlush(new TextWebSocketFrame(baseApi.buildJson()));
            }
            CompletableFuture<ApiResult> completableFuture = new CompletableFuture<>();
            completableFutureMap.put(baseApi.getEcho(), completableFuture);
            ApiResult apiResult = getApiResult(baseApi.getEcho());
            if (apiResult == null || !"ok".equals(apiResult.getStatus())) {
                throw new BotException(String.format("[%s]调用api出错: %s", this.botConfig.getBotName(), apiResult));
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
