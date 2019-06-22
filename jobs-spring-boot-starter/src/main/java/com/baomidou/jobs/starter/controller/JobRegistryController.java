package com.baomidou.jobs.starter.controller;

import com.baomidou.jobs.starter.service.IJobsRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.jobs.core.web.JobsResponse;
import com.baomidou.jobs.starter.entity.JobsRegistry;

/**
 * 用户信息
 *
 * @author 青苗
 * @since 2019-05-31
 */
@RestController
@RequestMapping("/v1/job-registry")
public class JobRegistryController extends BaseController {
    @Autowired
    private IJobsRegistryService jobRegistryService;

    /**
     * 分页
     */
    @GetMapping("/page")
    public JobsResponse<Object> page(JobsRegistry jobRegistry) {
        return success(jobRegistryService.page(request, jobRegistry));
    }
}
