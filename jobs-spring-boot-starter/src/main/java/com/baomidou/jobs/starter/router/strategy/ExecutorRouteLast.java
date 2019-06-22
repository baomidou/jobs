package com.baomidou.jobs.starter.router.strategy;

import com.baomidou.jobs.core.biz.model.ReturnT;
import com.baomidou.jobs.core.biz.model.TriggerParam;
import com.baomidou.jobs.starter.router.ExecutorRouter;

import java.util.List;

/**
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteLast extends ExecutorRouter {

    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {
        return new ReturnT<String>(addressList.get(addressList.size()-1));
    }

}
