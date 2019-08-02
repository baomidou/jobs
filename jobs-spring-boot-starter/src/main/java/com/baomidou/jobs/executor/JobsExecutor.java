package com.baomidou.jobs.executor;

import com.baomidou.jobs.api.JobsResponse;
import com.baomidou.jobs.exception.JobsException;
import com.baomidou.jobs.handler.IJobsHandler;
import com.baomidou.jobs.model.param.TriggerParam;
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
    public JobsResponse run(TriggerParam triggerParam) throws JobsException {
        if (null == triggerParam) {
            throw new JobsException("JobsExecutor execute triggerParam is null");
        }
        IJobsHandler jobsHandler = JobsAbstractExecutor.getJobsHandler(triggerParam.getHandler());
        if (null == jobsHandler) {
            throw new JobsException("JobsExecutor not found execute handler:" + triggerParam.getHandler());
        }
        return jobsHandler.execute(triggerParam.getTenantId(), triggerParam.getParam());
    }
}
