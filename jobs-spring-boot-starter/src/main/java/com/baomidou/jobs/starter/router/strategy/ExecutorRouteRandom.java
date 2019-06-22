package com.baomidou.jobs.starter.router.strategy;

import com.baomidou.jobs.core.biz.model.ReturnT;
import com.baomidou.jobs.core.biz.model.TriggerParam;
import com.baomidou.jobs.starter.router.ExecutorRouter;

import java.util.List;
import java.util.Random;

/**
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteRandom extends ExecutorRouter {

    private static Random localRandom = new Random();

    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {
        String address = addressList.get(localRandom.nextInt(addressList.size()));
        return new ReturnT<String>(address);
    }

}
