package com.baomidou.jobs.starter.executor.impl;

import com.baomidou.jobs.starter.JobsHelper;
import com.baomidou.jobs.starter.api.JobsResponse;
import com.baomidou.jobs.starter.executor.IJobsExecutor;
import com.baomidou.jobs.starter.executor.JobsAbstractExecutor;
import com.baomidou.jobs.starter.handler.IJobsHandler;
import com.baomidou.jobs.starter.model.param.TriggerParam;
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
    public JobsResponse<String> run(TriggerParam triggerParam) {
        IJobsHandler jobsHandler = JobsAbstractExecutor.getJobsHandler(triggerParam.getExecutorHandler());
        try {
            return jobsHandler.execute(triggerParam.getExecutorParams());
        } catch (Exception e) {
            return JobsResponse.failed(JobsHelper.getErrorInfo(e));
        }
    }
}
