package com.baomidou.jobs.trigger;

import com.baomidou.jobs.JobsClock;
import com.baomidou.jobs.JobsConstant;
import com.baomidou.jobs.api.JobsResponse;
import com.baomidou.jobs.executor.IJobsExecutor;
import com.baomidou.jobs.handler.IJobsAlarmHandler;
import com.baomidou.jobs.model.JobsInfo;
import com.baomidou.jobs.model.JobsLog;
import com.baomidou.jobs.model.param.TriggerParam;
import com.baomidou.jobs.rpc.util.ThrowableUtil;
import com.baomidou.jobs.service.IJobsService;
import com.baomidou.jobs.service.JobsHelper;
import com.baomidou.jobs.starter.JobsScheduler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * jobs trigger
 *
 * @author jobob
 * @since 2019-07-15
 */
@Slf4j
public class JobsTrigger {

    /**
     * trigger job
     *
     * @param jobId
     * @param triggerType
     * @param failRetryCount >=0: use this param
     *                       <0: use param from job info config
     * @param executorParam  null: use job param
     *                       not null: cover job param
     */
    public static boolean trigger(Long jobId, TriggerTypeEnum triggerType, int failRetryCount, String executorParam) {
        // load data
        JobsInfo jobsInfo = JobsHelper.getJobsService().getJobsInfoById(jobId);
        if (jobsInfo == null) {
            log.warn("Trigger fail, jobId invalid，jobId={}", jobId);
            return false;
        }
        if (executorParam != null) {
            jobsInfo.setParam(executorParam);
        }
        int finalFailRetryCount = failRetryCount >= 0 ? failRetryCount : jobsInfo.getFailRetryCount();
        return processTrigger(jobsInfo, finalFailRetryCount, triggerType);
    }

    /**
     * 执行触发任务
     *
     * @param jobsInfo
     * @param finalFailRetryCount
     * @param triggerType
     */
    private static boolean processTrigger(JobsInfo jobsInfo, int finalFailRetryCount, TriggerTypeEnum triggerType) {
        IJobsService jobsService = JobsHelper.getJobsService();

        // 1、save log-id
        JobsLog jobLog = new JobsLog();
        jobLog.setJobId(jobsInfo.getId());
        jobLog.setCreateTime(JobsClock.currentTimeMillis());
        jobsService.saveOrUpdateLogById(jobLog);
        log.debug("Jobs trigger start, jobId:{}", jobLog.getId());

        // 2、init trigger-param
        TriggerParam triggerParam = new TriggerParam();
        triggerParam.setJobId(jobsInfo.getId());
        triggerParam.setExecutorHandler(jobsInfo.getHandler());
        triggerParam.setExecutorParams(jobsInfo.getParam());
        triggerParam.setExecutorTimeout(jobsInfo.getTimeout());
        triggerParam.setLogId(jobLog.getId());
        triggerParam.setLogDateTime(JobsClock.currentTimeMillis());

        // 3、init address
        String address = null;
        List<String> registryList = jobsService.getAppAddressList(jobsInfo.getApp());
        if (null != registryList) {
            address = JobsHelper.getJobsExecutorRouter().route(jobsInfo.getApp(), registryList);
        }

        // 4、trigger remote executor
        JobsResponse<String> triggerResult;
        if (address != null) {
            jobLog.setTriggerTime(JobsClock.currentTimeMillis());
            triggerResult = runExecutor(triggerParam, address);
            /**
             * 调度失败、触发报警处理器
             */
            if (triggerResult.getCode() == JobsConstant.CODE_FAILED) {
                jobsAlarmHandler(jobsInfo, address, triggerResult);
            }
        } else {
            triggerResult = JobsResponse.failed("Trigger address is null");
            jobsAlarmHandler(jobsInfo, address, triggerResult);
        }

        // 5、save log trigger-info
        jobLog.setExecutorAddress(address);
        jobLog.setExecutorHandler(jobsInfo.getHandler());
        jobLog.setExecutorParam(jobsInfo.getParam());
        jobLog.setExecutorFailRetryCount(finalFailRetryCount);
        jobLog.setTriggerCode(triggerResult.getCode());
        jobLog.setTriggerMsg(triggerResult.getMsg());
        jobsService.saveOrUpdateLogById(jobLog);

        log.debug("Jobs trigger end, jobId:{}", jobLog.getId());
        return true;
    }

    public static void jobsAlarmHandler(JobsInfo jobsInfo, String address, JobsResponse<String> jobsResponse) {
        IJobsAlarmHandler jobsAlarmHandler = JobsHelper.getJobsAlarmHandler();
        if (null != jobsAlarmHandler) {
            jobsAlarmHandler.failed(jobsInfo, address, jobsResponse);
        }
    }

    /**
     * run executor
     *
     * @param triggerParam
     * @param address
     * @return
     */
    public static JobsResponse<String> runExecutor(TriggerParam triggerParam, String address) {
        JobsResponse<String> jobsResponse;
        try {
            IJobsExecutor jobsExecutor = JobsScheduler.getJobsExecutor(address);
            jobsResponse = jobsExecutor.run(triggerParam);
        } catch (Exception e) {
            log.error("Trigger error, please check if the executor[{}] is running.", address, e);
            jobsResponse = JobsResponse.failed(ThrowableUtil.toString(e));
        }
        return jobsResponse;
    }
}
