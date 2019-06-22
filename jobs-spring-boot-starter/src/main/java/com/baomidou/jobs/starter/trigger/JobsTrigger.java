package com.baomidou.jobs.starter.trigger;

import com.baomidou.jobs.core.JobsConstant;
import com.baomidou.jobs.core.enums.ExecutorBlockStrategyEnum;
import com.baomidou.jobs.core.model.TriggerParam;
import com.baomidou.jobs.core.executor.IJobsExecutor;
import com.baomidou.jobs.core.web.JobsResponse;
import com.baomidou.jobs.starter.JobsHelper;
import com.baomidou.jobs.starter.entity.JobsGroup;
import com.baomidou.jobs.starter.entity.JobsInfo;
import com.baomidou.jobs.starter.entity.JobsLog;
import com.baomidou.jobs.starter.router.ExecutorRouteStrategyEnum;
import com.baomidou.jobs.starter.starter.JobsScheduler;
import com.xxl.rpc.util.IpUtil;
import com.xxl.rpc.util.ThrowableUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * jobs trigger
 * Created by xuxueli on 17/7/13.
 */
@Slf4j
public class JobsTrigger {

    /**
     * trigger job
     *
     * @param jobId
     * @param triggerType
     * @param failRetryCount        >=0: use this param
     *                              <0: use param from job info config
     * @param executorShardingParam
     * @param executorParam         null: use job param
     *                              not null: cover job param
     */
    public static void trigger(int jobId, TriggerTypeEnum triggerType, int failRetryCount, String executorShardingParam, String executorParam) {
        // load data
        JobsInfo jobInfo = JobsHelper.getJobInfoService().getById(jobId);
        if (jobInfo == null) {
            log.warn(">>>>>>>>>>>> trigger fail, jobId invalid，jobId={}", jobId);
            return;
        }
        if (executorParam != null) {
            jobInfo.setExecutorParam(executorParam);
        }
        int finalFailRetryCount = failRetryCount >= 0 ? failRetryCount : jobInfo.getExecutorFailRetryCount();
        JobsGroup group = JobsHelper.getJobGroupService().getById(jobInfo.getJobGroup());

        // sharding param
        int[] shardingParam = null;
        if (executorShardingParam != null) {
            String[] shardingArr = executorShardingParam.split("/");
            if (shardingArr.length == 2 && isNumeric(shardingArr[0]) && isNumeric(shardingArr[1])) {
                shardingParam = new int[2];
                shardingParam[0] = Integer.valueOf(shardingArr[0]);
                shardingParam[1] = Integer.valueOf(shardingArr[1]);
            }
        }
        if (ExecutorRouteStrategyEnum.SHARDING_BROADCAST == ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null)
                && !StringUtils.isEmpty(group.getAddressList())
                && shardingParam == null) {
            int size = group.getAddressList().split(",").length;
            for (int i = 0; i < size; i++) {
                processTrigger(group, jobInfo, finalFailRetryCount, triggerType, i, size);
            }
        } else {
            if (shardingParam == null) {
                shardingParam = new int[]{0, 1};
            }
            processTrigger(group, jobInfo, finalFailRetryCount, triggerType, shardingParam[0], shardingParam[1]);
        }

    }

    private static boolean isNumeric(String str) {
        try {
            int result = Integer.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * @param group               job group, registry list may be empty
     * @param jobInfo
     * @param finalFailRetryCount
     * @param triggerType
     * @param index               sharding index
     * @param total               sharding index
     */
    private static void processTrigger(JobsGroup group, JobsInfo jobInfo, int finalFailRetryCount, TriggerTypeEnum triggerType, int index, int total) {

        // param
        // block strategy
        ExecutorBlockStrategyEnum blockStrategy = ExecutorBlockStrategyEnum.match(jobInfo.getExecutorBlockStrategy(), ExecutorBlockStrategyEnum.SERIAL_EXECUTION);
        // route strategy
        ExecutorRouteStrategyEnum executorRouteStrategyEnum = ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null);
        String shardingParam = (ExecutorRouteStrategyEnum.SHARDING_BROADCAST == executorRouteStrategyEnum) ? String.valueOf(index).concat("/").concat(String.valueOf(total)) : null;

        // 1、save log-id
        JobsLog jobLog = new JobsLog();
        jobLog.setJobGroup(jobInfo.getJobGroup());
        jobLog.setJobId(jobInfo.getId());
        jobLog.setTriggerTime(new Date());
        jobLog.setHandleCode(0);
        jobLog.setTriggerCode(0);
        JobsHelper.getJobLogService().save(jobLog);
        log.debug(">>>>>>>>>>> jobs trigger start, jobId:{}", jobLog.getId());

        // 2、init trigger-param
        TriggerParam triggerParam = new TriggerParam();
        triggerParam.setJobId(jobInfo.getId());
        triggerParam.setExecutorHandler(jobInfo.getExecutorHandler());
        triggerParam.setExecutorParams(jobInfo.getExecutorParam());
        triggerParam.setExecutorBlockStrategy(jobInfo.getExecutorBlockStrategy());
        triggerParam.setExecutorTimeout(jobInfo.getExecutorTimeout());
        triggerParam.setLogId(jobLog.getId());
        triggerParam.setLogDateTim(jobLog.getTriggerTime().getTime());
        triggerParam.setGlueType(jobInfo.getGlueType());
        triggerParam.setGlueSource(jobInfo.getGlueSource());
        triggerParam.setGlueUpdatetime(jobInfo.getGlueUpdatetime().getTime());
        triggerParam.setBroadcastIndex(index);
        triggerParam.setBroadcastTotal(total);

        // 3、init address
        String address = null;
        JobsResponse<String> routeAddressResult = null;
        List<String> registryList = getRegistryList(group.getAddressList());
        if (registryList != null && !registryList.isEmpty()) {
            if (ExecutorRouteStrategyEnum.SHARDING_BROADCAST == executorRouteStrategyEnum) {
                if (index < registryList.size()) {
                    address = registryList.get(index);
                } else {
                    address = registryList.get(0);
                }
            } else {
                routeAddressResult = executorRouteStrategyEnum.getRouter().route(triggerParam, registryList);
                if (routeAddressResult.getCode() == JobsConstant.CODE_SUCCESS) {
                    address = routeAddressResult.getData();
                }
            }
        } else {
            routeAddressResult = JobsResponse.failed("调度失败：执行器地址为空");
        }

        // 4、trigger remote executor
        JobsResponse<String> triggerResult = null;
        if (address != null) {
            triggerResult = runExecutor(triggerParam, address);
        } else {
            triggerResult = JobsResponse.failed("trigger address is null");
        }

        // 5、collection trigger info
        StringBuffer triggerMsgSb = new StringBuffer();
        triggerMsgSb.append("任务触发类型：").append(triggerType.getTitle());
        triggerMsgSb.append("<br>调度机器：").append(IpUtil.getIp());
        triggerMsgSb.append("<br>执行器-注册方式：").append((group.getAddressType() == 0) ? "自动注册" : "手动录入");
        triggerMsgSb.append("<br>执行器-地址列表：").append(getRegistryList(group.getAddressList()));
        triggerMsgSb.append("<br>路由策略：").append(executorRouteStrategyEnum.getTitle());
        if (shardingParam != null) {
            triggerMsgSb.append("(" + shardingParam + ")");
        }
        triggerMsgSb.append("<br>阻塞处理策略：").append(blockStrategy.getTitle());
        triggerMsgSb.append("<br>任务超时时间：").append(jobInfo.getExecutorTimeout());
        triggerMsgSb.append("<br>失败重试次数：").append(finalFailRetryCount);

        triggerMsgSb.append("<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>")
                .append((routeAddressResult != null && routeAddressResult.getMsg() != null) ? routeAddressResult.getMsg() + "<br><br>" : "").append(triggerResult.getMsg() != null ? triggerResult.getMsg() : "");

        // 6、save log trigger-info
        jobLog.setExecutorAddress(address);
        jobLog.setExecutorHandler(jobInfo.getExecutorHandler());
        jobLog.setExecutorParam(jobInfo.getExecutorParam());
        jobLog.setExecutorShardingParam(shardingParam);
        jobLog.setExecutorFailRetryCount(finalFailRetryCount);
        //jobLog.setTriggerTime();
        jobLog.setTriggerCode(triggerResult.getCode());
        jobLog.setTriggerMsg(triggerMsgSb.toString());
        JobsHelper.getJobLogService().updateById(jobLog);

        log.debug(">>>>>>>>>>> jobs trigger end, jobId:{}", jobLog.getId());
    }

    /**
     * run executor
     *
     * @param triggerParam
     * @param address
     * @return
     */
    public static JobsResponse<String> runExecutor(TriggerParam triggerParam, String address) {
        JobsResponse<String> runResult;
        try {
            IJobsExecutor executorBiz = JobsScheduler.getExecutorBiz(address);
            runResult = executorBiz.run(triggerParam);
        } catch (Exception e) {
            log.error(">>>>>>>>>>> jobs trigger error, please check if the executor[{}] is running.", address, e);
            runResult = JobsResponse.failed(ThrowableUtil.toString(e));
        }

        StringBuffer runResultSB = new StringBuffer("触发调度：");
        runResultSB.append("<br>address：").append(address);
        runResultSB.append("<br>code：").append(runResult.getCode());
        runResultSB.append("<br>msg：").append(runResult.getMsg());

        runResult.setMsg(runResultSB.toString());
        return runResult;
    }


    public static List<String> getRegistryList(String addressList) {
        List<String> registryList = null;
        if (!StringUtils.isEmpty(addressList)) {
            registryList = new ArrayList<>(Arrays.asList(addressList.split(",")));
        }
        return registryList;
    }
}
