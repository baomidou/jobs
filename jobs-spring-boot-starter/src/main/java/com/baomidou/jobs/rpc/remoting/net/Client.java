package com.baomidou.jobs.rpc.remoting.net;

import com.baomidou.jobs.rpc.remoting.invoker.reference.JobsRpcReferenceBean;
import com.baomidou.jobs.rpc.remoting.net.params.JobsRpcRequest;

/**
 * i client
 *
 * @author xuxueli 2015-11-24 22:18:10
 */
public abstract class Client {

    // ---------------------- init ----------------------

    protected volatile JobsRpcReferenceBean rpcReferenceBean;

    public void init(JobsRpcReferenceBean rpcReferenceBean) {
        this.rpcReferenceBean = rpcReferenceBean;
    }


    // ---------------------- send ----------------------

    /**
     * async send, bind requestId and future-response
     *
     * @param address
     * @param xxlRpcRequest
     * @return
     * @throws Exception
     */
    public abstract void asyncSend(String address, JobsRpcRequest xxlRpcRequest) throws Exception;

}
