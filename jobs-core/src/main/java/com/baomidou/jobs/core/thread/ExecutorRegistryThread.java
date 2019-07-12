package com.baomidou.jobs.core.thread;

import com.baomidou.jobs.core.JobsConstant;
import com.baomidou.jobs.core.executor.JobsAbstractExecutor;
import com.baomidou.jobs.core.model.RegistryParam;
import com.baomidou.jobs.core.web.IJobsAdmin;
import com.baomidou.jobs.core.web.JobsResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 执行器注册线程
 *
 * @author xxl jobob
 * @since 2019-06-23
 */
@Slf4j
public class ExecutorRegistryThread {
    private static ExecutorRegistryThread instance = new ExecutorRegistryThread();

    public static ExecutorRegistryThread getInstance() {
        return instance;
    }

    private Thread registryThread;
    private volatile boolean toStop = false;

    public void start(final String appName, final String address) {

        // valid
        if (appName == null || appName.trim().length() == 0) {
            log.warn("Jobs executor registry config fail, appName is null.");
            return;
        }
        if (JobsAbstractExecutor.getJobsAdminList() == null) {
            log.warn("Jobs executor registry config fail, adminAddresses is null.");
            return;
        }

        registryThread = new Thread(() -> {

            // registry
            while (!toStop) {
                try {
                    RegistryParam registryParam = new RegistryParam(appName, address);
                    for (IJobsAdmin jobsAdmin : JobsAbstractExecutor.getJobsAdminList()) {
                        try {
                            JobsResponse<Boolean> registryResult = jobsAdmin.registry(registryParam);
                            if (registryResult != null && JobsConstant.CODE_SUCCESS == registryResult.getCode()) {
                                registryResult = JobsResponse.ok();
                                log.debug("Jobs registry success, registryParam:{}, registryResult:{}", new Object[]{registryParam, registryResult});
                                break;
                            } else {
                                log.info("Jobs registry fail, registryParam:{}, registryResult:{}", new Object[]{registryParam, registryResult});
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
                for (IJobsAdmin jobsAdmin : JobsAbstractExecutor.getJobsAdminList()) {
                    try {
                        JobsResponse<Boolean> registryResult = jobsAdmin.registryRemove(registryParam);
                        if (registryResult != null && JobsConstant.CODE_SUCCESS == registryResult.getCode()) {
                            registryResult = JobsResponse.ok();
                            log.info("Jobs registry-remove success, registryParam:{}, registryResult:{}", new Object[]{registryParam, registryResult});
                            break;
                        } else {
                            log.info("Jobs registry-remove fail, registryParam:{}, registryResult:{}", new Object[]{registryParam, registryResult});
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
