package com.baomidou.jobs.admin.service.impl;

import com.baomidou.jobs.admin.mapper.JobsLogMapper;
import com.baomidou.jobs.starter.model.JobsLog;
import com.baomidou.jobs.admin.service.IJobsLogService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class JobsLogServiceImpl implements IJobsLogService {
    @Resource
    private JobsLogMapper jobLogMapper;

    @Override
    public int countAll() {
        return jobLogMapper.selectCount(null);
    }

    @Override
    public int countSuccess() {
        return jobLogMapper.selectCount(Wrappers.<JobsLog>lambdaQuery()
                .eq(JobsLog::getTriggerCode, 0));
    }

    @Override
    public JobsLog getById(Long id) {
        return jobLogMapper.selectById(id);
    }

    @Override
    public boolean updateById(JobsLog jobLog) {
        return jobLogMapper.updateById(jobLog) > 0;
    }

    @Override
    public boolean save(JobsLog jobLog) {
        return jobLogMapper.insert(jobLog) > 0;
    }

    @Override
    public boolean removeById(Long id) {
        return jobLogMapper.deleteById(id) > 0;
    }
}