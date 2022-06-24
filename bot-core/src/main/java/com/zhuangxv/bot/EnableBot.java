package com.zhuangxv.bot;

import com.zhuangxv.bot.support.BotApplicationRegistrar;
import com.zhuangxv.bot.support.BotAutoConfigRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author xiaoxu
 * @since 2020-08-07 10:43
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({BotApplicationRegistrar.class, BotAutoConfigRegistrar.class})
public @interface EnableBot {
}
