package com.baomidou.jobs.admin.service.impl;

import com.baomidou.jobs.admin.mapper.JobsLogMapper;
import com.baomidou.jobs.admin.service.IJobsLogService;
import com.baomidou.jobs.admin.service.JobsPageHelper;
import com.baomidou.jobs.api.JobsResponse;
import com.baomidou.jobs.model.JobsLog;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
    public JobsResponse<IPage<JobsLog>> page(HttpServletRequest request, JobsLog jobsLog) {
        return JobsResponse.ok(jobsLogMapper.selectPage(
                JobsPageHelper.getPage(request), Wrappers.query(jobsLog)
        ));
    }
}