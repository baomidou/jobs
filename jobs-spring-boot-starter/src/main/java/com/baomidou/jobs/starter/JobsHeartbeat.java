package com.baomidou.jobs.starter;

import com.baomidou.jobs.starter.cron.CronExpression;
import com.baomidou.jobs.starter.model.JobsInfo;
import com.baomidou.jobs.starter.service.IJobsLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;

import java.util.Date;
import java.util.List;

/**
 * jobs 心跳
 *
 * @author jobob
 * @since 2019-07-15
 */
@Slf4j
public class JobsHeartbeat implements Runnable {
    /**
     * 上锁时长
     */
    private long wait = 0;
    /**
     * 心跳时长
     */
    private long beat = 0;

    @Override
    public void run() {
        log.debug("Jobs, JobsHeartbeat begin");
        IJobsLockService jobsLockService = JobsHelper.getJobsLockService();
        try {
            // 尝试获取锁
            if (jobsLockService.tryLock(JobsConstant.DEFAULT_LOCK_KEY)) {
                wait = 0;
                long nowTime = System.currentTimeMillis();
                // 1、预读10s内调度任务
                List<JobsInfo> scheduleList = JobsHelper.getJobsInfoService().scheduleJobQuery(nowTime + 10000);
                if (scheduleList != null && scheduleList.size() > 0) {
                    // 2、推送时间轮
                    for (JobsInfo jobsInfo : scheduleList) {
                        long waitSecond;
                        if (jobsInfo.getNextTime() < nowTime - 10000) {
                            // 过期超10s：本地忽略，当前时间开始计算下次触发时间
                            waitSecond = -1;
                        } else if (jobsInfo.getNextTime() < nowTime) {
                            // 过期10s内：立即触发，计算延迟触发时长
                            waitSecond = nowTime - jobsInfo.getLastTime();
                        } else {
                            // 未过期：等待下次循环
                            continue;
                        }
                        JobsInfo tempJobsInfo = new JobsInfo();
                        tempJobsInfo.setId(jobsInfo.getId());
                        tempJobsInfo.setLastTime(jobsInfo.getNextTime());
                        tempJobsInfo.setNextTime(new CronExpression(jobsInfo.getCron())
                                .getNextValidTimeAfter(new Date()).getTime());
                        if (waitSecond >= 0) {
                            // 推送任务消息
                            JobsHelper.getJobsDisruptorTemplate().publish(jobsInfo, waitSecond);
                        }
                        // 更新任务状态
                        JobsHelper.getJobsInfoService().updateById(tempJobsInfo);
                    }

                }
            }
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                // 上锁时长累计
                ++wait;
                if (log.isDebugEnabled()) {
                    log.debug("Jobs, JobsHeartbeat locking");
                }
            } else {
                log.error("Jobs, JobsHeartbeat error:{}", e);
            }
        } finally {
            // 释放锁，上锁时长超过 90 秒强制解锁
            jobsLockService.unlock(JobsConstant.DEFAULT_LOCK_KEY, wait > 90);
            // 清理异常注册节点
            ++beat;
            if (beat > JobsConstant.BEAT_TIMEOUT) {
                JobsHelper.getJobsRegistryService().cleanTimeout();
                beat = 0;
            }
        }
        log.debug("Jobs, JobsHeartbeat end");
    }
}
