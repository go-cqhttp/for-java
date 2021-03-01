package com.zhuangxv.bot.core;

import com.zhuangxv.bot.exception.BotException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebSocketClient {

    private final WebSocketHandler webSocketHandler;

    @PostConstruct
    public void init() {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        BotApplication.getClientBootstrap()
                           .group(workerGroup)
                           .channel(NioSocketChannel.class)
                           .option(ChannelOption.SO_KEEPALIVE, true)
                           .handler(new ChannelInitializer<SocketChannel>() {
                               @Override
                               protected void initChannel(SocketChannel socketChannel) {
                                   socketChannel.pipeline()
                                                .addLast(new HttpClientCodec())
                                                .addLast(new HttpObjectAggregator(1024 * 8))
                                                .addLast(webSocketHandler);
                               }
                           });
    }

}
