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
    private IJobsInfoService _jobsInfoService;
    @Resource
    private IJobsLogService _jobsLogService;
    @Resource
    private IJobsRegistryService _jobsRegistryService;
    @Resource
    private IJobsAlarmHandler _jobsAlarmHandler;
    @Resource
    private JobsProperties _jobsProperties;
    @Resource
    private IJobsAdminService _jobsAdminService;
    @Resource
    private IJobsLockService _jobsLockService;
    @Resource
    private JobsDisruptorTemplate _jobsDisruptorTemplate;

    public static JobsProperties getJobsProperties() {
        return JOB_HELPER._jobsProperties;
    }

    public static IJobsInfoService getJobsInfoService() {
        return JOB_HELPER._jobsInfoService;
    }

    public static IJobsLogService getJobsLogService() {
        return JOB_HELPER._jobsLogService;
    }

    public static IJobsRegistryService getJobsRegistryService() {
        return JOB_HELPER._jobsRegistryService;
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
