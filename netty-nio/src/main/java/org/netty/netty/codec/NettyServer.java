package org.netty.netty.codec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

/**
 * @author lijichen
 * @date 2021/1/31 - 15:48
 */
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        // 创建两个线程组boosGroup 和 workerGroup
        // boosGroup 只处理连接请求，业务处理交给workerGroup来做
        // 两个都是无限循环
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建服务器端的启动对象
            ServerBootstrap bootstrap = new ServerBootstrap();

            // 使用链式编程进行设置
            bootstrap.group(boosGroup, workerGroup)// 设置两个线程组
                    .channel(NioServerSocketChannel.class)// 使用NioServerSocketChannel作为服务的实现
                    .option(ChannelOption.SO_BACKLOG, 128)// 设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)// 设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {// 创建一个通道测试对象（匿名对象）
                        // 设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                            ChannelPipeline pipeline = ch.pipeline();
                            // 加入proto解码器, 需要传入StudentPOJO.org.netty.netty.codec2.Student.getDefaultInstance()
                            pipeline.addLast("encoder",new ProtobufDecoder(StudentPOJO.Student.getDefaultInstance()));
                            pipeline.addLast(new NettyServerHandler());



                            // 可以使用一个集合管理SocketChannel 再推送消息时，
                            // 可将任务加入到各个channel对应的NioEventLoop的taskQueue获知scheduleTaskQueue
                            System.out.println("client的SocketChannel的哈希值:"+ch.hashCode());
                        }
                    });//给workerGroup 对应的EventLoop对应的管道设置处理器

            System.out.println("服务器is。。ready。。");

            //绑定一个端口并进行同步，启动服务器
            ChannelFuture channelFuture = bootstrap.bind(6666).sync();

            // 给ChannelFuture 注册监听事件，监听我们感兴趣的事
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("监听6666端口成功！");
                    } else {
                        System.out.println("监听6666端口失败！");
                    }

                }
            });

            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            // 优雅的关闭
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
