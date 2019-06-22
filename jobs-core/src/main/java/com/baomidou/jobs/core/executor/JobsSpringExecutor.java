package com.baomidou.jobs.core.executor;

import com.baomidou.jobs.core.glue.IGlueFactory;
import com.baomidou.jobs.core.handler.IJobsHandler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * jobs executor (for spring)
 *
 * @author xxl jobob
 * @since 2019-06-22
 */
public class JobsSpringExecutor extends JobsAbstractExecutor implements ApplicationContextAware {


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
        String[] jobsHandlerArr = applicationContext.getBeanNamesForType(IJobsHandler.class);
        if (null != jobsHandlerArr && jobsHandlerArr.length > 0) {
            for (String jobsHandler : jobsHandlerArr) {
                putJobsHandler(jobsHandler, (IJobsHandler) applicationContext.getBean(jobsHandler));
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
