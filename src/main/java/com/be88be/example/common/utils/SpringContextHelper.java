package com.be88be.example.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Map;

@Component
public class SpringContextHelper implements ApplicationContextAware {
//	ApplicationContextHolder.java
    private static ApplicationContext context = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static <T> T getBean(String beanName, Class<T> clazz) {
        return (T) context.getBean(beanName, clazz);
    }

    public static <T> T getBean(Class<T> clazz) {
        return (T) context.getBean(clazz);
    }

    public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> aClass) {
        return context.getBeansWithAnnotation(aClass);
    }
}
