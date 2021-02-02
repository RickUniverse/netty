package org.netty.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * @author lijichen
 * @date 2021/2/2 - 16:48
 */
public class MyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 客户端发送10 条消息
        for (int i = 0; i < 10; ++i) {
            ByteBuf byteBuf = Unpooled.copiedBuffer("hello，服务器", Charset.forName("utf-8"));

            // 发送
            ctx.writeAndFlush(byteBuf);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] buffer = new byte[msg.readableBytes()];
        msg.readBytes(buffer);

        // 将buffer转换为字符串
        String message = new String(buffer, Charset.forName("utf-8"));

        System.out.println("客户端收到服务器端的消息：" + message);
        System.out.println("消息量：" + (++count));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
