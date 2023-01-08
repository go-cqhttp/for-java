package com.zhuangxv.bot.core;

/**
 * @author xiaoxu
 * @since 2022/5/31 11:18
 */
public interface BotContext {

    boolean approve(Long qq, String token);

    void connected(Bot bot);

}
