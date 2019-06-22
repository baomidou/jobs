package com.baomidou.jobs.starter.controller;

import com.baomidou.jobs.core.web.JobsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.jobs.starter.entity.JobsLogGlue;
import com.baomidou.jobs.starter.service.IJobsLogGlueService;

/**
 * 日志信息
 *
 * @author 青苗
 * @since 2019-05-31
 */
@RestController
@RequestMapping("/v1/job-log-glue")
public class JobLogGlueController extends BaseController {
    @Autowired
    private IJobsLogGlueService jobLogGlueService;

    /**
     * 分页
     */
    @GetMapping("/page")
    public JobsResponse<Object> page(JobsLogGlue jobLogGlue) {
        return success(jobLogGlueService.page(request, jobLogGlue));
    }
}
