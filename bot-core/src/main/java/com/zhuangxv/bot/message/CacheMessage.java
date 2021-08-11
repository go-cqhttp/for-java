package com.zhuangxv.bot.message;

import lombok.Data;

/**
 * @author xiaoxu
 * @since 2021/8/11 3:41 下午
 */
@Data
public class CacheMessage {

    private Long senderId;

    private MessageChain messageChain;

}
