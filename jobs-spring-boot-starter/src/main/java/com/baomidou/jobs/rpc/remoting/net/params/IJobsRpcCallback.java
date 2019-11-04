package com.baomidou.jobs.rpc.remoting.net.params;

import com.baomidou.jobs.exception.JobsRpcException;

/**
 * Job RPC 回调接口
 *
 * @author jobob
 * @since 2019-06-08
 */
public interface IJobsRpcCallback {

    /**
     * 执行回调逻辑
     */
    void execute() throws JobsRpcException;
}
