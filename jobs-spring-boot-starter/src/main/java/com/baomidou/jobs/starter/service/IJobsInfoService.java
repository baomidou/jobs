package com.baomidou.jobs.starter.service;

import com.baomidou.jobs.starter.model.JobsInfo;

import java.util.List;

public interface IJobsInfoService {

    /**
     * 执行任务总数
     *
     * @return
     */
    int count();

    List<JobsInfo> scheduleJobQuery(long maxNextTime);

    boolean updateById(JobsInfo jobInfo);

    /**
     * 执行、指定 ID 任务
     *
     * @param id    主键 ID
     * @param param 执行参数
     * @return
     */
    boolean execute(Long id, String param);

    /**
     * 启动、指定 ID 任务
     *
     * @param id 主键 ID
     * @return
     */
    boolean start(Long id);

    /**
     * 停止、指定 ID 任务
     *
     * @param id 主键 ID
     * @return
     */
    boolean stop(Long id);

    /**
     * 删除、指定 ID 任务
     *
     * @param id 主键 ID
     * @return
     */
    boolean remove(Long id);

    JobsInfo getById(Long id);
}
