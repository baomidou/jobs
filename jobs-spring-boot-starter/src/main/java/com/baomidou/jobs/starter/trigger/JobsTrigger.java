package com.baomidou.jobs.starter.trigger;

import com.baomidou.jobs.starter.JobsClock;
import com.baomidou.jobs.starter.JobsConstant;
import com.baomidou.jobs.starter.JobsHelper;
import com.baomidou.jobs.starter.api.JobsResponse;
import com.baomidou.jobs.starter.executor.IJobsExecutor;
import com.baomidou.jobs.starter.handler.IJobsAlarmHandler;
import com.baomidou.jobs.starter.model.JobsInfo;
import com.baomidou.jobs.starter.model.JobsLog;
import com.baomidou.jobs.starter.model.param.TriggerParam;
import com.baomidou.jobs.starter.router.ExecutorRouteStrategyEnum;
import com.baomidou.jobs.starter.starter.JobsScheduler;
import com.xxl.rpc.util.IpUtil;
import com.xxl.rpc.util.ThrowableUtil;
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
        JobsInfo jobsInfo = JobsHelper.getJobInfoService().getById(jobId);
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
        // route strategy
        ExecutorRouteStrategyEnum executorRouteStrategyEnum = ExecutorRouteStrategyEnum.match(jobsInfo.getRouteStrategy(), null);

        // 1、save log-id
        JobsLog jobLog = new JobsLog();
        jobLog.setJobId(jobsInfo.getId());
        jobLog.setCreateTime(JobsClock.currentTimeMillis());
        JobsHelper.getJobLogService().save(jobLog);
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
        String routeAddressResultMsg = "";
        String address = null;
        List<String> registryList = JobsHelper.getJobRegistryService().listAddress(jobsInfo.getApp());
        if (null != registryList) {
            JobsResponse<String> routeAddressResult = executorRouteStrategyEnum.getRouter()
                    .route(triggerParam, registryList);
            if (routeAddressResult.getCode() == JobsConstant.CODE_SUCCESS) {
                address = routeAddressResult.getData();
            }
            routeAddressResultMsg = routeAddressResult.getMsg();
        }

        // 4、trigger remote executor
        JobsResponse<String> triggerResult;
        if (address != null) {
            triggerResult = runExecutor(triggerParam, address);
            /**
             * 调度失败、触发报警处理器
             */
            if (triggerResult.getCode() == JobsConstant.CODE_FAILED) {
                IJobsAlarmHandler jobsAlarmHandler = JobsHelper.getJobsAlarmHandler();
                if (null != jobsAlarmHandler) {
                    jobsAlarmHandler.failed(jobsInfo, address, triggerResult);
                }
            }
        } else {
            triggerResult = JobsResponse.failed("Trigger address is null");
        }

        // 5、collection trigger info
        StringBuffer triggerMsgSb = new StringBuffer();
        triggerMsgSb.append("任务触发类型：").append(triggerType.getTitle());
        triggerMsgSb.append(",调度机器：").append(IpUtil.getIp());
        triggerMsgSb.append(",执行器-地址列表：").append(registryList);
        triggerMsgSb.append(",路由策略：").append(executorRouteStrategyEnum.getTitle());
        triggerMsgSb.append(",任务超时时间：").append(jobsInfo.getTimeout());
        triggerMsgSb.append(",失败重试次数：").append(finalFailRetryCount);
        triggerMsgSb.append(",触发调度：").append(routeAddressResultMsg);
        triggerMsgSb.append(triggerResult.getMsg());

        // 6、save log trigger-info
        jobLog.setExecutorAddress(address);
        jobLog.setExecutorHandler(jobsInfo.getHandler());
        jobLog.setExecutorParam(jobsInfo.getParam());
        jobLog.setExecutorFailRetryCount(finalFailRetryCount);
        jobLog.setTriggerCode(triggerResult.getCode());
        jobLog.setTriggerMsg(triggerMsgSb.toString());
        JobsHelper.getJobLogService().updateById(jobLog);

        log.debug("Jobs trigger end, jobId:{}", jobLog.getId());
        return true;
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
