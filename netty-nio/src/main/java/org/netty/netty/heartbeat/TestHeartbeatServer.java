package org.netty.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author lijichen
 * @date 2021/2/1 - 17:42
 */
public class TestHeartbeatServer {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            /**
                             * IdleStateHandler : 是netty提供的处理空闲状态的处理器
                             * long readerIdleTime ： 表示多长时间没有读，就发送一个心跳检测包，检测是否连接
                             * long writerIdleTime ： 表示多长时间没有写，就发送一个心跳检测包，检测是否连接
                             * long allIdleTime ： 表示多长时间没有读写，就发送一个心跳检测包，检测是否连接
                             *
                             * 当IdleStateHandler触发后，就会传递给管道的下一个handler去处理通过调用（触发）下一个
                             * handler的userEventTiggered在该方法中处理IdleStateHandler(读空闲，写空闲，都写空闲)
                             */
                            pipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));

                            pipeline.addLast(new TestHeartbeatServerHandler());

                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(6666).sync();
            channelFuture.channel().closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
