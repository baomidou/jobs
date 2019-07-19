package com.baomidou.jobs.rpc.remoting.invoker.route;

import com.baomidou.jobs.rpc.remoting.invoker.route.impl.*;

/**
 * @author xuxueli 2018-12-04
 */
public enum LoadBalance {
    RANDOM(new XxlRpcLoadBalanceRandomStrategy()),
    ROUND(new XxlRpcLoadBalanceRoundStrategy()),
    LRU(new XxlRpcLoadBalanceLRUStrategy()),
    LFU(new XxlRpcLoadBalanceLFUStrategy()),
    CONSISTENT_HASH(new XxlRpcLoadBalanceConsistentHashStrategy());


    public final JobsRpcLoadBalance xxlRpcInvokerRouter;

    LoadBalance(JobsRpcLoadBalance xxlRpcInvokerRouter) {
        this.xxlRpcInvokerRouter = xxlRpcInvokerRouter;
    }


    public static LoadBalance match(String name, LoadBalance defaultRouter) {
        for (LoadBalance item : LoadBalance.values()) {
            if (item.equals(name)) {
                return item;
            }
        }
        return defaultRouter;
    }
}