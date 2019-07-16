package com.baomidou.jobs.admin.service.impl;

import com.baomidou.jobs.admin.service.IJobsStatisticsService;
import com.baomidou.jobs.admin.service.vo.JobsDateDistributionVO;
import com.baomidou.jobs.admin.service.vo.JobsImportantNumVO;
import com.baomidou.jobs.admin.service.vo.JobsSuccessRatioVO;
import com.baomidou.jobs.starter.service.IJobsInfoService;
import com.baomidou.jobs.starter.service.IJobsLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobsStatisticsServiceImpl implements IJobsStatisticsService {
    @Autowired
    private IJobsInfoService jobInfoService;
    @Autowired
    private IJobsLogService jobLogService;

    @Override
    public JobsImportantNumVO getImportantNum() {
        JobsImportantNumVO vo = new JobsImportantNumVO();
        vo.setRunTaskNum(jobLogService.countAll());
        vo.setTriggeredNum(jobLogService.countSuccess());
        int onlineExecutorNum = 0;
//        List<JobsGroup> jobGroupList = jobGroupService.listAll();
//        if (jobGroupList != null && !jobGroupList.isEmpty()) {
//            Set<String> addressSet = new HashSet<>();
//            for (JobsGroup jobGroup : jobGroupList) {
//                String addressList = jobGroup.getAddress();
//                if (null != addressList) {
//                    addressSet.addAll(Arrays.asList(addressList.split(",")));
//                }
//            }
//            onlineExecutorNum = addressSet.size();
//        }
        // TODO 待处理统计
        vo.setOnlineExecutorNum(onlineExecutorNum);
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
