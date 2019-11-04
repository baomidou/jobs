package com.baomidou.jobs.rpc.remoting.invoker.route.impl;

import com.baomidou.jobs.rpc.remoting.invoker.route.JobsRpcLoadBalance;

import java.util.Random;
import java.util.TreeSet;

/**
 * random
 *
 * @author xuxueli 2018-12-04
 */
public class RpcLoadBalanceRandomStrategy extends JobsRpcLoadBalance {

    private Random random = new Random();

    @Override
    public String route(String serviceKey, TreeSet<String> addressSet) {
        // arr
        String[] addressArr = addressSet.toArray(new String[addressSet.size()]);

        // random
        String finalAddress = addressArr[random.nextInt(addressSet.size())];
        return finalAddress;
    }
}
