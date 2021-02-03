package org.netty.dubborpc.netty;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lijichen
 * @date 2021/2/3 - 20:40
 */
public class NettyClient {

    // c创建线程池
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static NettyClientHandler client;

    // 计数器
    private static int conut;


    public Object getBean(final Class<?> serviceClass, final String protocolHead) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{serviceClass},
                (proxy, method, args) -> {

                    System.out.println("(proxy, method, args) -> {} 被调用第：" + (++conut) + "次了");

                    // 先判断client是否为空
                    if (client == null) {
                        initClient();
                    }
                    // 设置要给服务器发送的消息
                    // 需要使用协议头,就是调用HelloService hello(String msg)时传入的参数
                    client.setPara(protocolHead + args[0]);

                    // 调用NettyClientHandler 的 call方法
                    return executor.submit(client).get();
                });
    }


    // 初始化客户端
    private static void initClient() {

        client = new NettyClientHandler();

        // 创建io线程池
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(client);
                    }
                });

        try {
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8080).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
