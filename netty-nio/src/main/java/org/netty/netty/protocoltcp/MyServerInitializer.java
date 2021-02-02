package org.netty.netty.protocoltcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author lijichen
 * @date 2021/2/2 - 16:55
 */
public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();

        // 加入解码器
        pipeline.addLast(new MyMessageDecoder());

        // 加入编码器
        pipeline.addLast(new MyMessageEncoder());

        pipeline.addLast(new MyServerHandler());
    }
}
