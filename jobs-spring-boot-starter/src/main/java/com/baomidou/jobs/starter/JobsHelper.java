package com.baomidou.jobs.starter;

import com.baomidou.jobs.core.biz.AdminBiz;
import com.baomidou.jobs.starter.handler.IJobsAlarmHandler;
import com.baomidou.jobs.starter.service.*;
import com.baomidou.jobs.starter.starter.JobsProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Spring Boot 相关辅助类
 *
 * @author 青苗
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
    private IJobsGroupService _jobGroupService;
    @Resource
    private IJobsInfoService _jobInfoService;
    @Resource
    private IJobsLogService _jobLogService;
    @Resource
    private IJobsLogGlueService _jobLogGlueService;
    @Resource
    private IJobsRegistryService _jobRegistryService;
    @Resource
    private IJobsAlarmHandler _jobAlarmHandler;
    @Resource
    private JobsProperties _jobProperties;
    @Resource
    private AdminBiz _adminBiz;
    @Resource
    private DataSource _dataSource;

    public static JobsProperties getJobProperties() {
        return JOB_HELPER._jobProperties;
    }

    public static IJobsGroupService getJobGroupService() {
        return JOB_HELPER._jobGroupService;
    }

    public static IJobsInfoService getJobInfoService() {
        return JOB_HELPER._jobInfoService;
    }

    public static IJobsLogGlueService getJobLogGlueService() {
        return JOB_HELPER._jobLogGlueService;
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

    public static AdminBiz getAdminBiz() {
        return JOB_HELPER._adminBiz;
    }

    public static DataSource getDataSource() {
        return JOB_HELPER._dataSource;
    }
}
