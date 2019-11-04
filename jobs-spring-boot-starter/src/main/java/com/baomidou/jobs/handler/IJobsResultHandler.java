package com.baomidou.jobs.handler;

import com.baomidou.jobs.api.JobsResponse;
import com.baomidou.jobs.model.JobsInfo;


/**
 * Job 结果处理器
 *
 * @author jobob
 * @since 2019-07-16
 */
public interface IJobsResultHandler {

    /**
     * 调度结果处理方法
     *
     * @param jobsInfo     任务信息
     * @param address      调度地址
     * @param jobsResponse 异常响应，根据 code 对应类 JobsErrorCode 值确定调度正确失败
     */
    void handle(JobsInfo jobsInfo, String address, JobsResponse jobsResponse);
}
