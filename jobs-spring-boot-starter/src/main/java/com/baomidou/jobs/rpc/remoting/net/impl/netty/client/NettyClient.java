package com.baomidou.jobs.rpc.remoting.net.impl.netty.client;

import com.baomidou.jobs.rpc.remoting.net.Client;
import com.baomidou.jobs.rpc.remoting.net.params.XxlRpcRequest;
import com.baomidou.jobs.rpc.remoting.net.common.ConnectClient;

/**
 * netty client
 *
 * @author xuxueli 2015-11-24 22:25:15
 */
public class NettyClient extends Client {

	private Class<? extends ConnectClient> connectClientImpl = NettyConnectClient.class;

	@Override
	public void asyncSend(String address, XxlRpcRequest xxlRpcRequest) throws Exception {
		ConnectClient.asyncSend(xxlRpcRequest, address, connectClientImpl, xxlRpcReferenceBean);
	}

}
