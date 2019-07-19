package com.baomidou.jobs.disruptor;

import com.baomidou.jobs.model.JobsInfo;
import com.baomidou.jobs.trigger.JobsTrigger;
import com.baomidou.jobs.trigger.TriggerTypeEnum;
import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * disruptor EventHandler
 *
 * @author jobob
 * @since 2019-06-27
 */
@Slf4j
public class JobsEventHandler implements EventHandler<JobsInfoEvent> {

    @Override
    public void onEvent(JobsInfoEvent jobsInfoEvent, long sequence, boolean endOfBatch) throws Exception {
        JobsInfo jobsInfo = jobsInfoEvent.getJobsInfo();
        if (null != jobsInfo) {
            if (jobsInfoEvent.getWaitSecond() > 0) {
                try {
                    TimeUnit.MILLISECONDS.sleep(jobsInfoEvent.getWaitSecond());
                } catch (InterruptedException e) {
                    // to do nothing
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("Jobs Event Handler: {}", jobsInfo.toString());
            }
            JobsTrigger.trigger(jobsInfo.getId(), TriggerTypeEnum.CRON, -1,  null);
        }
    }
}
