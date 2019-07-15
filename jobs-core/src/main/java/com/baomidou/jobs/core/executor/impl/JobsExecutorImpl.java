package com.baomidou.jobs.core.executor.impl;

import com.baomidou.jobs.core.enums.ExecutorBlockStrategyEnum;
import com.baomidou.jobs.core.executor.IJobsExecutor;
import com.baomidou.jobs.core.executor.JobsAbstractExecutor;
import com.baomidou.jobs.core.handler.IJobsHandler;
import com.baomidou.jobs.core.model.TriggerParam;
import com.baomidou.jobs.core.thread.JobsThread;
import com.baomidou.jobs.core.web.JobsResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Jobs 执行器实现
 *
 * @author jobob
 * @since 2019-07-13
 */
@Slf4j
public class JobsExecutorImpl implements IJobsExecutor {

    @Override
    public JobsResponse<String> beat() {
        return JobsResponse.ok();
    }

    @Override
    public JobsResponse<String> idleBeat(Long jobId) {
        boolean isRunningOrHasQueue = false;
        JobsThread jobThread = JobsAbstractExecutor.getJobsThread(jobId);
        if (jobThread != null && jobThread.isRunningOrHasQueue()) {
            isRunningOrHasQueue = true;
        }

        if (isRunningOrHasQueue) {
            return JobsResponse.failed("job thread is running or has trigger queue.");
        }
        return JobsResponse.ok();
    }

    @Override
    public JobsResponse<String> kill(Long jobId) {
        // kill handlerThread, and create new one
        JobsThread jobThread = JobsAbstractExecutor.getJobsThread(jobId);
        if (jobThread != null) {
            JobsAbstractExecutor.removeJobsThread(jobId, "scheduling center kill job.");
            return JobsResponse.ok();
        }

        return JobsResponse.failed("job thread aleady killed.");
    }

    @Override
    public JobsResponse<String> run(TriggerParam triggerParam) {
        // load old：jobHandler + jobThread
        JobsThread jobsThread = JobsAbstractExecutor.getJobsThread(triggerParam.getJobId());
        IJobsHandler jobsHandler = jobsThread != null ? jobsThread.getHandler() : null;
        String removeOldReason = null;

        // new jobhandler
        IJobsHandler newJobHandler = JobsAbstractExecutor.getJobsHandler(triggerParam.getExecutorHandler());

        // valid old jobThread
        if (null != jobsThread && jobsHandler != newJobHandler) {
            // change handler, need kill old thread
            removeOldReason = "change jobhandler or glue type, and terminate the old job thread.";

            jobsThread = null;
            jobsHandler = null;
        }

        // valid handler
        if (null == jobsHandler) {
            jobsHandler = newJobHandler;
            if (jobsHandler == null) {
                return JobsResponse.failed("job handler [" + triggerParam.getExecutorHandler() + "] not found.");
            }
        }

        // executor block strategy
        if (null != jobsThread) {
            switch (ExecutorBlockStrategyEnum.match(triggerParam.getExecutorBlockStrategy(), null)) {
                case DISCARD_LATER:
                    // discard when running
                    if (jobsThread.isRunningOrHasQueue()) {
                        return JobsResponse.failed("block strategy effect：" + ExecutorBlockStrategyEnum.DISCARD_LATER.getTitle());
                    }
                    break;
                case COVER_EARLY:
                    // kill running jobThread
                    if (jobsThread.isRunningOrHasQueue()) {
                        removeOldReason = "block strategy effect：" + ExecutorBlockStrategyEnum.COVER_EARLY.getTitle();

                        jobsThread = null;
                    }
                    break;
                default:
                    // just queue trigger
                    break;
            }
        }

        // replace thread (new or exists invalid)
        if (null == jobsThread) {
            jobsThread = JobsAbstractExecutor.putJobsThread(triggerParam.getJobId(), jobsHandler, removeOldReason);
        }

        // push data to queue
        return jobsThread.pushTriggerQueue(triggerParam);
    }
}
