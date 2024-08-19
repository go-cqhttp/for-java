package com.zhuangxv.bot.core.network;

import com.zhuangxv.bot.message.MessageChain;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author xiaoxu
 * @since 2024/3/22 14:26
 */
public class MessageCache extends LinkedHashMap<String, String> {

    private final int maxSize;

    public MessageCache(int maxSize) {
        super();
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
        return this.size() > this.maxSize;
    }

    public String getMessage(String key) {
        return this.get(key);
    }

}