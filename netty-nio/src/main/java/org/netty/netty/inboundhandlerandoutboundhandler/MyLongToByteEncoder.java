package org.netty.netty.inboundhandlerandoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author lijichen
 * @date 2021/2/2 - 15:04
 */
public class MyLongToByteEncoder extends MessageToByteEncoder<Long> {
    // 进行编码的方法
    @Override
    protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception {
        System.out.println("MyLongToByteEncoder 被调用,msg: " + msg);

        out.writeLong(msg);
    }
}
