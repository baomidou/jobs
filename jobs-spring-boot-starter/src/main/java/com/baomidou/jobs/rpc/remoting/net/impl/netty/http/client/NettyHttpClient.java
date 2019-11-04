package com.baomidou.jobs.rpc.remoting.net.impl.netty.http.client;

import com.baomidou.jobs.rpc.remoting.net.Client;
import com.baomidou.jobs.rpc.remoting.net.common.ConnectClient;
import com.baomidou.jobs.rpc.remoting.net.params.JobsRpcRequest;

/**
 * netty_http client
 *
 * @author xuxueli 2015-11-24 22:25:15
 */
public class NettyHttpClient extends Client {

    private Class<? extends ConnectClient> connectClientImpl = NettyHttpConnectClient.class;

    @Override
    public void asyncSend(String address, JobsRpcRequest rpcRequest) throws Exception {
        ConnectClient.asyncSend(rpcRequest, address, connectClientImpl, rpcReferenceBean);
    }
}
