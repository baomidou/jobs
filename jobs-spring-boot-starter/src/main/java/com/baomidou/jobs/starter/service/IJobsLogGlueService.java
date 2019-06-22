package com.baomidou.jobs.starter.service;

import com.baomidou.jobs.starter.entity.JobsLogGlue;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

public interface IJobsLogGlueService<P extends Serializable> {

    /**
     * 分页
     *
     * @param request    当前请求
     * @param jobLogGlue 实体对象
     * @return
     */
    P page(HttpServletRequest request, JobsLogGlue jobLogGlue);

    boolean removeById(int id);
}
