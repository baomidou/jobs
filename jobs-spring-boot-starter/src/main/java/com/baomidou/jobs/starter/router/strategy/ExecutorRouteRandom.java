package com.baomidou.jobs.starter.router.strategy;

import com.baomidou.jobs.core.model.TriggerParam;
import com.baomidou.jobs.core.web.JobsResponse;
import com.baomidou.jobs.starter.router.ExecutorRouter;

import java.util.List;
import java.util.Random;

/**
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteRandom extends ExecutorRouter {

    private static Random localRandom = new Random();

    @Override
    public JobsResponse<String> route(TriggerParam triggerParam, List<String> addressList) {
        return JobsResponse.ok(addressList.get(localRandom.nextInt(addressList.size())));
    }
}
