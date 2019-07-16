package com.baomidou.jobs.starter.router.strategy;

import com.baomidou.jobs.starter.model.param.TriggerParam;
import com.baomidou.jobs.starter.api.JobsResponse;
import com.baomidou.jobs.starter.router.IExecutorRouter;

import java.util.List;

/**
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteLast implements IExecutorRouter {

    @Override
    public JobsResponse<String> route(TriggerParam triggerParam, List<String> addressList) {
        return JobsResponse.ok(addressList.get(addressList.size() - 1));
    }
}
