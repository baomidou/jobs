package com.baomidou.jobs.admin.controller;

import com.baomidou.jobs.admin.service.IJobsRegistryService;
import com.baomidou.jobs.model.JobsRegistry;
import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public R<Object> page(JobsRegistry jobRegistry) {
        return success(null);//jobRegistryService.page(request, jobRegistry));
    }
}
