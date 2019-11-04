package com.baomidou.jobs.rpc.remoting.net.impl.netty.socket.client;

import com.baomidou.jobs.rpc.remoting.invoker.JobsRpcInvokerFactory;
import com.baomidou.jobs.rpc.remoting.net.common.ConnectClient;
import com.baomidou.jobs.rpc.remoting.net.impl.netty.socket.codec.NettyDecoder;
import com.baomidou.jobs.rpc.remoting.net.impl.netty.socket.codec.NettyEncoder;
import com.baomidou.jobs.rpc.remoting.net.params.JobsRpcRequest;
import com.baomidou.jobs.rpc.remoting.net.params.JobsRpcResponse;
import com.baomidou.jobs.rpc.serialize.IJobsRpcSerializer;
import com.baomidou.jobs.rpc.util.IpUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * netty pooled client
 *
 * @author xuxueli
 */
@Slf4j
public class NettyConnectClient extends ConnectClient {
    private EventLoopGroup group;
    private Channel channel;

    @Override
    public void init(String address, final IJobsRpcSerializer serializer, final JobsRpcInvokerFactory xxlRpcInvokerFactory) throws Exception {

        Object[] array = IpUtil.parseIpPort(address);
        String host = (String) array[0];
        int port = (int) array[1];


        this.group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new IdleStateHandler(0, 0, 10, TimeUnit.MINUTES))
                                .addLast(new NettyEncoder(JobsRpcRequest.class, serializer))
                                .addLast(new NettyDecoder(JobsRpcResponse.class, serializer))
                                .addLast(new NettyClientHandler(xxlRpcInvokerFactory));
                    }
                })
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
        this.channel = bootstrap.connect(host, port).sync().channel();

        // valid
        if (!isValidate()) {
            close();
            return;
        }

        log.debug("Jobs rpc netty client proxy, connect to server success at host:{}, port:{}", host, port);
    }


    @Override
    public boolean isValidate() {
        if (this.channel != null) {
            return this.channel.isActive();
        }
        return false;
    }

    @Override
    public void close() {
        if (this.channel != null && this.channel.isActive()) {
            this.channel.close();        // if this.channel.isOpen()
        }
        if (this.group != null && !this.group.isShutdown()) {
            this.group.shutdownGracefully();
        }
        log.debug("Jobs rpc netty client close.");
    }


    @Override
    public void send(JobsRpcRequest xxlRpcRequest) throws Exception {
        this.channel.writeAndFlush(xxlRpcRequest).sync();
    }
}
