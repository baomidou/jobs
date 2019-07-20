package com.baomidou.jobs.handler;

import com.baomidou.jobs.api.JobsResponse;
import com.baomidou.jobs.model.JobsInfo;


/**
 * Job 报警处理器
 *
 * @author jobob
 * @since 2019-07-16
 */
public interface IJobsAlarmHandler {

    /**
     * 调度失败
     *
     * @param jobsInfo     任务信息
     * @param address      调度地址
     * @param jobsResponse 异常响应
     * @return
     */
    boolean failed(JobsInfo jobsInfo, String address, JobsResponse<String> jobsResponse);
}
