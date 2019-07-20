package com.baomidou.jobs.admin.service.impl;

import com.baomidou.jobs.JobsClock;
import com.baomidou.jobs.admin.service.IJobsInfoService;
import com.baomidou.jobs.admin.service.IJobsLockService;
import com.baomidou.jobs.admin.service.IJobsLogService;
import com.baomidou.jobs.admin.service.IJobsRegistryService;
import com.baomidou.jobs.model.JobsInfo;
import com.baomidou.jobs.model.JobsLog;
import com.baomidou.jobs.model.param.HandleCallbackParam;
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
    public boolean callback(List<HandleCallbackParam> handleCallbackParamList) {
        for (HandleCallbackParam handleCallbackParam : handleCallbackParamList) {
            boolean callbackResult = callback(handleCallbackParam);
            log.debug("callback {}, handleCallbackParam={}, callbackResult={}",
                    callbackResult, handleCallbackParam, callbackResult);
        }
        return true;
    }

    private boolean callback(HandleCallbackParam handleCallbackParam) {
        // valid log item
        JobsLog log = jobsLogService.getById(handleCallbackParam.getLogId());
        if (log == null) {
            return false;
        }
        if (log.getTriggerCode() > 0) {
            // avoid repeat callback, trigger child job etc
            return false;
        }

        // success, save log
        log.setHandleTime(JobsClock.currentTimeMillis());
        log.setHandleCode(handleCallbackParam.getExecuteResult().getCode());
        log.setHandleMsg(handleCallbackParam.getExecuteResult().getMsg());
        jobsLogService.updateById(log);
        return true;
    }

    @Override
    public boolean registry(RegistryParam registryParam) {
        int ret = jobsRegistryService.update(registryParam.getApp(), registryParam.getAddress(), 0);
        if (ret < 1) {
            ret = jobsRegistryService.save(registryParam.getApp(), registryParam.getAddress());
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
        return jobsRegistryService.update(registryParam.getApp(),
                registryParam.getAddress(), 1) > 0;
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
