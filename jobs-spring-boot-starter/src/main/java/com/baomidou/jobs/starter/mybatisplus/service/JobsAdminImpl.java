package com.baomidou.jobs.starter.mybatisplus.service;

import com.baomidou.jobs.core.model.HandleCallbackParam;
import com.baomidou.jobs.core.model.RegistryParam;
import com.baomidou.jobs.core.web.IJobsAdmin;
import com.baomidou.jobs.core.web.JobsResponse;
import com.baomidou.jobs.starter.entity.JobsLog;
import com.baomidou.jobs.starter.service.IJobsLogService;
import com.baomidou.jobs.starter.service.IJobsRegistryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Jobs Admin Impl
 *
 * @author jobob
 * @since 2019-07-12
 */
@Slf4j
@Service
public class JobsAdminImpl implements IJobsAdmin {
    @Autowired
    public IJobsLogService jobLogService;
    @Autowired
    private IJobsRegistryService jobRegistryService;

    @Override
    public String lockSql() {
        return "SELECT * FROM jobs_lock WHERE lock_name = 'schedule_lock' FOR UPDATE";
    }

    @Override
    public JobsResponse<Boolean> callback(List<HandleCallbackParam> callbackParamList) {
        for (HandleCallbackParam handleCallbackParam : callbackParamList) {
            JobsResponse<Boolean> callbackResult = callback(handleCallbackParam);
            log.debug("callback {}, handleCallbackParam={}, callbackResult={}",
                    callbackResult.toString(), handleCallbackParam, callbackResult);
        }

        return JobsResponse.ok();
    }

    private JobsResponse<Boolean> callback(HandleCallbackParam handleCallbackParam) {
        // valid log item
        JobsLog log = jobLogService.getById(handleCallbackParam.getLogId());
        if (log == null) {
            return JobsResponse.failed("log item not found.");
        }
        if (log.getHandleCode() > 0) {
            // avoid repeat callback, trigger child job etc
            return JobsResponse.failed("log repeate callback.");
        }

        // handle msg
        StringBuffer handleMsg = new StringBuffer();
        if (log.getHandleMsg() != null) {
            handleMsg.append(log.getHandleMsg()).append("<br>");
        }
        if (handleCallbackParam.getExecuteResult().getMsg() != null) {
            handleMsg.append(handleCallbackParam.getExecuteResult().getMsg());
        }

        // success, save log
        log.setHandleTime(new Date());
        log.setHandleCode(handleCallbackParam.getExecuteResult().getCode());
        log.setHandleMsg(handleMsg.toString());
        jobLogService.updateById(log);
        return JobsResponse.ok();
    }

    @Override
    public JobsResponse<Boolean> registry(RegistryParam registryParam) {
        int ret = jobRegistryService.update(registryParam.getApp(), registryParam.getAddress());
        if (ret < 1) {
            ret = jobRegistryService.save(registryParam.getApp(), registryParam.getAddress());
        }
        return JobsResponse.ok(ret > 0);
    }

    @Override
    public JobsResponse<Boolean> registryRemove(RegistryParam registryParam) {
        jobRegistryService.remove(registryParam.getApp(), registryParam.getAddress());
        return JobsResponse.ok();
    }
}
