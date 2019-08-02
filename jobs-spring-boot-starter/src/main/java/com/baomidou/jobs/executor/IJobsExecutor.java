package com.baomidou.jobs.executor;

import com.baomidou.jobs.api.JobsResponse;
import com.baomidou.jobs.exception.JobsException;
import com.baomidou.jobs.model.param.TriggerParam;

/**
 * Jobs 执行器
 *
 * @author jobob
 * @since 2019-07-13
 */
public interface IJobsExecutor {

    /**
     * 执行任务调度
     *
     * @param triggerParam 触发参数
     * @return JobsResponse<String>
     * @throws JobsException
     */
    JobsResponse run(TriggerParam triggerParam) throws JobsException;
}
