package com.baomidou.jobs.router;

import com.baomidou.jobs.toolkit.ConsistentHash;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;

/**
 * 执行器调度一次性 HASH 路由
 *
 * @author jobob
 * @since 2019-07-19
 */
@Slf4j
public class ExecutorConsistentHashRouter implements IJobsExecutorRouter {

    @Override
    public String route(String app, List<String> addressList) {
        if (null == app || null == addressList) {
            return null;
        }
        int nodeCount = addressList.size();
        if (0 == nodeCount) {
            return null;
        } else if (nodeCount > 1) {
            nodeCount = nodeCount * 10;
        } else {
            // 一个节点不需要负载直接返回
            return addressList.get(0);
        }
        // 设置虚拟节点为真实节点数的 10 倍
        ConsistentHash<String> consistentHash = new ConsistentHash(nodeCount);
        consistentHash.add(addressList);
        String address = consistentHash.getNode(app + new Random().nextInt(nodeCount));
        log.debug("{} Consistent Hash Address [ {} ]", app, address);
        return address;
    }
}
