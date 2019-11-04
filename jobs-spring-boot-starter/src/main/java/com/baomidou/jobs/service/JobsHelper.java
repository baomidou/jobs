package com.baomidou.jobs.service;

import com.baomidou.jobs.disruptor.JobsDisruptorTemplate;
import com.baomidou.jobs.handler.IJobsResultHandler;
import com.baomidou.jobs.router.IJobsExecutorRouter;
import com.baomidou.jobs.rpc.serialize.IJobsRpcSerializer;
import com.baomidou.jobs.starter.JobsProperties;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.ZonedDateTime;

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
    private IJobsRpcSerializer _jobsRpcSerializer;
    @Resource
    private JobsProperties _jobsProperties;
    @Resource
    private JobsDisruptorTemplate _jobsDisruptorTemplate;

    public static CronParser getCronParser() {
        return JOB_HELPER._cronParser;
    }

    /**
     * CRON 表达式校验
     *
     * @param expression CRON 表达式
     * @return
     */
    public static boolean cronValidate(final String expression) {
        try {
            getCronParser().parse(expression).validate();
        } catch (Throwable t) {
            // 非法
            return false;
        }
        // 合法
        return true;
    }

    /**
     * CRON 表达式下次执行时间
     *
     * @param expression CRON 表达式
     * @return
     */
    public static long cronNextTime(final String expression) {
        ExecutionTime executionTime = ExecutionTime.forCron(getCronParser().parse(expression));
        return executionTime.nextExecution(ZonedDateTime.now()).get().toInstant().toEpochMilli();
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

    public static IJobsRpcSerializer getJobsRpcSerializer() {
        return JOB_HELPER._jobsRpcSerializer;
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
