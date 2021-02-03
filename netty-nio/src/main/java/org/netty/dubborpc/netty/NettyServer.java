package org.netty.dubborpc.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author lijichen
 * @date 2021/2/3 - 19:39
 */
public class NettyServer {

    // 对外暴露标准的启动方式
    public static void startServer(String hostName, int port) {
        startServer0(hostName,port);
    }

    // 编写方法完成对NettyServer的启动
    private static void startServer0(String hostName, int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            // 解码器
                            pipeline.addLast(new StringDecoder());
                            // 编码器
                            pipeline.addLast(new StringEncoder());

                            // 自定义handler
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });

            System.out.println("-------供给者开始提供服务------");

            ChannelFuture channelFuture = serverBootstrap.bind(hostName, port).sync();
            channelFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
