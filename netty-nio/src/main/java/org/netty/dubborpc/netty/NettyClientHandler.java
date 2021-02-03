package org.netty.dubborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/**
 * @author lijichen
 * @date 2021/2/3 - 20:23
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    // 上下文
    private ChannelHandlerContext content;
    // 返回结果
    private String result;
    // 发送给服务器的参数
    private String para;


    // 当与服务器建立连接后调用该方法  执行顺序是：1
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("channelActive 被调用");

        content = ctx;
    }

    // 当与服务器有消息发送过来后调用该方法  执行顺序是：4
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("channelRead 被调用");
        // 将结果置为全局
        result = msg.toString();

        // 唤醒发送消息给服务器端wait的call方法
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    // 被代理对象调用，发送数据给服务器 -》 wait -》 等待唤醒 -》 返回结果
    // 执行顺序是：3 5
    @Override
    public synchronized Object call() throws Exception {

        System.out.println("call 被调用 wait before");

        content.writeAndFlush(para);
        // 服务器端相应结果后执行 channelRead后等待被唤醒
        wait();

        System.out.println("call 被调用 wait after");

        // 返回结果
        return result;
    }

    // 设置参数 执行顺序是：2
    void setPara(String para) {

        System.out.println("setPara 被调用设置的参数是：" + para);

        this.para = para;
    }
}
