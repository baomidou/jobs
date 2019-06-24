package com.baomidou.jobs.core.executor.impl;

import com.baomidou.jobs.core.enums.ExecutorBlockStrategyEnum;
import com.baomidou.jobs.core.executor.IJobsExecutor;
import com.baomidou.jobs.core.executor.JobsAbstractExecutor;
import com.baomidou.jobs.core.glue.GlueTypeEnum;
import com.baomidou.jobs.core.glue.IGlueFactory;
import com.baomidou.jobs.core.handler.IJobsHandler;
import com.baomidou.jobs.core.handler.impl.GlueJobsHandler;
import com.baomidou.jobs.core.model.TriggerParam;
import com.baomidou.jobs.core.thread.JobsThread;
import com.baomidou.jobs.core.web.JobsResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Jobs 执行器实现
 *
 * @author xxl jobob
 * @since 2019-06-22
 */
@Slf4j
public class JobsExecutorImpl implements IJobsExecutor {

    @Override
    public JobsResponse<String> beat() {
        return JobsResponse.ok();
    }

    @Override
    public JobsResponse<String> idleBeat(int jobId) {

        // isRunningOrHasQueue
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
    public JobsResponse<String> kill(int jobId) {
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
        JobsThread jobThread = JobsAbstractExecutor.getJobsThread(triggerParam.getJobId());
        IJobsHandler jobHandler = jobThread != null ? jobThread.getHandler() : null;
        String removeOldReason = null;

        // valid：jobHandler + jobThread
        GlueTypeEnum glueTypeEnum = GlueTypeEnum.match(triggerParam.getGlueType());
        if (GlueTypeEnum.BEAN == glueTypeEnum) {

            // new jobhandler
            IJobsHandler newJobHandler = JobsAbstractExecutor.getJobsHandler(triggerParam.getExecutorHandler());

            // valid old jobThread
            if (jobThread != null && jobHandler != newJobHandler) {
                // change handler, need kill old thread
                removeOldReason = "change jobhandler or glue type, and terminate the old job thread.";

                jobThread = null;
                jobHandler = null;
            }

            // valid handler
            if (jobHandler == null) {
                jobHandler = newJobHandler;
                if (jobHandler == null) {
                    return JobsResponse.failed("job handler [" + triggerParam.getExecutorHandler() + "] not found.");
                }
            }

        } else if (GlueTypeEnum.GLUE_GROOVY == glueTypeEnum) {

            // valid old jobThread
            if (jobThread != null &&
                    !(jobThread.getHandler() instanceof GlueJobsHandler
                            && ((GlueJobsHandler) jobThread.getHandler()).getGlueUpdatetime() == triggerParam.getGlueUpdatetime())) {
                // change handler or gluesource updated, need kill old thread
                removeOldReason = "change job source or glue type, and terminate the old job thread.";

                jobThread = null;
                jobHandler = null;
            }

            // valid handler
            if (jobHandler == null) {
                try {
                    IJobsHandler originJobHandler = IGlueFactory.getInstance().loadNewInstance(triggerParam.getGlueSource());
                    jobHandler = new GlueJobsHandler(originJobHandler, triggerParam.getGlueUpdatetime());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    return JobsResponse.failed(e.getMessage());
                }
            }
        } else if (glueTypeEnum != null && glueTypeEnum.isScript()) {

            // 暂不支持
//            // valid old jobThread
//            if (jobThread != null &&
//                    !(jobThread.getHandler() instanceof ScriptJobsHandler
//                            && ((ScriptJobsHandler) jobThread.getHandler()).getGlueUpdatetime() == triggerParam.getGlueUpdatetime())) {
//                // change script or gluesource updated, need kill old thread
//                removeOldReason = "change job source or glue type, and terminate the old job thread.";
//
//                jobThread = null;
//                jobHandler = null;
//            }
//
//            // valid handler
//            if (jobHandler == null) {
//                jobHandler = new ScriptJobsHandler(triggerParam.getJobId(), triggerParam.getGlueUpdatetime(), triggerParam.getGlueSource(), GlueTypeEnum.match(triggerParam.getGlueType()));
//            }
        } else {
            return JobsResponse.failed("glueType[" + triggerParam.getGlueType() + "] is not valid.");
        }

        // executor block strategy
        if (jobThread != null) {
            ExecutorBlockStrategyEnum blockStrategy = ExecutorBlockStrategyEnum.match(triggerParam.getExecutorBlockStrategy(), null);
            if (ExecutorBlockStrategyEnum.DISCARD_LATER == blockStrategy) {
                // discard when running
                if (jobThread.isRunningOrHasQueue()) {
                    return JobsResponse.failed("block strategy effect：" + ExecutorBlockStrategyEnum.DISCARD_LATER.getTitle());
                }
            } else if (ExecutorBlockStrategyEnum.COVER_EARLY == blockStrategy) {
                // kill running jobThread
                if (jobThread.isRunningOrHasQueue()) {
                    removeOldReason = "block strategy effect：" + ExecutorBlockStrategyEnum.COVER_EARLY.getTitle();

                    jobThread = null;
                }
            } else {
                // just queue trigger
            }
        }

        // replace thread (new or exists invalid)
        if (jobThread == null) {
            jobThread = JobsAbstractExecutor.putJobsThread(triggerParam.getJobId(), jobHandler, removeOldReason);
        }

        // push data to queue
        return jobThread.pushTriggerQueue(triggerParam);
    }
}
