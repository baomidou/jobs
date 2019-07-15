package com.baomidou.jobs.admin.service;

import com.baomidou.jobs.starter.JobsClock;
import com.baomidou.jobs.starter.lock.IJobsLock;
import com.baomidou.jobs.starter.entity.JobsLock;
import com.baomidou.jobs.admin.mapper.JobsLockMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class JobsLockImpl implements IJobsLock {
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
        return jobsLockMapper.delete(Wrappers.<JobsLock>lambdaQuery()
                .eq(JobsLock::getName, name).eq(JobsLock::getOwner, owner));
    }
}
