package com.baomidou.jobs.starter.mybatisplus.service;

import com.baomidou.jobs.starter.entity.JobsRegistry;
import com.baomidou.jobs.starter.service.IJobsRegistryService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import com.baomidou.jobs.starter.mybatisplus.mapper.JobsRegistryMapper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class JobsRegistryServiceImpl implements IJobsRegistryService<IPage> {
    @Resource
    private JobsRegistryMapper jobRegistryMapper;

    @Override
    public IPage page(HttpServletRequest request, JobsRegistry jobRegistry) {
        return jobRegistryMapper.selectPage(JobsPageHelper.getPage(request),
                Wrappers.<JobsRegistry>lambdaQuery().setEntity(jobRegistry));
    }

    @Override
    public int removeTimeOut(int timeout) {
        return jobRegistryMapper.deleteTimeOut(timeout);
    }

    @Override
    public int update(String registryGroup, String registryKey, String registryValue) {
        return jobRegistryMapper.update(new JobsRegistry().setUpdateTime(new Date()),
                Wrappers.<JobsRegistry>lambdaQuery()
                        .eq(JobsRegistry::getRegistryGroup, registryGroup)
                        .eq(JobsRegistry::getRegistryKey, registryKey)
                        .eq(JobsRegistry::getRegistryValue, registryValue));
    }

    @Override
    public int save(String registryGroup, String registryKey, String registryValue) {
        return jobRegistryMapper.insert(new JobsRegistry().setRegistryGroup(registryGroup)
                .setRegistryKey(registryKey).setRegistryValue(registryValue)
                .setUpdateTime(new Date()));
    }

    @Override
    public int remove(String registryGroup, String registryKey, String registryValue) {
        return jobRegistryMapper.delete(Wrappers.<JobsRegistry>lambdaQuery()
                .eq(JobsRegistry::getRegistryGroup, registryGroup)
                .eq(JobsRegistry::getRegistryKey, registryKey)
                .eq(JobsRegistry::getRegistryValue, registryValue));
    }

    @Override
    public List<JobsRegistry> listTimeout(int timeout) {
        return jobRegistryMapper.selectTimeout(timeout);
    }
}
