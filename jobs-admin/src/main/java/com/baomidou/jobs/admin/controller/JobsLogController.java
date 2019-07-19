package com.baomidou.jobs.admin.controller;

import com.baomidou.jobs.admin.service.IJobsLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.jobs.api.JobsResponse;
import com.baomidou.jobs.model.JobsLog;

/**
 * 日志信息
 *
 * @author xxl jobob
 * @since 2019-05-31
 */
@RestController
@RequestMapping("/v1/jobs-log")
public class JobsLogController extends BaseController {
    @Autowired
    private IJobsLogService jobLogService;

    /**
     * 分页
     */
    @GetMapping("/page")
    public JobsResponse<Object> page(JobsLog jobLog) {
        return success(null);//jobLogService.page(request, jobLog));
    }

    /**
     * 总执行次数
     */
    @GetMapping("/count")
    public JobsResponse<Integer> count() {
        return success(jobLogService.countAll());
    }

    /**
     * 总执行成功次数
     */
    @GetMapping("/count-success")
    public JobsResponse<Integer> countSuccess() {
        return success(jobLogService.countSuccess());
    }
}
