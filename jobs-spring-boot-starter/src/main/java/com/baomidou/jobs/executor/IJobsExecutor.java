package com.baomidou.jobs.executor;

import com.baomidou.jobs.model.param.TriggerParam;
import com.baomidou.jobs.api.JobsResponse;

/**
 * Jobs executor interface
 * 
 * @author jobob
 * @since 2019-07-22
 */
public interface IJobsExecutor {

    /**
     * 调度运行
     *
     * @param triggerParam 触发参数
     * @return
     */
    JobsResponse<String> run(TriggerParam triggerParam);

}
