package com.baomidou.jobs.admin.service.impl;

import com.baomidou.jobs.admin.mapper.JobsLogMapper;
import com.baomidou.jobs.admin.service.IJobsLogService;
import com.baomidou.jobs.admin.service.JobsPageHelper;
import com.baomidou.jobs.admin.service.vo.JobsDateDistributionVO;
import com.baomidou.jobs.admin.service.vo.JobsDateTempVO;
import com.baomidou.jobs.api.JobsResponse;
import com.baomidou.jobs.model.JobsLog;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class JobsLogServiceImpl implements IJobsLogService {
    @Resource
    private JobsLogMapper jobsLogMapper;

    @Override
    public int countAll() {
        return jobsLogMapper.selectCount(null);
    }

    @Override
    public int countSuccess() {
        return jobsLogMapper.selectCount(Wrappers.<JobsLog>lambdaQuery()
                .eq(JobsLog::getTriggerCode, 0));
    }

    @Override
    public JobsLog getById(Long id) {
        return jobsLogMapper.selectById(id);
    }

    @Override
    public boolean updateById(JobsLog jobsLog) {
        return jobsLogMapper.updateById(jobsLog) > 0;
    }

    @Override
    public boolean save(JobsLog jobsLog) {
        return jobsLogMapper.insert(jobsLog) > 0;
    }

    @Override
    public boolean removeById(Long id) {
        return jobsLogMapper.deleteById(id) > 0;
    }

    @Override
    public R<IPage<JobsLog>> page(HttpServletRequest request, JobsLog jobsLog) {
        return R.ok(jobsLogMapper.selectPage(
                JobsPageHelper.getPage(request), Wrappers.query(jobsLog)
        ));
    }

    @Override
    public List<JobsDateDistributionVO> getJobsDateDistributionVO() {
        List<JobsDateTempVO> tempVOList = jobsLogMapper.selectJobsDateTempVO();
        if (CollectionUtils.isEmpty(tempVOList)) {
            return null;
        }
        List<JobsDateDistributionVO> voList = new ArrayList<>();
        for (JobsDateTempVO tempVO : tempVOList) {
            JobsDateDistributionVO vo = new JobsDateDistributionVO();
            if (0 == tempVO.getCode()) {
                vo.setSuccessful(tempVO.getNum());
                vo.setFailed(0);
            } else {
                vo.setSuccessful(0);
                vo.setFailed(tempVO.getNum());
            }
            vo.setAtDate(tempVO.getAtDate());
            voList.add(vo);
        }
        return voList;
    }
}