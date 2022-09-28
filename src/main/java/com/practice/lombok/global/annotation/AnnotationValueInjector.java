package com.practice.lombok.global.annotation;

import com.practice.lombok.global.annotation.logger.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class AnnotationValueInjector implements ApplicationListener<ApplicationStartedEvent> {
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        ConfigurableApplicationContext ac = event.getApplicationContext();
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for(String name : beanDefinitionNames){
            String packageName = this.getClass().getPackageName().split(".global")[0];
            Object bean = ac.getBean(name);
            String classPath = bean.getClass().getName();
            if (!classPath.contains(packageName))
                continue;
            if(classPath.contains("$"))
                classPath = classPath.substring(0,classPath.indexOf("$"));
            injectLogger(bean, classPath);
        }
    }

    private void injectLogger(Object bean, String classPath) {
        try {
            Class<?> beanClass = Class.forName(classPath);
            Field[] fields = beanClass.getDeclaredFields();
            for(Field field:fields){
                Slf4j annotation = field.getDeclaredAnnotation(Slf4j.class);
                if(annotation!=null){
                    field.setAccessible(true);
                    Logger logger = LoggerFactory.getLogger(classPath);
                    field.set(bean, logger);
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
