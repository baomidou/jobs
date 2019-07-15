package com.baomidou.jobs.starter.service;

import com.baomidou.jobs.starter.entity.JobsLog;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IJobsLogService<P> {

    /**
     * 分页
     *
     * @param request 当前请求
     * @param jobLog  实体对象
     * @return
     */
    P page(HttpServletRequest request, JobsLog jobLog);

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
