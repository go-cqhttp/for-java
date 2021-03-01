package com.zhuangxv.bot;

import com.zhuangxv.bot.support.BotApplicationRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author xiaoxu
 * @date 2020-08-07 10:43
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(BotApplicationRegistrar.class)
public @interface EnableBot {
}
