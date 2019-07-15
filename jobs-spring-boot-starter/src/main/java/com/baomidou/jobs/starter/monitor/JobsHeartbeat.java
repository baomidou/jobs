package com.baomidou.jobs.starter.monitor;

import com.baomidou.jobs.starter.JobsConstant;
import com.baomidou.jobs.starter.lock.IJobsLock;
import com.baomidou.jobs.starter.JobsHelper;
import com.baomidou.jobs.starter.cron.CronExpression;
import com.baomidou.jobs.starter.entity.JobsInfo;
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

    @Override
    public void run() {
        log.debug("Jobs, JobsHeartbeat begin");
        IJobsLock jobsLock = JobsHelper.getJobsLock();
        try {
            // 尝试获取锁
            if (jobsLock.tryLock(JobsConstant.DEFAULT_LOCK_KEY)) {
                long nowTime = System.currentTimeMillis();
                // 1、预读10s内调度任务
                List<JobsInfo> scheduleList = JobsHelper.getJobInfoService().scheduleJobQuery(nowTime + 10000);
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
                        JobsHelper.getJobInfoService().updateById(tempJobsInfo);
                    }

                }
            }
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                if (log.isDebugEnabled()) {
                    log.debug("Jobs, JobsHeartbeat locking");
                }
            } else {
                log.error("Jobs, JobsHeartbeat error:{}", e);
            }
        } finally {
            // 释放锁
            jobsLock.unlock(JobsConstant.DEFAULT_LOCK_KEY);
        }
        log.debug("Jobs, JobsHeartbeat end");
    }
}
