package com.baomidou.jobs.core.glue.impl;

import com.baomidou.jobs.core.executor.JobsSpringExecutor;
import com.baomidou.jobs.core.glue.IGlueFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AnnotationUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author xuxueli 2018-11-01
 */
@Slf4j
public class SpringGlueFactory extends IGlueFactory {

    /**
     * inject action of spring
     *
     * @param instance
     */
    @Override
    public void injectService(Object instance) {
        if (instance == null) {
            return;
        }

        if (JobsSpringExecutor.getApplicationContext() == null) {
            return;
        }

        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            Object fieldBean = null;
            // with bean-id, bean could be found by both @Resource and @Autowired, or bean could only be found by @Autowired

            if (AnnotationUtils.getAnnotation(field, Resource.class) != null) {
                try {
                    Resource resource = AnnotationUtils.getAnnotation(field, Resource.class);
                    if (resource.name() != null && resource.name().length() > 0) {
                        fieldBean = JobsSpringExecutor.getApplicationContext().getBean(resource.name());
                    } else {
                        fieldBean = JobsSpringExecutor.getApplicationContext().getBean(field.getName());
                    }
                } catch (Exception e) {
                }
                if (fieldBean == null) {
                    fieldBean = JobsSpringExecutor.getApplicationContext().getBean(field.getType());
                }
            } else if (AnnotationUtils.getAnnotation(field, Autowired.class) != null) {
                Qualifier qualifier = AnnotationUtils.getAnnotation(field, Qualifier.class);
                if (qualifier != null && qualifier.value() != null && qualifier.value().length() > 0) {
                    fieldBean = JobsSpringExecutor.getApplicationContext().getBean(qualifier.value());
                } else {
                    fieldBean = JobsSpringExecutor.getApplicationContext().getBean(field.getType());
                }
            }

            if (fieldBean != null) {
                field.setAccessible(true);
                try {
                    field.set(instance, fieldBean);
                } catch (IllegalArgumentException e) {
                    log.error(e.getMessage(), e);
                } catch (IllegalAccessException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }
}
