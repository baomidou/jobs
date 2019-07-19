package com.baomidou.jobs.admin.service.impl;

import com.baomidou.jobs.admin.mapper.JobsLockMapper;
import com.baomidou.jobs.JobsClock;
import com.baomidou.jobs.model.JobsLock;
import com.baomidou.jobs.admin.service.IJobsLockService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class JobsLockServiceImpl implements IJobsLockService {
    @Resource
    private JobsLockMapper jobsLockMapper;

    @Override
    public int insert(String name, String owner) {
        JobsLock jobsLock = new JobsLock();
        jobsLock.setName(name);
        jobsLock.setOwner(owner);
        jobsLock.setCreateTime(JobsClock.currentTimeMillis());
        return jobsLockMapper.insert(jobsLock);
    }

    @Override
    public int delete(String name, String owner) {
        return jobsLockMapper.delete(Wrappers.<JobsLock>lambdaQuery().eq(JobsLock::getName, name)
                .eq(null != owner, JobsLock::getOwner, owner));
    }
}
