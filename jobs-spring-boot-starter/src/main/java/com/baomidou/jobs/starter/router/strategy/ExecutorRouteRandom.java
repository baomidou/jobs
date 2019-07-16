package com.baomidou.jobs.starter.router.strategy;

import com.baomidou.jobs.starter.model.param.TriggerParam;
import com.baomidou.jobs.starter.api.JobsResponse;
import com.baomidou.jobs.starter.router.IExecutorRouter;

import java.util.List;
import java.util.Random;

/**
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteRandom implements IExecutorRouter {

    private static Random localRandom = new Random();

    @Override
    public JobsResponse<String> route(TriggerParam triggerParam, List<String> addressList) {
        return JobsResponse.ok(addressList.get(localRandom.nextInt(addressList.size())));
    }
}
