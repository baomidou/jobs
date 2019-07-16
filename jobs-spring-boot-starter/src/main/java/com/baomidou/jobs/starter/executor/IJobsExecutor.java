package com.baomidou.jobs.starter.executor;

import com.baomidou.jobs.starter.model.param.TriggerParam;
import com.baomidou.jobs.starter.api.JobsResponse;

/**
 * Jobs executor interface
 * 
 * @author xxl jobob
 * @since 2019-06-22
 */
public interface IJobsExecutor {

    /**
     * beat
     *
     * @return
     */
    JobsResponse<String> beat();

    /**
     * idle beat
     *
     * @param jobId
     * @return
     */
    JobsResponse<String> idleBeat(Long jobId);

    /**
     * kill
     *
     * @param jobId
     * @return
     */
    JobsResponse<String> kill(Long jobId);

    /**
     * run
     *
     * @param triggerParam
     * @return
     */
    JobsResponse<String> run(TriggerParam triggerParam);

}
