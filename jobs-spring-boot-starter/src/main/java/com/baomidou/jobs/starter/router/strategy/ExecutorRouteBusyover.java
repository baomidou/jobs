package com.baomidou.jobs.starter.router.strategy;

import com.baomidou.jobs.core.biz.ExecutorBiz;
import com.baomidou.jobs.core.biz.model.ReturnT;
import com.baomidou.jobs.core.biz.model.TriggerParam;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.jobs.starter.router.ExecutorRouter;
import com.baomidou.jobs.starter.starter.JobsScheduler;

import java.util.List;

/**
 * Created by xuxueli on 17/3/10.
 */
@Slf4j
public class ExecutorRouteBusyover extends ExecutorRouter {

    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {
        StringBuffer idleBeatResultSB = new StringBuffer();
        for (String address : addressList) {
            // beat
            ReturnT<String> idleBeatResult = null;
            try {
                ExecutorBiz executorBiz = JobsScheduler.getExecutorBiz(address);
                idleBeatResult = executorBiz.idleBeat(triggerParam.getJobId());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                idleBeatResult = new ReturnT<String>(ReturnT.FAIL_CODE, ""+e );
            }
            idleBeatResultSB.append( (idleBeatResultSB.length()>0)?"<br><br>":"")
                    .append("空闲检测：")
                    .append("<br>address：").append(address)
                    .append("<br>code：").append(idleBeatResult.getCode())
                    .append("<br>msg：").append(idleBeatResult.getMsg());

            // beat success
            if (idleBeatResult.getCode() == ReturnT.SUCCESS_CODE) {
                idleBeatResult.setMsg(idleBeatResultSB.toString());
                idleBeatResult.setContent(address);
                return idleBeatResult;
            }
        }

        return new ReturnT<>(ReturnT.FAIL_CODE, idleBeatResultSB.toString());
    }
}
