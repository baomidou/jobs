package com.baomidou.jobs.starter.router.strategy;

import com.baomidou.jobs.core.model.TriggerParam;
import com.baomidou.jobs.core.web.JobsResponse;
import com.baomidou.jobs.starter.router.IExecutorRouter;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteRound implements IExecutorRouter {

    private static ConcurrentHashMap<Integer, Integer> routeCountEachJob = new ConcurrentHashMap<>();
    private static long CACHE_VALID_TIME = 0;

    private static int count(int jobId) {
        // cache clear
        if (System.currentTimeMillis() > CACHE_VALID_TIME) {
            routeCountEachJob.clear();
            CACHE_VALID_TIME = System.currentTimeMillis() + 1000 * 60 * 60 * 24;
        }

        // count++
        Integer count = routeCountEachJob.get(jobId);
        // 初始化时主动Random一次，缓解首次压力
        count = (count == null || count > 1000000) ? (new Random().nextInt(100)) : ++count;
        routeCountEachJob.put(jobId, count);
        return count;
    }

    @Override
    public JobsResponse<String> route(TriggerParam triggerParam, List<String> addressList) {
        String address = addressList.get(count(triggerParam.getJobId()) % addressList.size());
        return JobsResponse.ok(address);
    }
}
