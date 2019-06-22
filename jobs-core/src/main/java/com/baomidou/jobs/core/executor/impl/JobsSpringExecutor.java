package com.baomidou.jobs.core.executor.impl;

import com.baomidou.jobs.core.executor.IJobsExecutor;
import com.baomidou.jobs.core.glue.IGlueFactory;
import com.baomidou.jobs.core.handler.IJobsHandler;
import com.baomidou.jobs.core.handler.annotation.JobsHandler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * jobs executor (for spring)
 *
 * @author xuxueli 2018-11-01 09:24:52
 */
public class JobsSpringExecutor extends IJobsExecutor implements ApplicationContextAware {


    @Override
    public void start() throws Exception {

        // init JobsHandler Repository
        initJobHandlerRepository(applicationContext);

        // refresh IGlueFactory
        IGlueFactory.refreshInstance(1);

        // super start
        super.start();
    }

    private void initJobHandlerRepository(ApplicationContext applicationContext) {
        if (applicationContext == null) {
            return;
        }

        // init job handler action
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(JobsHandler.class);

        if (serviceBeanMap != null && serviceBeanMap.size() > 0) {
            for (Object serviceBean : serviceBeanMap.values()) {
                if (serviceBean instanceof IJobsHandler) {
                    String name = serviceBean.getClass().getAnnotation(JobsHandler.class).value();
                    IJobsHandler handler = (IJobsHandler) serviceBean;
                    if (loadJobHandler(name) != null) {
                        throw new RuntimeException("jobs jobhandler naming conflicts.");
                    }
                    registJobHandler(name, handler);
                }
            }
        }
    }

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        JobsSpringExecutor.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

}
