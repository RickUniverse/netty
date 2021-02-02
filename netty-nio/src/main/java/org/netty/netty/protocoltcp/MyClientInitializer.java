package org.netty.netty.protocoltcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author lijichen
 * @date 2021/2/2 - 16:47
 */
public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 加入消息协议包编码器
        pipeline.addLast(new MyMessageEncoder());

        // 加入解码器
        pipeline.addLast(new MyMessageDecoder());

        pipeline.addLast(new MyClientHandler());
    }
}
