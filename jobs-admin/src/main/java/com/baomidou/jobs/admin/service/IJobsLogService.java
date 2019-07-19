package com.baomidou.jobs.admin.service;

import com.baomidou.jobs.model.JobsLog;

/**
 * 任务日志接口
 *
 * @author jobob
 * @since 2019-07-18
 */
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

    /**
     *
     * @param id
     * @return
     */
    boolean removeById(Long id);
}
