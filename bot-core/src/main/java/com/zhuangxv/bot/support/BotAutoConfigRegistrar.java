package com.zhuangxv.bot.support;

import com.zhuangxv.bot.config.BotConfig;
import com.zhuangxv.bot.config.BotConfigFactory;
import com.zhuangxv.bot.core.BotFactory;
import com.zhuangxv.bot.util.BeanRegistryUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

/**
 * @author xiaoxu
 * @date 2020-06-22 11:10
 */
@Slf4j
public class BotAutoConfigRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {


    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {

    }

    @Override
    public void setEnvironment(Environment environment) {
        Assert.isInstanceOf(ConfigurableEnvironment.class, environment);
        BotFactory.setEnvironment((ConfigurableEnvironment) environment);
    }
}