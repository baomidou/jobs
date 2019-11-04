package com.baomidou.jobs.rpc.remoting.net.impl.netty.socket.codec;

import com.baomidou.jobs.rpc.serialize.IJobsRpcSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * decoder
 *
 * @author xuxueli 2015-10-29 19:02:36
 */
public class NettyDecoder extends ByteToMessageDecoder {
    private Class<?> genericClass;
    private IJobsRpcSerializer serializer;

    public NettyDecoder(Class<?> genericClass, final IJobsRpcSerializer serializer) {
        this.genericClass = genericClass;
        this.serializer = serializer;
    }

    @Override
    public final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }
        in.markReaderIndex();
        int dataLength = in.readInt();
        if (dataLength < 0) {
            ctx.close();
        }
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;    // fix 1024k buffer splice limix
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);

        Object obj = serializer.deserialize(data, genericClass);
        out.add(obj);
    }
}
