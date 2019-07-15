package com.baomidou.jobs.starter.monitor;

import com.baomidou.jobs.starter.JobsConstant;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * job registry instance
 *
 * @author xxl jobob
 * @since 2019-06-23
 */
@Slf4j
public class JobsRegistryMonitor {
    private Thread registryThread;
    private volatile boolean toStop = false;

    private static JobsRegistryMonitor instance = new JobsRegistryMonitor();

    public static JobsRegistryMonitor getInstance() {
        return instance;
    }

    public void start() {
        registryThread = new Thread(() -> {
            while (!toStop) {
                try {
                    System.out.println("----注册-------");
                    // remove dead address (admin/executor)
                    // JobsHelper.getJobRegistryService().removeTimeOut(RegistryConfig.DEAD_TIMEOUT);

                    // fresh online address (admin/executor)
                } catch (Exception e) {
                    if (!toStop) {
                        log.error("Jobs registry monitor monitor error:{}", e);
                    }
                }
                try {
                    TimeUnit.SECONDS.sleep(JobsConstant.BEAT_TIMEOUT);
                } catch (InterruptedException e) {
                    if (!toStop) {
                        log.error("Jobs registry monitor monitor error:{}", e);
                    }
                }
            }
            log.info("Jobs registry monitor monitor stop");
        });
        registryThread.setDaemon(true);
        registryThread.setName("jobs, admin JobsRegistryMonitor");
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
