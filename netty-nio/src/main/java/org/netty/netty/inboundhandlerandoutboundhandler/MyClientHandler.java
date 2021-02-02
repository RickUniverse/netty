package org.netty.netty.inboundhandlerandoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

/**
 * @author lijichen
 * @date 2021/2/2 - 15:07
 */
public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("服务器地址：" + ctx.channel().remoteAddress() + "消息是=> " + msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyClientHandler 发送数据：");

        // 发送的是一个long类型
        ctx.writeAndFlush(123456L);
        // qweasdzxcqweasdz 是16个字节
//        ctx.writeAndFlush(Unpooled.copiedBuffer("qweasdzxcqweasdz", Charset.forName("utf-8")));
    }
}
