package com.baomidou.jobs.handler;

import com.baomidou.jobs.api.JobsResponse;
import com.baomidou.jobs.exception.JobsException;

/**
 * job handler interface
 *
 * @author jobob
 * @since 2019-07-20
 */
public interface IJobsHandler {


    /**
     * 任务调度执行方法
     *
     * @param tenantId 租户ID
     * @param param    执行参数
     * @return
     * @throws JobsException
     */
    JobsResponse execute(String tenantId, String param) throws JobsException;
}
