package com.baomidou.jobs.admin.controller;

import com.baomidou.jobs.admin.service.IJobsLogService;
import com.baomidou.jobs.api.JobsResponse;
import com.baomidou.jobs.model.JobsLog;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日志信息
 *
 * @author jobob
 * @since 2019-05-31
 */
@RestController
@RequestMapping("/v1/jobs-log")
public class JobsLogController extends BaseController {
    @Autowired
    private IJobsLogService jobsInfoService;

    /**
     * 分页
     */
    @GetMapping("/page")
    public R<IPage<JobsLog>> page(JobsLog jobsInfo) {
        return jobsInfoService.page(request, jobsInfo);
    }

    /**
     * 总执行次数
     */
    @GetMapping("/count")
    public R<Integer> count() {
        return success(jobsInfoService.countAll());
    }

    /**
     * 总执行成功次数
     */
    @GetMapping("/count-success")
    public R<Integer> countSuccess() {
        return success(jobsInfoService.countSuccess());
    }
}
