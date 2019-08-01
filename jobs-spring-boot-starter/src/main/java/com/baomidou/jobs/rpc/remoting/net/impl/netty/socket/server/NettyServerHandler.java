package com.baomidou.jobs.rpc.remoting.net.impl.netty.socket.server;

import com.baomidou.jobs.rpc.remoting.net.params.JobsRpcRequest;
import com.baomidou.jobs.rpc.remoting.net.params.JobsRpcResponse;
import com.baomidou.jobs.rpc.remoting.provider.JobsRpcProviderFactory;
import com.baomidou.jobs.service.JobsHelper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * netty server handler
 *
 * @author xuxueli 2015-10-29 20:07:37
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<JobsRpcRequest> {
    private JobsRpcProviderFactory xxlRpcProviderFactory;
    private ThreadPoolExecutor serverHandlerPool;

    public NettyServerHandler(final JobsRpcProviderFactory xxlRpcProviderFactory, final ThreadPoolExecutor serverHandlerPool) {
        this.xxlRpcProviderFactory = xxlRpcProviderFactory;
        this.serverHandlerPool = serverHandlerPool;
    }

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, final JobsRpcRequest xxlRpcRequest) throws Exception {
        try {
            // do invoke
            serverHandlerPool.execute(() -> {
                // invoke + response
                JobsRpcResponse xxlRpcResponse = xxlRpcProviderFactory.invokeService(xxlRpcRequest);

                ctx.writeAndFlush(xxlRpcResponse);
            });
        } catch (Exception e) {
            // catch error
            JobsRpcResponse jobsRpcResponse = new JobsRpcResponse();
            jobsRpcResponse.setRequestId(xxlRpcRequest.getRequestId());
            jobsRpcResponse.setErrorMsg(JobsHelper.getErrorInfo(e));
            ctx.writeAndFlush(jobsRpcResponse);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("Jobs rpc provider netty server caught exception", cause);
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            ctx.channel().close();
            log.debug("Jobs rpc provider netty server close an idle channel.");
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
