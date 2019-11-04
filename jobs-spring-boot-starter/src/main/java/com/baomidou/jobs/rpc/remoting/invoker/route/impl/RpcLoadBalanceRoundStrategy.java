package com.baomidou.jobs.rpc.remoting.invoker.route.impl;

import com.baomidou.jobs.rpc.remoting.invoker.route.JobsRpcLoadBalance;

import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * round
 *
 * @author xuxueli 2018-12-04
 */
public class RpcLoadBalanceRoundStrategy extends JobsRpcLoadBalance {
    private ConcurrentHashMap<String, Integer> routeCountEachJob = new ConcurrentHashMap<String, Integer>();
    private long CACHE_VALID_TIME = 0;
    private int count(String serviceKey) {
        // cache clear
        if (System.currentTimeMillis() > CACHE_VALID_TIME) {
            routeCountEachJob.clear();
            CACHE_VALID_TIME = System.currentTimeMillis() + 24*60*60*1000;
        }

        // count++
        Integer count = routeCountEachJob.get(serviceKey);
        // 初始化时主动Random一次，缓解首次压力
        count = (count==null || count>1000000)?(new Random().nextInt(100)):++count;
        routeCountEachJob.put(serviceKey, count);
        return count;
    }

    @Override
    public String route(String serviceKey, TreeSet<String> addressSet) {
        // arr
        String[] addressArr = addressSet.toArray(new String[addressSet.size()]);

        // round
        String finalAddress = addressArr[count(serviceKey)%addressArr.length];
        return finalAddress;
    }

}
