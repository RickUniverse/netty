package org.netty.netty.inboundhandlerandoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @author lijichen
 * @date 2021/2/2 - 14:35
 */
public class MyByteToLongDecoder2 extends ReplayingDecoder<Void> {

    /**
     * decode 方法；会根据接收的数据，被调用多次，直到没有新的元素被添加到list中，或者byteBuf没有更多可读的字节为止
     *
     * 如果list集合 out 不为空，就会将内容传递给下一个ChannelInboundHandler处理，该处理器的方法也会被调用多次
     *
     * @param ctx 上下文对象
     * @param in 入站的ByteBuf
     * @param out list集合，将解码后的数据传递给下一个handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        System.out.println("MyByteToLongDecoder 被调用");

        // ReplayingDecoder 不需要判断字节是否足够，内部已经做了判断
        out.add(in.readLong());
    }
}
