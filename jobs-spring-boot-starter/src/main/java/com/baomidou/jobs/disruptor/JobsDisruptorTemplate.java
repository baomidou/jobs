package com.baomidou.jobs.disruptor;

import com.baomidou.jobs.model.JobsInfo;
import com.lmax.disruptor.dsl.Disruptor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 日志 Disruptor 辅助类
 *
 * @author jobob
 * @since 2019-06-27
 */
public class JobsDisruptorTemplate {
    @Autowired
    protected Disruptor<JobsInfoEvent> disruptor;

    public void publish(JobsInfo jobsInfo, long waitSecond) {
        JobsInfoEvent jobsInfoEvent = new JobsInfoEvent();
        jobsInfoEvent.setJobsInfo(jobsInfo);
        jobsInfoEvent.setWaitSecond(waitSecond);
        disruptor.publishEvent((event, sequence, bind) -> event.setJobsInfo(
                bind.getJobsInfo()), jobsInfoEvent);
    }
}
