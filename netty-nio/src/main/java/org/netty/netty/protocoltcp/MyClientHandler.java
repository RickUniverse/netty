package org.netty.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

/**
 * @author lijichen
 * @date 2021/2/2 - 16:48
 */
public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 客户端发送10 条消息
        for (int i = 0; i < 10; ++i) {
            String msg = "hello，服务器 ";
            byte[] content = msg.getBytes("UTF-8");
            int length = msg.getBytes("UTF-8").length;

            // 创建协议包对象
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(length);
            messageProtocol.setContent(content);

            ctx.writeAndFlush(messageProtocol);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {

        // 将MessageProtocol.content 转换为字符串
        String message = new String(msg.getContent(), Charset.forName("utf-8"));

        System.out.println("\t\t\t");
        System.out.println("消息大小：：" + msg.getLen());
        System.out.println("客户端收到服务器端的消息：" + message);
        System.out.println("消息量：" + (++count));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
