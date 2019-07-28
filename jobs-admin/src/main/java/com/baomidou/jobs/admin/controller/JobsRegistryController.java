package com.baomidou.jobs.admin.controller;

import com.baomidou.jobs.admin.service.IJobsRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.jobs.api.JobsResponse;
import com.baomidou.jobs.model.JobsRegistry;

/**
 * 用户信息
 *
 * @author jobob
 * @since 2019-05-31
 */
@RestController
@RequestMapping("/v1/jobs-registry")
public class JobsRegistryController extends BaseController {
    @Autowired
    private IJobsRegistryService jobRegistryService;

    /**
     * 分页
     */
    @GetMapping("/page")
    public JobsResponse<Object> page(JobsRegistry jobRegistry) {
        return success(null);//jobRegistryService.page(request, jobRegistry));
    }
}
