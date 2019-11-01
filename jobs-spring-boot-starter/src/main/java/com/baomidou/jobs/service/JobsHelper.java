package com.baomidou.jobs.service;

import com.baomidou.jobs.disruptor.JobsDisruptorTemplate;
import com.baomidou.jobs.handler.IJobsResultHandler;
import com.baomidou.jobs.router.IJobsExecutorRouter;
import com.baomidou.jobs.starter.JobsProperties;
import com.cronutils.parser.CronParser;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Spring Boot 相关辅助类
 *
 * @author jobob
 * @since 2019-07-18
 */
@Configuration
public class JobsHelper implements InitializingBean {

    private static JobsHelper JOB_HELPER = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        JOB_HELPER = this;
    }

    @Resource
    private CronParser _cronParser;

    @Resource
    private IJobsService _jobsService;
    @Resource
    private IJobsExecutorRouter _jobsExecutorRouter;
    @Resource
    private IJobsResultHandler _jobsResultHandler;
    @Resource
    private JobsProperties _jobsProperties;
    @Resource
    private JobsDisruptorTemplate _jobsDisruptorTemplate;

    public static CronParser getCronParser() {
        return JOB_HELPER._cronParser;
    }

    public static IJobsService getJobsService() {
        return JOB_HELPER._jobsService;
    }

    public static IJobsExecutorRouter getJobsExecutorRouter() {
        return JOB_HELPER._jobsExecutorRouter;
    }

    public static IJobsResultHandler getJobsResultHandler() {
        return JOB_HELPER._jobsResultHandler;
    }

    public static JobsProperties getJobsProperties() {
        return JOB_HELPER._jobsProperties;
    }

    public static JobsDisruptorTemplate getJobsDisruptorTemplate() {
        return JOB_HELPER._jobsDisruptorTemplate;
    }

    public static String getErrorInfo(Throwable t) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            String str = sw.toString();
            sw.close();
            pw.close();
            return str;
        } catch (Exception ex) {
            return "获得Exception信息的异常";
        }
    }
}
