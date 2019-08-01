package com.baomidou.jobs.rpc.remoting.net;

import com.baomidou.jobs.rpc.remoting.invoker.reference.JobsRpcReferenceBean;
import com.baomidou.jobs.rpc.remoting.net.params.JobsRpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * i client
 *
 * @author xuxueli 2015-11-24 22:18:10
 */
public abstract class Client {
    protected static final Logger logger = LoggerFactory.getLogger(Client.class);


    // ---------------------- init ----------------------

    protected volatile JobsRpcReferenceBean xxlRpcReferenceBean;

    public void init(JobsRpcReferenceBean xxlRpcReferenceBean) {
        this.xxlRpcReferenceBean = xxlRpcReferenceBean;
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
