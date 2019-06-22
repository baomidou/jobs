package com.baomidou.jobs.starter.router.strategy;

import com.baomidou.jobs.core.JobsConstant;
import com.baomidou.jobs.core.model.TriggerParam;
import com.baomidou.jobs.core.runner.IJobsRunner;
import com.baomidou.jobs.core.web.JobsResponse;
import com.baomidou.jobs.starter.router.ExecutorRouter;
import com.baomidou.jobs.starter.starter.JobsScheduler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Created by xuxueli on 17/3/10.
 */
@Slf4j
public class ExecutorRouteBusyover extends ExecutorRouter {

    @Override
    public JobsResponse<String> route(TriggerParam triggerParam, List<String> addressList) {
        StringBuffer idleBeatResultSB = new StringBuffer();
        for (String address : addressList) {
            // beat
            JobsResponse<String> idleBeatResult;
            try {
                IJobsRunner executorBiz = JobsScheduler.getExecutorBiz(address);
                idleBeatResult = executorBiz.idleBeat(triggerParam.getJobId());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                idleBeatResult = JobsResponse.failed(e.getMessage());
            }
            idleBeatResultSB.append( (idleBeatResultSB.length()>0)?"<br><br>":"")
                    .append("空闲检测：")
                    .append("<br>address：").append(address)
                    .append("<br>code：").append(idleBeatResult.getCode())
                    .append("<br>msg：").append(idleBeatResult.getMsg());

            // beat success
            if (idleBeatResult.getCode() == JobsConstant.CODE_SUCCESS) {
                idleBeatResult.setMsg(idleBeatResultSB.toString());
                idleBeatResult.setData(address);
                return idleBeatResult;
            }
        }
        return JobsResponse.failed(idleBeatResultSB.toString());
    }
}
