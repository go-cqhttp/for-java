package com.zhuangxv.bot.support;

import com.zhuangxv.bot.config.BotConfig;
import com.zhuangxv.bot.config.BotConfigFactory;
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

    private ConfigurableEnvironment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        BeanDefinitionBuilder configFactory = BeanDefinitionBuilder.rootBeanDefinition(BotConfigFactory.class);
        BotConfig botConfig = new BotConfig();
        Binder binder = Binder.get(this.environment);
        botConfig = binder.bind(botConfig.getConfigKey(), Bindable.of(BotConfig.class)).get();
        configFactory.addPropertyValue("botConfig", botConfig);
        BeanRegistryUtils.registerBeanDefinition(beanDefinitionRegistry, configFactory.getBeanDefinition(), "botConfigFactory");
        BeanDefinitionBuilder serverConfig = BeanDefinitionBuilder.rootBeanDefinition(BotConfig.class);
        serverConfig.setFactoryMethodOnBean("getBotConfig", "botConfigFactory");
        BeanRegistryUtils.registerBeanDefinition(beanDefinitionRegistry, serverConfig.getBeanDefinition(), "botConfig");
    }

    @Override
    public void setEnvironment(Environment environment) {
        Assert.isInstanceOf(ConfigurableEnvironment.class, environment);
        this.environment = (ConfigurableEnvironment) environment;
    }
}