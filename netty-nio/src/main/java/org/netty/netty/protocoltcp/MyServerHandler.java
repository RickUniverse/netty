package org.netty.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.UUID;

/**
 * @author lijichen
 * @date 2021/2/2 - 16:55
 */
public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {

        // 将buffer转换为字符串
        String message = new String(msg.getContent(), Charset.forName("utf-8"));

        System.out.println("\t\t\t");
        System.out.println("消息长度是：" + msg.getLen());
        System.out.println("服务器端收到的消息：" + message);
        System.out.println("消息量：" + (++count));

        // 会送一个随机id
        String responseBody = UUID.randomUUID().toString();

        // 创建协议包并发送
        ctx.writeAndFlush(new MessageProtocol().setLen(responseBody
                .length())
                .setContent(responseBody.getBytes("utf-8")));

        System.out.println("服务器发送了：" + responseBody);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
