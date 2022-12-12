package com.zhuangxv.bot.core.network;

import com.zhuangxv.bot.api.ApiResult;
import com.zhuangxv.bot.api.BaseApi;
import com.zhuangxv.bot.core.Bot;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author xiaoxu
 * @since 2022/5/19 10:59
 */
public interface BotClient {

    ApiResult invokeApi(BaseApi baseApi, Bot bot);

    void heartbeat();

}
