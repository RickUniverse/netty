package org.netty.netty.inboundhandlerandoutboundhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author lijichen
 * @date 2021/2/2 - 15:00
 */
public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 加入一个出站的handler，对数据进行编码
        pipeline.addLast(new MyLongToByteEncoder());

        // 加入入站的解码器
//        pipeline.addLast(new MyByteToLongDecoder());
        pipeline.addLast(new MyByteToLongDecoder2());

        // 加入一个自定义的hanelr，处理业务
        pipeline.addLast(new MyClientHandler());
    }
}
