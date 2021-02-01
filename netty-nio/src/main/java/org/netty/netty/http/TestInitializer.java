package org.netty.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author lijichen
 * @date 2021/1/31 - 20:09
 */
public class TestInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        // 向管道加入处理器
        ChannelPipeline pipeline = ch.pipeline();

        // HttpServerCodec时netty提供的编-解码器
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());
        // 增加自定义的handler
        pipeline.addLast("MyTestHttpServerHandler",new TestHttpServerHandler());


    }
}
