package com.baomidou.jobs.starter.mybatisplus.service;

import com.baomidou.jobs.starter.entity.dto.JobsHandleCodeDto;
import com.baomidou.jobs.starter.entity.vo.JobsDateDistributionVO;
import com.baomidou.jobs.starter.entity.vo.JobsImportantNumVO;
import com.baomidou.jobs.starter.entity.vo.JobsSuccessRatioVO;
import com.baomidou.jobs.starter.service.IJobsInfoService;
import com.baomidou.jobs.starter.service.IJobsLogService;
import com.baomidou.jobs.starter.service.IJobsStatisticsService;
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
        List<JobsHandleCodeDto> handleCodeVOList = jobInfoService.getHandleCodeDto();
        if (null == handleCodeVOList || handleCodeVOList.isEmpty()) {
            return null;
        }
        JobsSuccessRatioVO vo = new JobsSuccessRatioVO();
        int size = handleCodeVOList.size();
        for (int i=0; i < size; i++) {
            JobsHandleCodeDto dto = handleCodeVOList.get(i);
            if(0 == dto.getHandleCode()){
                vo.setInProgress(dto.getNum());
            } else if(200 == dto.getHandleCode()){
                vo.setSuccessful(dto.getNum());
            } else if(500 == dto.getHandleCode()){
                vo.setFailed(dto.getNum());
            }
        }
        if(null == vo.getSuccessful()){
            vo.setSuccessful(0);
        }
        if(null == vo.getInProgress()){
            vo.setInProgress(0);
        }
        if(null == vo.getFailed()){
            vo.setFailed(0);
        }
        return vo;
    }

    @Override
    public List<JobsDateDistributionVO> getDateDistribution() {
        return null;
    }
}
