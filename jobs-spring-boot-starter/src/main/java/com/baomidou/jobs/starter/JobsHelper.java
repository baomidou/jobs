package com.baomidou.jobs.starter;

import com.baomidou.jobs.starter.lock.IJobsLock;
import com.baomidou.jobs.starter.web.IJobsAdmin;
import com.baomidou.jobs.starter.disruptor.JobsDisruptorTemplate;
import com.baomidou.jobs.starter.handler.IJobsAlarmHandler;
import com.baomidou.jobs.starter.service.IJobsInfoService;
import com.baomidou.jobs.starter.service.IJobsLogService;
import com.baomidou.jobs.starter.service.IJobsRegistryService;
import com.baomidou.jobs.starter.starter.JobsProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * Spring Boot 相关辅助类
 *
 * @author xxl jobob
 * @since 2019-06-08
 */
@Configuration
public class JobsHelper implements InitializingBean {

    private static JobsHelper JOB_HELPER = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        JOB_HELPER = this;
    }

    @Resource
    private IJobsInfoService _jobInfoService;
    @Resource
    private IJobsLogService _jobLogService;
    @Resource
    private IJobsRegistryService _jobRegistryService;
    @Resource
    private IJobsAlarmHandler _jobAlarmHandler;
    @Resource
    private JobsProperties _jobProperties;
    @Resource
    private IJobsAdmin _jobsAdmin;
    @Resource
    private IJobsLock _jobsLock;
    @Resource
    private JobsDisruptorTemplate _jobsDisruptorTemplate;

    public static JobsProperties getJobProperties() {
        return JOB_HELPER._jobProperties;
    }

    public static IJobsInfoService getJobInfoService() {
        return JOB_HELPER._jobInfoService;
    }

    public static IJobsLogService getJobLogService() {
        return JOB_HELPER._jobLogService;
    }

    public static IJobsRegistryService getJobRegistryService() {
        return JOB_HELPER._jobRegistryService;
    }

    public static IJobsAlarmHandler getJobAlarmHandler() {
        return JOB_HELPER._jobAlarmHandler;
    }

    public static IJobsAdmin getJobsAdmin() {
        return JOB_HELPER._jobsAdmin;
    }

    public static IJobsLock getJobsLock() {
        return JOB_HELPER._jobsLock;
    }

    public static JobsDisruptorTemplate getJobsDisruptorTemplate() {
        return JOB_HELPER._jobsDisruptorTemplate;
    }
}
