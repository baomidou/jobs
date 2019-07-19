package com.baomidou.jobs.admin.controller;

import com.baomidou.jobs.admin.service.vo.JobsImportantNumVO;
import com.baomidou.jobs.admin.service.vo.JobsSuccessRatioVO;
import com.baomidou.jobs.admin.service.IJobsStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.jobs.api.JobsResponse;

/**
 * 统计信息
 *
 * @author xxl jobob
 * @since 2019-06-15
 */
@RestController
@RequestMapping("/v1/jobs-statistics")
public class JobsStatisticsController extends BaseController {
    @Autowired
    private IJobsStatisticsService statisticsService;

    /**
     * 重要参数数量
     */
    @GetMapping("/important-num")
    public JobsResponse<JobsImportantNumVO> importantNum() {
        return success(statisticsService.getImportantNum());
    }
    /**
     * 成功比例
     */
    @GetMapping("/success-ratio")
    public JobsResponse<JobsSuccessRatioVO> successRatio() {
        return success(statisticsService.getSuccessRatio());
    }
}
