package com.baomidou.jobs.trigger;

import com.baomidou.jobs.JobsClock;
import com.baomidou.jobs.JobsConstant;
import com.baomidou.jobs.api.JobsResponse;
import com.baomidou.jobs.exception.JobsException;
import com.baomidou.jobs.handler.IJobsAlarmHandler;
import com.baomidou.jobs.model.JobsInfo;
import com.baomidou.jobs.model.JobsLog;
import com.baomidou.jobs.model.param.TriggerParam;
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

        // save log-id
        JobsLog jobLog = new JobsLog();
        jobLog.setJobId(jobsInfo.getId());
        jobLog.setCreateTime(JobsClock.currentTimeMillis());
        log.debug("Jobs trigger start, jobId:{}", jobLog.getId());

        // init trigger-param
        TriggerParam triggerParam = new TriggerParam();
        triggerParam.setJobId(jobsInfo.getId());
        triggerParam.setHandler(jobsInfo.getHandler());
        triggerParam.setParam(jobsInfo.getParam());
        triggerParam.setTimeout(jobsInfo.getTimeout());

        // trigger remote executor
        int actualRetryCount = 0;
        JobsResponse<String> triggerResult;
        List<String> registryList = jobsService.getAppAddressList(jobsInfo.getApp());
        if (null != registryList && 0 != registryList.size()) {
            // 路由选举执行地址
            String address = JobsHelper.getJobsExecutorRouter().route(jobsInfo.getApp(), registryList);
            jobLog.setAddress(address);
            triggerResult = runExecutor(triggerParam, address, registryList,
                    finalFailRetryCount, actualRetryCount);
            /**
             * 调度失败、触发报警处理器
             */
            if (triggerResult.getCode() == JobsConstant.CODE_FAILED) {
                jobsAlarmHandler(jobsInfo, address, triggerResult);
            }
        } else {
            triggerResult = JobsResponse.failed("Trigger address is null");
            jobsAlarmHandler(jobsInfo, null, triggerResult);
        }

        // save log trigger-info
        jobLog.setHandler(jobsInfo.getHandler());
        jobLog.setParam(jobsInfo.getParam());
        jobLog.setFailRetryCount(actualRetryCount);
        jobLog.setTriggerCode(triggerResult.getCode());
        jobLog.setTriggerType(triggerType.getTitle());
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
     * @param triggerParam     触发执行参数
     * @param address          路由调度地址
     * @param registryList     注册节点地址列表
     * @param failRetryCount   失败重试次数
     * @param actualRetryCount 实际重试次数
     * @param address
     * @return
     */
    public static JobsResponse<String> runExecutor(TriggerParam triggerParam, String address, List<String> registryList,
                                                   int failRetryCount, int actualRetryCount) {
        JobsResponse<String> jobsResponse;
        try {
            jobsResponse = JobsScheduler.getJobsExecutor(address).run(triggerParam);
        } catch (JobsException e) {
            log.error("Trigger error, please check if the executor[{}] is running.", address, e);
            jobsResponse = JobsResponse.failed(JobsHelper.getErrorInfo(e));
        }
        if (JobsConstant.CODE_FAILED == jobsResponse.getCode()
                && failRetryCount > 0) {
            int i = 0;
            for (String registry : registryList) {
                if (address.equals(registry)) {
                    break;
                }
                ++i;
            }
            int size = registryList.size();
            for (int j = 0; j < failRetryCount; j++) {
                ++actualRetryCount;
                // 循环调度注册节点
                try {
                    jobsResponse = JobsScheduler.getJobsExecutor(registryList.get(i)).run(triggerParam);
                } catch (JobsException e) {
                    log.error("Trigger error, please check if the executor[{}] is running.", address, e);
                    jobsResponse = JobsResponse.failed(JobsHelper.getErrorInfo(e));
                }
                if (i < size - 1) {
                    ++i;
                } else {
                    i = 0;
                }
                // 执行成功或者最大尝试退出循环
                if (actualRetryCount == failRetryCount ||
                        JobsConstant.CODE_SUCCESS == jobsResponse.getCode()) {
                    break;
                }
            }
        }
        return jobsResponse;
    }


    /**
     * 重试执行
     */
    public static JobsResponse<String> run(TriggerParam triggerParam) throws JobsException {
        System.out.println(triggerParam.toString());
//        throw new JobsException("111");
        return JobsResponse.ok();
    }
}
