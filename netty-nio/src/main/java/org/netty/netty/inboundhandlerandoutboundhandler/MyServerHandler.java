package org.netty.netty.inboundhandlerandoutboundhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author lijichen
 * @date 2021/2/2 - 14:42
 */
public class MyServerHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {

        System.out.println("MyServerHandler channelRead0 : 客户端" + ctx.channel().remoteAddress() + "long ： " + msg);

        // 服务器端回复一个long
        ctx.writeAndFlush(9876543l);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
