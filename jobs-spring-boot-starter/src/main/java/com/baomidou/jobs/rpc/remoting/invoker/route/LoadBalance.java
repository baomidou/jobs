package com.baomidou.jobs.rpc.remoting.invoker.route;

import com.baomidou.jobs.rpc.remoting.invoker.route.impl.*;

/**
 * 负载均衡策略
 *
 * @author jobob
 * @since 2019-11-01
 */
public enum LoadBalance {
    RANDOM(new RpcLoadBalanceRandomStrategy()),
    ROUND(new RpcLoadBalanceRoundStrategy()),
    CONSISTENT_HASH(new RpcLoadBalanceConsistentHashStrategy());

    public final JobsRpcLoadBalance rpcLoadBalance;

    LoadBalance(JobsRpcLoadBalance rpcLoadBalance) {
        this.rpcLoadBalance = rpcLoadBalance;
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