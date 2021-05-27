package com.zhuangxv.bot.core;

import com.zhuangxv.bot.config.BotConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

/**
 * @author xiaoxu
 * @date 2020-08-07 11:12
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BotInit implements CommandLineRunner {

    @Override
    public void run(String... args) {
        BotFactory.initHandlerMethod();
        BotFactory.initBot();
    }
}
