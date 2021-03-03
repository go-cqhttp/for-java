package com.zhuangxv.bot.core;

import com.zhuangxv.bot.annotation.FriendMessageHandler;
import com.zhuangxv.bot.annotation.GroupMessageHandler;
import com.zhuangxv.bot.annotation.TempMessageHandler;
import com.zhuangxv.bot.api.ApiResult;
import com.zhuangxv.bot.api.BaseApi;
import com.zhuangxv.bot.event.BaseEvent;
import com.zhuangxv.bot.exception.BotException;
import com.zhuangxv.bot.injector.ObjectInjector;
import com.zhuangxv.bot.message.MessageChain;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Slf4j
public class BotApplication implements ApplicationContextAware, DisposableBean {

    private static final Bootstrap clientBootstrap = new Bootstrap();
    private static Channel channel;

    private static ConfigurableApplicationContext applicationContext;
    private static Map<String, List<HandlerMethod>> handlerMethodMap;
    private static Map<Class<?>, ObjectInjector<?>> objectInjectorMap;
    private static final Map<String, ResultCondition> resultConditionMap = new ConcurrentHashMap<>();
    private static final Map<String, ApiResult> apiResultMap = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (BotApplication.applicationContext == null && applicationContext instanceof ConfigurableApplicationContext) {
            BotApplication.applicationContext = (ConfigurableApplicationContext) applicationContext;
        }
    }

    @Override
    public void destroy() {
        log.info("clear ApplicationContext:" + applicationContext);
        applicationContext = null;
    }

    public static Bootstrap getClientBootstrap() {
        return clientBootstrap;
    }

    public static Channel getChannel() {
        if (channel == null || !channel.isActive() || !channel.pipeline().get(WebSocketHandler.class).getWebSocketClientHandshaker().isHandshakeComplete()) {
            throw new BotException("连接失败");
        }
        return channel;
    }

    public static void connection(String host, int port) {
        if (channel != null && channel.isActive()) {
            return;
        }
        ChannelFuture channelFuture = clientBootstrap.connect(host, port);
        channelFuture.addListener((ChannelFutureListener) futureListener -> {
            if (futureListener.isSuccess()) {
                channel = futureListener.channel();
            } else {
                log.error("Failed to connect to go-cqhttp, try connect after 10s");
                futureListener.channel().eventLoop().schedule(() -> connection(host, port), 10, TimeUnit.SECONDS);
            }
        });
    }

    public static void initHandlerMethod() {
        Map<String, Object> beans = BotApplication.getApplicationContext().getBeansOfType(Object.class);
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
        Map<String, ObjectInjector> objectInjectors = getBeansByClass(ObjectInjector.class);
        if (objectInjectors != null) {
            objectInjectors.values().forEach(objectInjector -> objectInjectorMap.put(objectInjector.getType(), objectInjector));
        }
        log.info("初始化事件处理器完成.");
    }

    public static List<HandlerMethod> getHandlerMethodList(String botName) {
        return handlerMethodMap.get(botName);
    }

    public static List<Object> handleMethod(Set<HandlerMethod> handlerMethodSet, BaseEvent baseEvent, MessageChain messageChain) {
        List<Object> resultList = new ArrayList<>();
        for (HandlerMethod handlerMethod : handlerMethodSet) {
            Class<?>[] parameterTypes = handlerMethod.getMethod().getParameterTypes();
            Object[] objects = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> parameterType = parameterTypes[i];
                ObjectInjector<?> objectInjector = objectInjectorMap.get(parameterType);
                if (objectInjector == null) {
                    objects[i] = null;
                } else {
                    objects[i] = objectInjector.getObject(baseEvent, messageChain);
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

    public synchronized static ApiResult invokeApi(BaseApi baseApi) {
        channel.writeAndFlush(new TextWebSocketFrame(baseApi.buildJson()));
        ResultCondition resultCondition = createResultCondition();
        resultConditionMap.put(baseApi.getEcho(), resultCondition);
        ApiResult apiResult = getApiResult(baseApi.getEcho());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            log.warn(e.getMessage(), e);
        }
        return apiResult;
    }

    private static ApiResult getApiResult(String echo) {
        ResultCondition resultCondition = resultConditionMap.get(echo);
        if (resultCondition == null) {
            return null;
        }
        try {
            resultCondition.getLock().lock();
            ApiResult apiResult = apiResultMap.get(echo);
            if (apiResult == null) {
                resultCondition.getCondition().await(5, TimeUnit.SECONDS);
                apiResult = apiResultMap.get(echo);
            }
            apiResultMap.remove(echo);
            return apiResult;
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            resultCondition.getLock().unlock();
        }
    }

    public static void setApiResult(String echo, ApiResult apiResult) {
        ResultCondition resultCondition = resultConditionMap.get(echo);
        if (resultCondition == null) {
            return;
        }
        apiResultMap.put(echo, apiResult);
        try {
            resultCondition.getLock().lock();
            resultCondition.getCondition().signalAll();
        } finally {
            resultCondition.getLock().unlock();
        }
    }

    private static ResultCondition createResultCondition() {
        ResultCondition resultCondition = new ResultCondition();
        resultCondition.setLock(new ReentrantLock());
        resultCondition.setCondition(resultCondition.getLock().newCondition());
        return resultCondition;
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
