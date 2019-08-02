package com.baomidou.jobs.admin.controller;

import com.baomidou.jobs.admin.service.IJobsInfoService;
import com.baomidou.jobs.model.JobsInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 任务信息
 *
 * @author jobob
 * @since 2019-05-31
 */
@RestController
@RequestMapping("/v1/jobs-info")
public class JobsInfoController extends BaseController {
    @Resource
    private IJobsInfoService jobInfoService;

    /**
     * 分页
     */
    @GetMapping("/page")
    public R<IPage<JobsInfo>> page(JobsInfo jobInfo) {
        return jobInfoService.page(request, jobInfo);
    }

    /**
     * 总任务数
     */
    @GetMapping("/count")
    public R<Integer> count() {
        return success(jobInfoService.count());
    }

    /**
     * 执行
     */
    @PostMapping("/execute-{id}")
    public R<Boolean> execute(@PathVariable("id") Long id, String param) {
        return success(jobInfoService.execute(id, param));
    }

    /**
     * 启动
     */
    @PostMapping("/start-{id}")
    public R<Boolean> start(@PathVariable("id") Long id) {
        return success(jobInfoService.start(id));
    }

    /**
     * 停止
     */
    @PostMapping("/stop-{id}")
    public R<Boolean> stop(@PathVariable("id") Long id) {
        return success(jobInfoService.stop(id));
    }

    /**
     * 删除
     */
    @PostMapping("/remove-{id}")
    public R<Boolean> remove(@PathVariable("id") Long id) {
        return success(jobInfoService.remove(id));
    }
}
