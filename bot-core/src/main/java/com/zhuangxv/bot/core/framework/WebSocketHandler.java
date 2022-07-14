package com.zhuangxv.bot.core.framework;

import com.zhuangxv.bot.config.BotConfig;
import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.core.BotDispatcher;
import com.zhuangxv.bot.core.BotFactory;
import com.zhuangxv.bot.core.Group;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Sharable
@RequiredArgsConstructor
public class WebSocketHandler extends SimpleChannelInboundHandler<Object> {

    private final BotConfig botConfig;
    private final BotDispatcher botDispatcher;
    private final Bot bot;

    private WebSocketClientHandshaker webSocketClientHandshaker;

    public WebSocketClientHandshaker getWebSocketClientHandshaker() {
        return webSocketClientHandshaker;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws URISyntaxException {
        HttpHeaders httpHeaders = new DefaultHttpHeaders();
        if (this.botConfig.getAccessToken() != null && this.botConfig.getAccessToken().trim().length() > 0) {
            httpHeaders.add("Authorization", "Bearer " + this.botConfig.getAccessToken());
        }
        this.webSocketClientHandshaker = WebSocketClientHandshakerFactory
                .newHandshaker(new URI(this.botConfig.getWebsocketUrl()), WebSocketVersion.V13, null, false, httpHeaders, 1024 * 1024 * 100);
        ;
        this.webSocketClientHandshaker.handshake(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        this.bot.getBotClient().connection();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        Channel ch = ctx.channel();
        if (!this.webSocketClientHandshaker.isHandshakeComplete()) {
            try {
                this.webSocketClientHandshaker.finishHandshake(ch, (FullHttpResponse) msg);
                log.info(String.format("[%s]Go-cqhttp connected!", this.botConfig.getBotName()));
                new Thread(() -> {
                    try {
                        bot.flushFriends();
                        for (Group group : bot.flushGroups()) {
                            bot.flushGroupMembers(group);
                        }
                        bot.completableFuture.complete(1L);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        Runtime.getRuntime().exit(0);
                    }
                }).start();
            } catch (WebSocketHandshakeException e) {
                log.error(String.format("[%s]Go-cqhttp failed to connect, Token authentication failed!", this.botConfig.getBotName()));
                BotFactory.getApplicationContext().close();
                Runtime.getRuntime().exit(0);
            }
            return;
        }
        if (msg instanceof WebSocketFrame) {
            WebSocketFrame frame = (WebSocketFrame) msg;
            if (frame instanceof TextWebSocketFrame) {
                TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
                this.botDispatcher.handle(textFrame.text(), this.bot);
            } else if (frame instanceof CloseWebSocketFrame) {
                ch.close();
            }
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                ctx.writeAndFlush(new PingWebSocketFrame());
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(cause.getMessage(), cause);
        ctx.close();
    }
}
