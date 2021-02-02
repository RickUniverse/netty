package org.netty.netty.inboundhandlerandoutboundhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author lijichen
 * @date 2021/2/2 - 14:33
 */
public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 加入入站的handler进行解码，MyByteToLongDecoder
//        pipeline.addLast(new MyByteToLongDecoder());
        pipeline.addLast(new MyByteToLongDecoder2());

        // 加入出站的编码器
        pipeline.addLast(new MyLongToByteEncoder());

        // 加入自定义handler
        pipeline.addLast(new MyServerHandler());
    }
}
