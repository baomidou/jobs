package com.baomidou.jobs.rpc.remoting.net;

import com.baomidou.jobs.rpc.remoting.net.impl.netty.http.client.NettyHttpClient;
import com.baomidou.jobs.rpc.remoting.net.impl.netty.http.server.NettyHttpServer;
import com.baomidou.jobs.rpc.remoting.net.impl.netty.socket.client.NettyClient;
import com.baomidou.jobs.rpc.remoting.net.impl.netty.socket.server.NettyServer;

/**
 * remoting net
 *
 * @author xuxueli 2015-11-24 22:09:57
 */
public enum NetEnum {
    /**
     * netty tcp server
     */
    NETTY(NettyServer.class, NettyClient.class),
    /**
     * netty http server (servlet no server, ServletServerHandler)
     */
    NETTY_HTTP(NettyHttpServer.class, NettyHttpClient.class);

    public final Class<? extends Server> serverClass;
    public final Class<? extends Client> clientClass;

    NetEnum(Class<? extends Server> serverClass, Class<? extends Client> clientClass) {
        this.serverClass = serverClass;
        this.clientClass = clientClass;
    }

    public static NetEnum autoMatch(String name, NetEnum defaultEnum) {
        for (NetEnum item : NetEnum.values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return defaultEnum;
    }
}