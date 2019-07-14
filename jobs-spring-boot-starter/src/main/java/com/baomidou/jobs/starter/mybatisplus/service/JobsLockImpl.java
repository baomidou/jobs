package com.baomidou.jobs.starter.mybatisplus.service;

import com.baomidou.jobs.core.JobsClock;
import com.baomidou.jobs.core.lock.IJobsLock;
import com.baomidou.jobs.starter.entity.JobsLock;
import com.baomidou.jobs.starter.mybatisplus.mapper.JobsLockMapper;
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
        jobsLock.setCreateTime(JobsClock.now());
        return jobsLockMapper.insert(jobsLock);
    }

    @Override
    public int delete(String name, String owner) {
        return jobsLockMapper.delete(Wrappers.<JobsLock>lambdaQuery()
                .eq(JobsLock::getName, name).eq(JobsLock::getOwner, owner));
    }
}
