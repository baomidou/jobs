package com.baomidou.jobs.starter.router.strategy;

import com.baomidou.jobs.core.JobsConstant;
import com.baomidou.jobs.core.model.TriggerParam;
import com.baomidou.jobs.core.executor.IJobsExecutor;
import com.baomidou.jobs.core.web.JobsResponse;
import com.baomidou.jobs.starter.router.ExecutorRouter;
import com.baomidou.jobs.starter.starter.JobsScheduler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Created by xuxueli on 17/3/10.
 */
@Slf4j
public class ExecutorRouteFailover extends ExecutorRouter {

    @Override
    public JobsResponse<String> route(TriggerParam triggerParam, List<String> addressList) {
        StringBuffer beatResultSB = new StringBuffer();
        for (String address : addressList) {
            // beat
            JobsResponse<String> beatResult;
            try {
                IJobsExecutor jobsExecutor = JobsScheduler.getJobsExecutor(address);
                beatResult = jobsExecutor.beat();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                beatResult = JobsResponse.failed(e.getMessage());
            }
            beatResultSB.append( (beatResultSB.length()>0)?"<br><br>":"")
                    .append("心跳检测：")
                    .append("<br>address：").append(address)
                    .append("<br>code：").append(beatResult.getCode())
                    .append("<br>msg：").append(beatResult.getMsg());

            // beat success
            if (beatResult.getCode() == JobsConstant.CODE_SUCCESS) {

                beatResult.setMsg(beatResultSB.toString());
                beatResult.setData(address);
                return beatResult;
            }
        }
        return JobsResponse.failed(beatResultSB.toString());
    }
}
