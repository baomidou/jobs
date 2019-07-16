package com.baomidou.jobs.starter.service;

import com.baomidou.jobs.starter.model.JobsLog;

public interface IJobsLogService {

    /**
     * 任务执行总数
     *
     * @return
     */
    int countAll();

    /**
     * 执行成功日志记录总数
     */
    int countSuccess();

    JobsLog getById(Long id);

    boolean updateById(JobsLog jobLog);

    boolean save(JobsLog jobLog);

    boolean removeById(Long id);
}
