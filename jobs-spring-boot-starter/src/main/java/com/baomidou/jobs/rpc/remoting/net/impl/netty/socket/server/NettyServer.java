package com.baomidou.jobs.rpc.remoting.net.impl.netty.socket.server;

import com.baomidou.jobs.rpc.remoting.net.Server;
import com.baomidou.jobs.rpc.remoting.net.impl.netty.socket.codec.NettyDecoder;
import com.baomidou.jobs.rpc.remoting.net.impl.netty.socket.codec.NettyEncoder;
import com.baomidou.jobs.rpc.remoting.net.params.JobsRpcRequest;
import com.baomidou.jobs.rpc.remoting.net.params.JobsRpcResponse;
import com.baomidou.jobs.rpc.remoting.provider.JobsRpcProviderFactory;
import com.baomidou.jobs.rpc.util.ThreadPoolUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * netty rpc server
 *
 * @author xuxueli 2015-10-29 18:17:14
 */
@Slf4j
public class NettyServer extends Server {
    private Thread thread;

    @Override
    public void start(final JobsRpcProviderFactory jobsRpcProviderFactory) throws Exception {
        thread = new Thread(() -> {

            // param
            final ThreadPoolExecutor serverHandlerPool = ThreadPoolUtil.makeServerThreadPool(NettyServer.class.getSimpleName());
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();

            try {
                // start server
                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel channel) throws Exception {
                                channel.pipeline()
                                        .addLast(new IdleStateHandler(0, 0, 10, TimeUnit.MINUTES))
                                        .addLast(new NettyDecoder(JobsRpcRequest.class, jobsRpcProviderFactory.getSerializer()))
                                        .addLast(new NettyEncoder(JobsRpcResponse.class, jobsRpcProviderFactory.getSerializer()))
                                        .addLast(new NettyServerHandler(jobsRpcProviderFactory, serverHandlerPool));
                            }
                        })
                        .childOption(ChannelOption.TCP_NODELAY, true)
                        .childOption(ChannelOption.SO_KEEPALIVE, true);

                // bind
                ChannelFuture future = bootstrap.bind(jobsRpcProviderFactory.getPort()).sync();

                log.info("Jobs rpc remoting server start success, nettype = {}, port = {}", NettyServer.class.getName(), jobsRpcProviderFactory.getPort());
                onStarted();

                // wait util stop
                future.channel().closeFuture().sync();

            } catch (Exception e) {
                if (e instanceof InterruptedException) {
                    log.info("Jobs rpc remoting server stop.");
                } else {
                    log.error("Jobs rpc remoting server error.", e);
                }
            } finally {
                // stop
                try {
                    serverHandlerPool.shutdown();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                try {
                    workerGroup.shutdownGracefully();
                    bossGroup.shutdownGracefully();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();

    }

    @Override
    public void stop() throws Exception {

        // destroy server thread
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }

        // on stop
        onStopped();
        log.info("Jobs rpc remoting server destroy success.");
    }
}
