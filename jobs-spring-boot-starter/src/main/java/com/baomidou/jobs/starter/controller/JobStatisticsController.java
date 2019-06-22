package com.baomidou.jobs.starter.controller;

import com.baomidou.jobs.starter.entity.vo.JobsImportantNumVO;
import com.baomidou.jobs.starter.entity.vo.JobsSuccessRatioVO;
import com.baomidou.jobs.starter.service.IJobsStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.jobs.starter.R;

/**
 * 统计信息
 *
 * @author 青苗
 * @since 2019-06-15
 */
@RestController
@RequestMapping("/v1/statistics")
public class JobStatisticsController extends BaseController {
    @Autowired
    private IJobsStatisticsService statisticsService;

    /**
     * 重要参数数量
     */
    @GetMapping("/important-num")
    public R<JobsImportantNumVO> importantNum() {
        return success(statisticsService.getImportantNum());
    }
    /**
     * 成功比例
     */
    @GetMapping("/success-ratio")
    public R<JobsSuccessRatioVO> successRatio() {
        return success(statisticsService.getSuccessRatio());
    }
}
