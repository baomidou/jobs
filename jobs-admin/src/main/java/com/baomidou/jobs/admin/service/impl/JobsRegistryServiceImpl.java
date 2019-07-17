package com.baomidou.jobs.admin.service.impl;

import com.baomidou.jobs.admin.mapper.JobsRegistryMapper;
import com.baomidou.jobs.starter.JobsClock;
import com.baomidou.jobs.starter.model.JobsRegistry;
import com.baomidou.jobs.starter.service.IJobsRegistryService;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobsRegistryServiceImpl implements IJobsRegistryService {
    @Resource
    private JobsRegistryMapper jobRegistryMapper;

    @Override
    public int removeTimeOut(int timeout) {
        return jobRegistryMapper.delete(Wrappers.<JobsRegistry>lambdaQuery().le(
                JobsRegistry::getUpdateTime, JobsClock.currentTimeMillis() - timeout
        ));
    }

    @Override
    public List<String> listAddress(String app) {
        List<JobsRegistry> jobsRegistryList = jobRegistryMapper.selectList(Wrappers.<JobsRegistry>lambdaQuery()
                .eq(JobsRegistry::getApp, app));
        return CollectionUtils.isEmpty(jobsRegistryList) ? null : jobsRegistryList.stream()
                .map(j -> j.getAddress()).collect(Collectors.toList());
    }

    @Override
    public int update(String app, String address) {
        return jobRegistryMapper.update(new JobsRegistry().setUpdateTime(JobsClock.currentTimeMillis()),
                Wrappers.<JobsRegistry>lambdaQuery().eq(JobsRegistry::getApp, app)
                        .eq(JobsRegistry::getAddress, address));
    }

    @Override
    public int save(String app, String address) {
        return jobRegistryMapper.insert(new JobsRegistry().setApp(app)
                .setAddress(address).setUpdateTime(JobsClock.currentTimeMillis()));
    }

    @Override
    public int remove(String app, String address) {
        return jobRegistryMapper.delete(Wrappers.<JobsRegistry>lambdaQuery()
                .eq(JobsRegistry::getApp, app)
                .eq(JobsRegistry::getAddress, address));
    }
}
