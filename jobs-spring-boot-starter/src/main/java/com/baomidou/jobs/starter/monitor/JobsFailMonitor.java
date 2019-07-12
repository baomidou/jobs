package com.baomidou.jobs.starter.monitor;

import com.baomidou.jobs.starter.JobsHelper;
import com.baomidou.jobs.starter.entity.JobsInfo;
import com.baomidou.jobs.starter.entity.JobsLog;
import com.baomidou.jobs.starter.trigger.JobsTrigger;
import com.baomidou.jobs.starter.trigger.TriggerTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * job monitor instance
 *
 * @author xuxueli 2015-9-1 18:05:56
 */
@Slf4j
public class JobsFailMonitor {
    private static JobsFailMonitor instance = new JobsFailMonitor();

    public static JobsFailMonitor getInstance() {
        return instance;
    }


    /**
     * ---------------------- monitor ----------------------
     */
    private Thread monitorThread;
    private volatile boolean toStop = false;

    public void start() {
        monitorThread = new Thread(() -> {

            // monitor
            while (!toStop) {
                try {

                    List<Object> failLogIds = JobsHelper.getJobLogService().listFailIds(1000);
                    if (failLogIds != null && !failLogIds.isEmpty()) {
                        for (Object id : failLogIds) {
                            int failLogId = (Integer) id;
                            // lock log
                            int lockRet = JobsHelper.getJobLogService().updateAlarmStatus(Integer.valueOf(failLogId), 0, -1);
                            if (lockRet < 1) {
                                continue;
                            }
                            JobsLog jobLog = JobsHelper.getJobLogService().getById(failLogId);
                            JobsInfo info = JobsHelper.getJobInfoService().getById(jobLog.getJobId());

                            // 1、fail retry monitor
                            if (jobLog.getExecutorFailRetryCount() > 0) {
                                JobsTrigger.trigger(jobLog.getJobId(), TriggerTypeEnum.RETRY, (jobLog.getExecutorFailRetryCount() - 1), null);
                                String retryMsg = "<br><br><span style=\"color:#F39C12;\" > >>>>>>>>>>>失败重试触发<<<<<<<<<<< </span><br>";
                                jobLog.setTriggerMsg(jobLog.getTriggerMsg() + retryMsg);
                                JobsHelper.getJobLogService().updateById(jobLog);
                            }

                            // 2、fail alarm monitor
                            // 告警状态：0-默认、-1=锁定状态、1-无需告警、2-告警成功、3-告警失败
                            int newAlarmStatus = 1;
                            if (info != null && info.getAlarmEmail() != null && info.getAlarmEmail().trim().length() > 0) {
                                boolean alarmResult = false;
                                try {
                                    alarmResult = JobsHelper.getJobAlarmHandler().failed(info);
                                } catch (Exception e) {
                                    log.error(e.getMessage(), e);
                                }
                                newAlarmStatus = alarmResult ? 2 : 3;
                            }

                            JobsHelper.getJobLogService().updateAlarmStatus(failLogId, -1, newAlarmStatus);
                        }
                    }

                    TimeUnit.SECONDS.sleep(10);
                } catch (Exception e) {
                    if (!toStop) {
                        log.error("Jobs, job fail monitor monitor error:{}", e);
                    }
                }
            }

            log.info("Jobs, job fail monitor monitor stop");

        });
        monitorThread.setDaemon(true);
        monitorThread.setName("jobs, admin JobsFailMonitor");
        monitorThread.start();
    }

    public void toStop() {
        toStop = true;
        // interrupt and wait
        monitorThread.interrupt();
        try {
            monitorThread.join();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
}
