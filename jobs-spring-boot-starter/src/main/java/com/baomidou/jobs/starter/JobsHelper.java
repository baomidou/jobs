package com.baomidou.jobs.starter;

import com.baomidou.jobs.starter.disruptor.JobsDisruptorTemplate;
import com.baomidou.jobs.starter.handler.IJobsAlarmHandler;
import com.baomidou.jobs.starter.service.*;
import com.baomidou.jobs.starter.starter.JobsProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.io.StringWriter;

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
    private IJobsAlarmHandler _jobsAlarmHandler;
    @Resource
    private JobsProperties _jobProperties;
    @Resource
    private IJobsAdminService _jobsAdminService;
    @Resource
    private IJobsLockService _jobsLockService;
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

    public static IJobsAlarmHandler getJobsAlarmHandler() {
        return JOB_HELPER._jobsAlarmHandler;
    }

    public static IJobsAdminService getJobsAdminService() {
        return JOB_HELPER._jobsAdminService;
    }

    public static IJobsLockService getJobsLockService() {
        return JOB_HELPER._jobsLockService;
    }

    public static JobsDisruptorTemplate getJobsDisruptorTemplate() {
        return JOB_HELPER._jobsDisruptorTemplate;
    }

    public static String getErrorInfo(Exception e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String str = sw.toString();
            sw.close();
            pw.close();
            return str;
        } catch (Exception ex) {
            return "获得Exception信息的工具类异常";
        }
    }
}
