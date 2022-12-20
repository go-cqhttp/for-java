package com.zhuangxv.bot.core.network.ws;

import com.zhuangxv.bot.config.BotConfig;
import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.core.BotContext;
import com.zhuangxv.bot.core.Group;
import com.zhuangxv.bot.core.component.BotDispatcher;
import com.zhuangxv.bot.core.component.BotFactory;
import com.zhuangxv.bot.core.network.BotClient;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * @author xiaoxu
 * @since 2022-05-24 10:19
 */
@Slf4j
@Sharable
@RequiredArgsConstructor
public class WsHandler extends SimpleChannelInboundHandler<Object> {

    private final BotConfig botConfig;
    private final BotDispatcher botDispatcher;
    private final WsNetwork wsNetwork;
    private final Map<Long, Bot> bots;

    private boolean shutdown = false;

    private WebSocketClientHandshaker webSocketClientHandshaker;

    public WebSocketClientHandshaker getWebSocketClientHandshaker() {
        return webSocketClientHandshaker;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws URISyntaxException {
        HttpHeaders httpHeaders = new DefaultHttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + this.botConfig.getAccessToken());

        this.webSocketClientHandshaker = WebSocketClientHandshakerFactory
                .newHandshaker(new URI(this.botConfig.getUrl()), WebSocketVersion.V13, null, false, httpHeaders, 1024 * 1024 * 100);
        ;
        this.webSocketClientHandshaker.handshake(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        for (Map.Entry<Long, Bot> botEntry : bots.entrySet()) {
            BotClient botClient = botEntry.getValue().getBotClient();
            if (!(botClient instanceof WsBotClient)) {
                continue;
            }
            if (((WsBotClient) botClient).getChannel().id().asLongText().equals(ctx.channel().id().asLongText())) {
                bots.remove(botEntry.getKey());
            }
        }
        if (shutdown) {
            return;
        }
        this.wsNetwork.connection();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        Channel ch = ctx.channel();
        if (!this.webSocketClientHandshaker.isHandshakeComplete() && msg instanceof FullHttpResponse) {
            try {
                this.webSocketClientHandshaker.finishHandshake(ch, (FullHttpResponse) msg);
                BotClient botClient = new WsBotClient(this.wsNetwork.getChannel());
                Bot bot = new Bot(this.botConfig, botClient);
                new Thread(() -> {
                    try {
                        bot.flushBotInfo();
                        log.info(String.format("[%s]Go-cqhttp connected!", bot.getBotName()));
                        if (bot.getBotId() == 0 || StringUtils.isEmpty(bot.getBotName())) {
                            log.error("ws正向连接失败.");
                            shutdown = true;
                            ctx.close();
                            return;
                        }
                        Map<String, BotContext> beans = BotFactory.getBeansByClass(BotContext.class);
                        if (beans != null) {
                            for (BotContext botContext : beans.values()) {
                                botContext.connected(bot);
                            }
                        }
                        bots.put(bot.getBotId(), bot);
                        try {
                            bot.flushFriends();
                            for (Group group : bot.flushGroups()) {
                                bot.flushGroupMembers(group);
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                        bot.getCompletableFuture().complete(1L);
                    } catch (Exception e) {
                        shutdown = true;
                        ctx.close();
                        log.error(e.getMessage(), e);
                    }
                }).start();
            } catch (WebSocketHandshakeException e) {
                log.error("Go-cqhttp ws failed to connect, Token authentication failed!");
                BotFactory.getApplicationContext().close();
                Runtime.getRuntime().exit(0);
            }
            return;
        }
        if (msg instanceof WebSocketFrame) {
            WebSocketFrame frame = (WebSocketFrame) msg;
            if (frame instanceof TextWebSocketFrame) {
                TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
                this.botDispatcher.handle(textFrame.text());
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
