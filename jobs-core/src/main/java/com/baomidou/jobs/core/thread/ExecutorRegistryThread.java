package com.baomidou.jobs.core.thread;

import com.baomidou.jobs.core.JobsConstant;
import com.baomidou.jobs.core.enums.RegistryConfig;
import com.baomidou.jobs.core.executor.JobsAbstractExecutor;
import com.baomidou.jobs.core.model.RegistryParam;
import com.baomidou.jobs.core.web.IJobsAdmin;
import com.baomidou.jobs.core.web.JobsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by xuxueli on 17/3/2.
 */
public class ExecutorRegistryThread {
    private static Logger logger = LoggerFactory.getLogger(ExecutorRegistryThread.class);

    private static ExecutorRegistryThread instance = new ExecutorRegistryThread();
    public static ExecutorRegistryThread getInstance(){
        return instance;
    }

    private Thread registryThread;
    private volatile boolean toStop = false;
    public void start(final String appName, final String address){

        // valid
        if (appName==null || appName.trim().length()==0) {
            logger.warn(">>>>>>>>>>> jobs, executor registry config fail, appName is null.");
            return;
        }
        if (JobsAbstractExecutor.getAdminBizList() == null) {
            logger.warn(">>>>>>>>>>> jobs, executor registry config fail, adminAddresses is null.");
            return;
        }

        registryThread = new Thread(new Runnable() {
            @Override
            public void run() {

                // registry
                while (!toStop) {
                    try {
                        RegistryParam registryParam = new RegistryParam(RegistryConfig.RegistType.EXECUTOR.name(), appName, address);
                        for (IJobsAdmin adminBiz: JobsAbstractExecutor.getAdminBizList()) {
                            try {
                                JobsResponse<String> registryResult = adminBiz.registry(registryParam);
                                if (registryResult!=null && JobsConstant.CODE_SUCCESS == registryResult.getCode()) {
                                    registryResult = JobsResponse.ok();
                                    logger.debug(">>>>>>>>>>> jobs registry success, registryParam:{}, registryResult:{}", new Object[]{registryParam, registryResult});
                                    break;
                                } else {
                                    logger.info(">>>>>>>>>>> jobs registry fail, registryParam:{}, registryResult:{}", new Object[]{registryParam, registryResult});
                                }
                            } catch (Exception e) {
                                logger.info(">>>>>>>>>>> jobs registry error, registryParam:{}", registryParam, e);
                            }

                        }
                    } catch (Exception e) {
                        if (!toStop) {
                            logger.error(e.getMessage(), e);
                        }

                    }

                    try {
                        if (!toStop) {
                            TimeUnit.SECONDS.sleep(RegistryConfig.BEAT_TIMEOUT);
                        }
                    } catch (InterruptedException e) {
                        if (!toStop) {
                            logger.warn(">>>>>>>>>>> jobs, executor registry thread interrupted, error msg:{}", e.getMessage());
                        }
                    }
                }

                // registry remove
                try {
                    RegistryParam registryParam = new RegistryParam(RegistryConfig.RegistType.EXECUTOR.name(), appName, address);
                    for (IJobsAdmin adminBiz: JobsAbstractExecutor.getAdminBizList()) {
                        try {
                            JobsResponse<String> registryResult = adminBiz.registryRemove(registryParam);
                            if (registryResult!=null && JobsConstant.CODE_SUCCESS == registryResult.getCode()) {
                                registryResult = JobsResponse.ok();
                                logger.info(">>>>>>>>>>> jobs registry-remove success, registryParam:{}, registryResult:{}", new Object[]{registryParam, registryResult});
                                break;
                            } else {
                                logger.info(">>>>>>>>>>> jobs registry-remove fail, registryParam:{}, registryResult:{}", new Object[]{registryParam, registryResult});
                            }
                        } catch (Exception e) {
                            if (!toStop) {
                                logger.info(">>>>>>>>>>> jobs registry-remove error, registryParam:{}", registryParam, e);
                            }

                        }

                    }
                } catch (Exception e) {
                    if (!toStop) {
                        logger.error(e.getMessage(), e);
                    }
                }
                logger.info(">>>>>>>>>>> jobs, executor registry thread destory.");

            }
        });
        registryThread.setDaemon(true);
        registryThread.setName("jobs, executor ExecutorRegistryThread");
        registryThread.start();
    }

    public void toStop() {
        toStop = true;
        // interrupt and wait
        registryThread.interrupt();
        try {
            registryThread.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
