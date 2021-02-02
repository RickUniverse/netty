package org.netty.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @author lijichen
 * @date 2021/2/2 - 17:49
 */
public class MyMessageDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        System.out.println("MyMessageDecoder decode 被调用");

        // 获取长度 int
        int length = in.readInt();

        byte[] content = new byte[length];
        // 获取content
        in.readBytes(content);

        // 封装为MessageProtocol
        MessageProtocol messageProtocol = new MessageProtocol().setLen(length).setContent(content);

        // 加入到集合中，传递下去
        out.add(messageProtocol);
    }
}
