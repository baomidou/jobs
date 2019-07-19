package com.baomidou.jobs.router.strategy;

import com.baomidou.jobs.model.param.TriggerParam;
import com.baomidou.jobs.api.JobsResponse;
import com.baomidou.jobs.router.IExecutorRouter;

import java.util.List;

/**
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteFirst implements IExecutorRouter {

    @Override
    public JobsResponse<String> route(TriggerParam triggerParam, List<String> addressList) {
        return JobsResponse.ok(addressList.get(0));
    }
}
