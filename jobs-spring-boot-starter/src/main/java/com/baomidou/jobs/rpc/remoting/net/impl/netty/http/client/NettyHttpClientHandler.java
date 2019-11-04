package com.baomidou.jobs.rpc.remoting.net.impl.netty.http.client;

import com.baomidou.jobs.rpc.remoting.invoker.JobsRpcInvokerFactory;
import com.baomidou.jobs.rpc.serialize.IJobsRpcSerializer;
import com.baomidou.jobs.rpc.remoting.net.params.JobsRpcResponse;
import com.baomidou.jobs.exception.JobsRpcException;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * netty_http
 *
 * @author xuxueli 2015-11-24 22:25:15
 */
public class NettyHttpClientHandler extends SimpleChannelInboundHandler<FullHttpResponse> {
    private static final Logger logger = LoggerFactory.getLogger(NettyHttpClientHandler.class);


    private JobsRpcInvokerFactory xxlRpcInvokerFactory;
    private IJobsRpcSerializer serializer;

    public NettyHttpClientHandler(final JobsRpcInvokerFactory xxlRpcInvokerFactory, IJobsRpcSerializer serializer) {
        this.xxlRpcInvokerFactory = xxlRpcInvokerFactory;
        this.serializer = serializer;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {

        // response parse
        byte[] responseBytes = ByteBufUtil.getBytes(msg.content());

        // valid
        if (responseBytes.length == 0) {
            throw new JobsRpcException("Jobs rpc request data empty.");
        }

        // response deserialize
        JobsRpcResponse xxlRpcResponse = (JobsRpcResponse) serializer.deserialize(responseBytes, JobsRpcResponse.class);

        // notify response
        xxlRpcInvokerFactory.notifyInvokerFuture(xxlRpcResponse.getRequestId(), xxlRpcResponse);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);
        logger.error("Jobs rpc netty_http client caught exception", cause);
        ctx.close();
    }

    /*@Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // retry
        super.channelInactive(ctx);
    }*/

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            ctx.channel().close();      // close idle channel
            logger.debug("Jobs rpc netty_http client close an idle channel.");
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

}
