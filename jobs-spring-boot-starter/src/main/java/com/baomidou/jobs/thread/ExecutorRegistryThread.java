package com.baomidou.jobs.thread;

import com.baomidou.jobs.JobsConstant;
import com.baomidou.jobs.executor.JobsAbstractExecutor;
import com.baomidou.jobs.model.param.RegisterStatusEnum;
import com.baomidou.jobs.model.param.RegistryParam;
import com.baomidou.jobs.service.IJobsService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 执行器注册线程
 *
 * @author jobob
 * @since 2019-07-18
 */
@Slf4j
public class ExecutorRegistryThread {
    private static ExecutorRegistryThread INSTANCE = new ExecutorRegistryThread();

    public static ExecutorRegistryThread getInstance() {
        return INSTANCE;
    }

    private Thread registryThread;
    private volatile boolean toStop = false;

    public void start(final String appName, final String address) {

        // valid
        if (appName == null || appName.trim().length() == 0) {
            log.warn("Jobs executor registry config fail, appName is null.");
            return;
        }
        if (null == JobsAbstractExecutor.getJobsServiceList()) {
            log.warn("Jobs executor registry config fail, adminAddresses is null.");
            return;
        }

        registryThread = new Thread(() -> {

            // registry
            while (!toStop) {
                try {
                    RegistryParam registryParam = new RegistryParam(appName, address);
                    for (IJobsService jobsService : JobsAbstractExecutor.getJobsServiceList()) {
                        try {
                            if (jobsService.registry(registryParam)) {
                                log.debug("Jobs registry success, registryParam:{}", registryParam);
                                break;
                            } else {
                                log.info("Jobs registry fail, registryParam:{}", registryParam);
                            }
                        } catch (Exception e) {
                            log.info("Jobs registry error, registryParam:{}", registryParam, e);
                        }

                    }
                } catch (Exception e) {
                    if (!toStop) {
                        log.error(e.getMessage(), e);
                    }

                }

                try {
                    if (!toStop) {
                        TimeUnit.SECONDS.sleep(JobsConstant.BEAT_TIMEOUT);
                    }
                } catch (InterruptedException e) {
                    if (!toStop) {
                        log.warn("Jobs executor registry thread interrupted, error msg:{}", e.getMessage());
                    }
                }
            }

            // registry remove
            try {
                RegistryParam registryParam = new RegistryParam(appName, address);
                for (IJobsService jobsService : JobsAbstractExecutor.getJobsServiceList()) {
                    try {
                        registryParam.setRegisterStatusEnum(RegisterStatusEnum.DISABLED);
                        if (jobsService.removeApp(registryParam)) {
                            log.info("Jobs registry-remove success, registryParam:{}", registryParam);
                            break;
                        } else {
                            log.info("Jobs registry-remove fail, registryParam:{}", registryParam);
                        }
                    } catch (Exception e) {
                        if (!toStop) {
                            log.info("Jobs registry-remove error, registryParam:{}", registryParam, e);
                        }

                    }

                }
            } catch (Exception e) {
                if (!toStop) {
                    log.error(e.getMessage(), e);
                }
            }
            log.info("Jobs executor registry thread destory.");

        });
        registryThread.setDaemon(true);
        registryThread.setName("Jobs executor ExecutorRegistryThread");
        registryThread.start();
    }

    public void toStop() {
        toStop = true;
        // interrupt and wait
        registryThread.interrupt();
        try {
            registryThread.join();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
}
