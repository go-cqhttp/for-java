package com.zhuangxv.bot.core.component;

import com.zhuangxv.bot.annotation.*;
import com.zhuangxv.bot.config.BotConfig;
import com.zhuangxv.bot.config.PropertySourcesUtils;
import com.zhuangxv.bot.core.Bot;
import com.zhuangxv.bot.core.network.BotNetworkFactory;
import com.zhuangxv.bot.event.BaseEvent;
import com.zhuangxv.bot.exception.BotException;
import com.zhuangxv.bot.injector.ObjectInjector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.ClassUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author xiaoxu
 * @since 2022-05-24 10:19
 */
@Slf4j
public class BotFactory implements ApplicationContextAware, DisposableBean {

    private static final Map<Long, Bot> bots = new HashMap<>();

    private static ConfigurableEnvironment environment;

    private static ConfigurableApplicationContext applicationContext;
    private static final List<HandlerMethod> handlerMethodList = new ArrayList<>();
    private static Map<String, Map<Class<?>, ObjectInjector<?>>> objectInjectorMap;

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

    @SuppressWarnings("all")
    public static void initHandlerMethod() {
        Map<String, Object> beans = BotFactory.getApplicationContext().getBeansOfType(Object.class);
        for (Object bean : beans.values()) {
            Class<?> beanClass = ClassUtils.getUserClass(bean);
            Set<Method> methodSet = Arrays.stream(beanClass.getMethods()).filter(method ->
                    method.isAnnotationPresent(GroupMessageHandler.class)
                            || method.isAnnotationPresent(TempMessageHandler.class)
                            || method.isAnnotationPresent(FriendMessageHandler.class)
                            || method.isAnnotationPresent(GroupRecallHandler.class)
                            || method.isAnnotationPresent(MemberAddHandler.class)
            ).collect(Collectors.toSet());
            methodSet.forEach(method -> {
                HandlerMethod handlerMethod = new HandlerMethod() {
                    {
                        setType(beanClass);
                        setMethod(method);
                        setObject(bean);
                    }
                };
                handlerMethodList.add(handlerMethod);
            });
        }
        objectInjectorMap = new HashMap<>();
        Map<String, ObjectInjector> objectInjectors = getBeansByClass(ObjectInjector.class);
        if (objectInjectors != null) {
            for (ObjectInjector objectInjector : objectInjectors.values()) {
                for (String type : objectInjector.getType()) {
                    Map<Class<?>, ObjectInjector<?>> objectInjectorMapTemp = BotFactory.objectInjectorMap.computeIfAbsent(type, key -> new HashMap<>());
                    objectInjectorMapTemp.put(objectInjector.getClassType(), objectInjector);
                }
            }
        }
        log.info("事件处理器初始化完成.");
    }

    public static void initBot() {
        BotDispatcher botDispatcher = BotFactory.getBeanByClass(BotDispatcher.class);
        if (botDispatcher == null) {
            throw new BotException("BotDispatcher初始化失败");
        }
        String configKey = "bot";
        List<BotConfig> botConfigs = null;
        if (PropertySourcesUtils.getPrefixedProperties(BotFactory.environment.getPropertySources(), configKey).size() != 0
                || PropertySourcesUtils.getPrefixedProperties(BotFactory.environment.getPropertySources(), configKey + "[0]").size() != 0) {
            Binder binder = Binder.get(BotFactory.environment);
            if (PropertySourcesUtils.getPrefixedProperties(BotFactory.environment.getPropertySources(), configKey + "[0]").size() > 0) {
                botConfigs = binder.bind(configKey, Bindable.listOf(BotConfig.class)).get();
            } else {
                botConfigs = new ArrayList<>();
                botConfigs.add(binder.bind(configKey, Bindable.of(BotConfig.class)).get());
            }
        }
        if (botConfigs != null && !botConfigs.isEmpty()) {
            BotNetworkFactory.initBotNetwork(botConfigs, bots, botDispatcher);
        }

    }

    public static void addBot(BotConfig botConfig) {
        List<BotConfig> botConfigs = new ArrayList<>();
        botConfigs.add(botConfig);
        BotDispatcher botDispatcher = BotFactory.getBeanByClass(BotDispatcher.class);
        if (botDispatcher == null) {
            throw new BotException("BotDispatcher初始化失败");
        }
        BotNetworkFactory.initBotNetwork(botConfigs, bots, botDispatcher);
    }

    public static void addBot(List<BotConfig> botConfigs) {
        BotDispatcher botDispatcher = BotFactory.getBeanByClass(BotDispatcher.class);
        if (botDispatcher == null) {
            throw new BotException("BotDispatcher初始化失败");
        }
        BotNetworkFactory.initBotNetwork(botConfigs, bots, botDispatcher);
    }

    public static Map<Long, Bot> getBots() {
        return bots;
    }

    public static Set<HandlerMethod> getHandlerMethodListByAnnotation(Predicate<? super HandlerMethod> predicate) {
        if (handlerMethodList.isEmpty()) {
            return new HashSet<>();
        }
        return handlerMethodList.stream().filter(predicate).collect(Collectors.toSet());
    }

    public static List<Object> handleMethod(Bot bot, BaseEvent event, Predicate<? super HandlerMethod> predicate, String objectInjectorType) {
        List<Object> resultList = new ArrayList<>();
        Set<HandlerMethod> handlerMethodSet = getHandlerMethodListByAnnotation(predicate);
        for (HandlerMethod handlerMethod : handlerMethodSet) {
            Class<?>[] parameterTypes = handlerMethod.getMethod().getParameterTypes();
            Object[] objects = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> parameterType = parameterTypes[i];
                ObjectInjector<?> objectInjector = objectInjectorMap.get(objectInjectorType) != null ? objectInjectorMap.get(objectInjectorType).get(parameterType) : null;
                if (objectInjector == null) {
                    objectInjector = objectInjectorMap.get("all") != null ? objectInjectorMap.get("all").get(parameterType) : null;
                }
                if (objectInjector == null) {
                    objects[i] = null;
                } else {
                    objects[i] = objectInjector.getObject(event, bot);
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

    public static <T> T getBeanByClass(Class<T> clazz) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(clazz);
    }

    public static <T> Map<String, T> getBeansByClass(Class<T> tClass) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBeansOfType(tClass);
    }
}
