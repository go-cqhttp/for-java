package com.zhuangxv.bot.core;

import lombok.Data;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

@Data
public class ResultCondition {

    private Lock lock;

    private Condition condition;

}
