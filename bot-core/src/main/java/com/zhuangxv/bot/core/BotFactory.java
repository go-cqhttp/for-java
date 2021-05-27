package com.zhuangxv.bot.core;

import com.zhuangxv.bot.annotation.FriendMessageHandler;
import com.zhuangxv.bot.annotation.GroupMessageHandler;
import com.zhuangxv.bot.annotation.TempMessageHandler;
import com.zhuangxv.bot.api.ApiResult;
import com.zhuangxv.bot.api.BaseApi;
import com.zhuangxv.bot.config.BotConfig;
import com.zhuangxv.bot.config.PropertySourcesUtils;
import com.zhuangxv.bot.event.message.MessageEvent;
import com.zhuangxv.bot.exception.BotException;
import com.zhuangxv.bot.injector.MessageObjectInjector;
import com.zhuangxv.bot.message.MessageChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
public class BotFactory implements ApplicationContextAware, DisposableBean {

    private static final List<Bot> bots = new ArrayList<>();

    private static ConfigurableEnvironment environment;

    private static ConfigurableApplicationContext applicationContext;
    private static Map<String, List<HandlerMethod>> handlerMethodMap;
    private static Map<Class<?>, MessageObjectInjector<?>> objectInjectorMap;

    protected static ExecutorService executorService = Executors.newFixedThreadPool(4);
    protected static BlockingQueue<BaseApi> apiBlockingQueue = new LinkedBlockingQueue<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (BotFactory.applicationContext == null && applicationContext instanceof ConfigurableApplicationContext) {
            BotFactory.applicationContext = (ConfigurableApplicationContext) applicationContext;
        }
    }

    public static void setEnvironment(ConfigurableEnvironment environment) {
        BotFactory.environment = environment;
    }

    @Override
    public void destroy() {
        log.info("clear ApplicationContext:" + applicationContext);
        applicationContext = null;
    }

    public static void initHandlerMethod() {
        Map<String, Object> beans = BotFactory.getApplicationContext().getBeansOfType(Object.class);
        handlerMethodMap = new HashMap<>();
        for (Object bean : beans.values()) {
            Class<?> beanClass = bean.getClass();
            Set<Method> methodSet = Arrays.stream(beanClass.getMethods()).filter(method -> method.isAnnotationPresent(GroupMessageHandler.class)
                    || method.isAnnotationPresent(TempMessageHandler.class)
                    || method.isAnnotationPresent(FriendMessageHandler.class)
            ).collect(Collectors.toSet());
            methodSet.forEach(method -> {
                HandlerMethod handlerMethod = new HandlerMethod() {
                    {
                        setType(beanClass);
                        setMethod(method);
                        setObject(bean);
                    }
                };
                // TODO: 2021/2/21 改成多qq
                handlerMethodMap.computeIfAbsent("bot", k -> new ArrayList<>()).add(handlerMethod);
            });
        }
        objectInjectorMap = new HashMap<>();
        Map<String, MessageObjectInjector> objectInjectors = getBeansByClass(MessageObjectInjector.class);
        if (objectInjectors != null) {
            objectInjectors.values().forEach(objectInjector -> objectInjectorMap.put(objectInjector.getType(), objectInjector));
        }
        log.info("初始化事件处理器完成.");
    }

    public static void initBot() {
        String configKey = "bot";
        List<BotConfig> botConfigs;
        if (PropertySourcesUtils.getPrefixedProperties(BotFactory.environment.getPropertySources(), configKey).size() == 0
                && PropertySourcesUtils.getPrefixedProperties(BotFactory.environment.getPropertySources(), configKey + "[0]").size() == 0) {
            throw new BotException("配置不存在");
        } else {
            Binder binder = Binder.get(BotFactory.environment);
            if (PropertySourcesUtils.getPrefixedProperties(BotFactory.environment.getPropertySources(), configKey + "[0]").size() > 0) {
                botConfigs = binder.bind(configKey, Bindable.listOf(BotConfig.class)).get();
            } else {
                botConfigs = new ArrayList<>();
                botConfigs.add(binder.bind(configKey, Bindable.of(BotConfig.class)).get());
            }
        }
        if (botConfigs.isEmpty()) {
            throw new BotException("配置不存在");
        }
        BotDispatcher botDispatcher = BotFactory.getBeanByClass(BotDispatcher.class);
        if (botDispatcher == null) {
            throw new BotException("BotDispatcher初始化失败");
        }
        for (BotConfig botConfig : botConfigs) {
            BotFactory.bots.add(new Bot(botConfig, botDispatcher));
        }
        for (Bot bot : BotFactory.bots) {
            bot.getBotClient().connection();
        }
    }

    public static List<HandlerMethod> getHandlerMethodList(String botName) {
        return handlerMethodMap.get(botName);
    }

    public static List<Object> handleMethod(Set<HandlerMethod> handlerMethodSet, MessageEvent messageEvent, MessageChain messageChain, Bot bot) {
        List<Object> resultList = new ArrayList<>();
        for (HandlerMethod handlerMethod : handlerMethodSet) {
            Class<?>[] parameterTypes = handlerMethod.getMethod().getParameterTypes();
            Object[] objects = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> parameterType = parameterTypes[i];
                MessageObjectInjector<?> objectInjector = objectInjectorMap.get(parameterType);
                if (objectInjector == null) {
                    objects[i] = null;
                } else {
                    objects[i] = objectInjector.getObject(messageEvent, messageChain, bot);
                }
            }
            try {
                resultList.add(handlerMethod.getMethod().invoke(handlerMethod.getObject(), objects));
            } catch (IllegalAccessException | InvocationTargetException e) {
                Throwable cause = e.getCause();
                if (cause == null) {
                    log.error(e.getMessage(), e);
                } else {
                    log.error(cause.getMessage(), cause);
                }
                return new ArrayList<>();
            }
        }
        return resultList;
    }

    public static ConfigurableApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBeanByName(String name) {
        if (applicationContext == null || !applicationContext.containsBean(name)) {
            return null;
        }
        return applicationContext.getBean(name);
    }

    public static <T> T getBeanByClass(Class<T> clazz) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(clazz);
    }

    public static <T> T getBeanByName(String name, Class<T> clazz) {
        if (applicationContext == null || !applicationContext.containsBean(name)) {
            return null;
        }
        return applicationContext.getBean(name, clazz);
    }

    public static <T> Map<String, T> getBeansByClass(Class<T> tClass) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBeansOfType(tClass);
    }

    public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> aClass) {
        return applicationContext.getBeansWithAnnotation(aClass);
    }
}
