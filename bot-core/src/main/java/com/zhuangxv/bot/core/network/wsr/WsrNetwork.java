package com.zhuangxv.bot.core.network.wsr;

import com.zhuangxv.bot.config.BotConfig;
import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.core.component.BotDispatcher;
import com.zhuangxv.bot.core.network.BotNetwork;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.Map;

/**
 * @author xiaoxu
 * @since 2022/5/24 15:40
 */
@Slf4j
public class WsrNetwork implements BotNetwork {

    @Override
    public void init(BotConfig botConfig, Map<Long, Bot> bots, BotDispatcher botDispatcher) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline()
                                    .addLast(new HttpServerCodec())
                                    .addLast(new HttpObjectAggregator(1024 * 1024 * 100))
                                    .addLast(new WebSocketFrameAggregator(1024 * 1024 * 100))
                                    .addLast(new WsrHandler(botConfig, botDispatcher, bots));
                        }
                    });
            URI uri = new URI(botConfig.getUrl());
            Channel ch = bootstrap.bind(new InetSocketAddress(uri.getHost(), uri.getPort())).sync().channel();
            log.info("ws反向服务器已开启,端口8080,等待连接.");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
