package org.netty.dubborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.netty.dubborpc.provider.HelloServiceImpl;

/**
 * @author lijichen
 * @date 2021/2/3 - 19:56
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 获取客户端发送的消息，并调用服务
        System.out.println("远程请求发送过来的协议头加参数 msg 是=" + msg);
        // 一般客户端调用api时，需要定义一个协议
        // 比如我们要求，每次发送消息必须以"HelloService#hello#" 开头 "HelloService#hello#变量"
        if (msg.toString().startsWith("HelloService#hello#")) {
            String helloResult = new HelloServiceImpl().hello(msg.toString().substring(msg.toString().lastIndexOf('#') + 1));

            ctx.writeAndFlush(helloResult);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
