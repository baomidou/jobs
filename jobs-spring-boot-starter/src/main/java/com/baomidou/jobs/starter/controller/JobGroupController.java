package com.baomidou.jobs.starter.controller;

import com.baomidou.jobs.starter.service.IJobsGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.jobs.starter.R;
import com.baomidou.jobs.starter.entity.JobsGroup;

/**
 * 任务组信息
 *
 * @author 青苗
 * @since 2019-05-31
 */
@RestController
@RequestMapping("/v1/job-group")
public class JobGroupController extends BaseController {
    @Autowired
    private IJobsGroupService jobGroupService;

    /**
     * 分页
     */
    @GetMapping("/page")
    public R<Object> page(JobsGroup jobGroup) {
        return success(jobGroupService.page(request, jobGroup));
    }

    /**
     * 删除
     */
    @PostMapping("/remove-{id}")
    public R<Boolean> remove(@PathVariable("id") int id) {
        return success(jobGroupService.remove(id));
    }
}
