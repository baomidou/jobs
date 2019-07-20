package com.baomidou.jobs.executor;

import com.baomidou.jobs.api.JobsResponse;
import com.baomidou.jobs.handler.IJobsHandler;
import com.baomidou.jobs.model.param.TriggerParam;
import com.baomidou.jobs.service.JobsHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * Jobs 执行器
 *
 * @author jobob
 * @since 2019-07-13
 */
@Slf4j
public class JobsExecutor implements IJobsExecutor {

    /**
     * 执行任务调度
     *
     * @param triggerParam 触发参数
     * @return
     */
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
