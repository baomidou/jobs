package com.baomidou.jobs.rpc.remoting.net.impl.netty.socket.codec;

import com.baomidou.jobs.rpc.serialize.IJobsRpcSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * encoder
 *
 * @author xuxueli 2015-10-29 19:43:00
 */
public class NettyEncoder extends MessageToByteEncoder<Object> {
    private Class<?> genericClass;
    private IJobsRpcSerializer serializer;

    public NettyEncoder(Class<?> genericClass, final IJobsRpcSerializer serializer) {
        this.genericClass = genericClass;
        this.serializer = serializer;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
        if (genericClass.isInstance(in)) {
            byte[] data = serializer.serialize(in);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}