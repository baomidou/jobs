package com.baomidou.jobs.rpc.remoting.net.impl.netty.http.client;

import com.baomidou.jobs.rpc.remoting.invoker.JobsRpcInvokerFactory;
import com.baomidou.jobs.rpc.remoting.net.common.ConnectClient;
import com.baomidou.jobs.rpc.remoting.net.params.JobsRpcRequest;
import com.baomidou.jobs.rpc.serialize.IJobsRpcSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * netty_http
 *
 * @author xuxueli 2015-11-24 22:25:15
 */
@Slf4j
public class NettyHttpConnectClient extends ConnectClient {

    private EventLoopGroup group;
    private Channel channel;

    private IJobsRpcSerializer serializer;
    private String address;
    private String host;

    @Override
    public void init(String address, final IJobsRpcSerializer serializer, final JobsRpcInvokerFactory xxlRpcInvokerFactory) throws Exception {

        if (!address.toLowerCase().startsWith("http")) {
            address = "http://" + address;    // IP:PORT, need parse to url
        }

        this.address = address;
        URL url = new URL(address);
        this.host = url.getHost();
        int port = url.getPort() > -1 ? url.getPort() : 80;


        this.group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new IdleStateHandler(0, 0, 10, TimeUnit.MINUTES))
                                .addLast(new HttpClientCodec())
                                .addLast(new HttpObjectAggregator(5 * 1024 * 1024))
                                .addLast(new NettyHttpClientHandler(xxlRpcInvokerFactory, serializer));
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true);
        this.channel = bootstrap.connect(host, port).sync().channel();

        this.serializer = serializer;

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
        byte[] requestBytes = serializer.serialize(xxlRpcRequest);

        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, new URI(address).getRawPath(), Unpooled.wrappedBuffer(requestBytes));
        request.headers().set(HttpHeaderNames.HOST, host);
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());

        this.channel.writeAndFlush(request).sync();
    }

}
