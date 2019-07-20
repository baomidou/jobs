package com.baomidou.jobs.admin.service.impl;

import com.baomidou.jobs.admin.service.IJobsInfoService;
import com.baomidou.jobs.admin.service.IJobsLockService;
import com.baomidou.jobs.admin.service.IJobsLogService;
import com.baomidou.jobs.admin.service.IJobsRegistryService;
import com.baomidou.jobs.model.JobsInfo;
import com.baomidou.jobs.model.JobsLog;
import com.baomidou.jobs.model.param.RegistryParam;
import com.baomidou.jobs.service.IJobsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Jobs Admin Impl
 *
 * @author jobob
 * @since 2019-07-12
 */
@Slf4j
@Service
public class JobsServiceImpl implements IJobsService {
    @Autowired
    private IJobsRegistryService jobsRegistryService;
    @Autowired
    private IJobsLockService jobsLockService;
    @Autowired
    private IJobsInfoService jobsInfoService;
    @Autowired
    public IJobsLogService jobsLogService;

    @Override
    public boolean registry(RegistryParam registryParam) {
        int ret = jobsRegistryService.update(registryParam.getApp(), registryParam.getAddress(),
                registryParam.getRegisterStatusEnum().getValue());
        if (ret < 1) {
            ret = jobsRegistryService.save(registryParam.getApp(), registryParam.getAddress(),
                registryParam.getRegisterStatusEnum().getValue());
        }
        return ret > 0;
    }

    @Override
    public List<JobsInfo> getJobsInfoList(long nextTime) {
        return jobsInfoService.listNextTime(nextTime);
    }

    @Override
    public JobsInfo getJobsInfoById(Long id) {
        return jobsInfoService.getById(id);
    }

    @Override
    public boolean updateJobsInfoById(JobsInfo jobsInfo) {
        return jobsInfoService.updateById(jobsInfo);
    }

    @Override
    public boolean tryLock(String name, String owner) {
        return jobsLockService.insert(name, owner) > 0;
    }

    @Override
    public boolean unlock(String name, String owner) {
        return jobsLockService.delete(name, owner) > 0;
    }

    @Override
    public int removeTimeOutApp(int timeout) {
        return jobsRegistryService.removeTimeOut(timeout);
    }

    @Override
    public boolean removeApp(RegistryParam registryParam) {
        return jobsRegistryService.update(registryParam.getApp(), registryParam.getAddress(),
                registryParam.getRegisterStatusEnum().getValue()) > 0;
    }

    @Override
    public List<String> getAppAddressList(String app) {
        return jobsRegistryService.listAddress(app);
    }

    @Override
    public boolean saveOrUpdateLogById(JobsLog jobsLog) {
        if (null == jobsLog) {
            return false;
        }
        if (null == jobsLog.getId()) {
            return jobsLogService.save(jobsLog);
        }
        return jobsLogService.updateById(jobsLog);
    }
}
