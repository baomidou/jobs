package com.baomidou.jobs.rpc.remoting.net.impl.netty.socket.client;

import com.baomidou.jobs.rpc.remoting.invoker.JobsRpcInvokerFactory;
import com.baomidou.jobs.rpc.remoting.net.params.JobsRpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * rpc netty client handler
 *
 * @author xuxueli 2015-10-31 18:00:27
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<JobsRpcResponse> {
    private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);
    private JobsRpcInvokerFactory jobsRpcInvokerFactory;

    public NettyClientHandler(final JobsRpcInvokerFactory jobsRpcInvokerFactory) {
        this.jobsRpcInvokerFactory = jobsRpcInvokerFactory;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JobsRpcResponse xxlRpcResponse) throws Exception {
        // notify response
        jobsRpcInvokerFactory.notifyInvokerFuture(xxlRpcResponse.getRequestId(), xxlRpcResponse);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("Jobs rpc netty client caught exception", cause);
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            // close idle channel
            ctx.channel().close();
            logger.debug("Jobs rpc netty client close an idle channel.");
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
