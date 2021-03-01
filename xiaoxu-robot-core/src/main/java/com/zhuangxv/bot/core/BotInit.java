package com.zhuangxv.bot.core;

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

    private final BotApplication botApplication;

    @Override
    public void run(String... args) {
        BotApplication.initHandlerMethod();
        this.botApplication.connection();
    }
}
