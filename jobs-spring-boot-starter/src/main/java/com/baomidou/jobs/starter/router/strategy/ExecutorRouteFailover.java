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
public class ExecutorRouteFailover extends ExecutorRouter {

    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {
        StringBuffer beatResultSB = new StringBuffer();
        for (String address : addressList) {
            // beat
            ReturnT<String> beatResult;
            try {
                ExecutorBiz executorBiz = JobsScheduler.getExecutorBiz(address);
                beatResult = executorBiz.beat();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                beatResult = new ReturnT<String>(ReturnT.FAIL_CODE, ""+e );
            }
            beatResultSB.append( (beatResultSB.length()>0)?"<br><br>":"")
                    .append("心跳检测：")
                    .append("<br>address：").append(address)
                    .append("<br>code：").append(beatResult.getCode())
                    .append("<br>msg：").append(beatResult.getMsg());

            // beat success
            if (beatResult.getCode() == ReturnT.SUCCESS_CODE) {

                beatResult.setMsg(beatResultSB.toString());
                beatResult.setContent(address);
                return beatResult;
            }
        }
        return new ReturnT<String>(ReturnT.FAIL_CODE, beatResultSB.toString());

    }
}
