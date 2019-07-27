package com.baomidou.jobs.admin.service.impl;

import com.baomidou.jobs.admin.service.IJobsInfoService;
import com.baomidou.jobs.admin.service.IJobsLogService;
import com.baomidou.jobs.admin.service.IJobsRegistryService;
import com.baomidou.jobs.admin.service.IJobsStatisticsService;
import com.baomidou.jobs.admin.service.vo.JobsDateDistributionVO;
import com.baomidou.jobs.admin.service.vo.JobsImportantNumVO;
import com.baomidou.jobs.admin.service.vo.JobsSuccessRatioVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobsStatisticsServiceImpl implements IJobsStatisticsService {
    @Autowired
    private IJobsInfoService jobsInfoService;
    @Autowired
    private IJobsLogService jobsLogService;
    @Autowired
    private IJobsRegistryService jobsRegistryService;

    @Override
    public JobsImportantNumVO getImportantNum() {
        JobsImportantNumVO vo = new JobsImportantNumVO();
        vo.setRunTaskNum(jobsLogService.countAll());
        vo.setTriggeredNum(jobsLogService.countSuccess());
        vo.setOnlineExecutorNum(jobsRegistryService.countOnline());
        return vo;
    }

    @Override
    public JobsSuccessRatioVO getSuccessRatio() {
        return null;
    }

    @Override
    public List<JobsDateDistributionVO> getDateDistribution() {
        return null;
    }
}
