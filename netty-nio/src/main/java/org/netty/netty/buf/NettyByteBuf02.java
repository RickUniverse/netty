package org.netty.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * @author lijichen
 * @date 2021/2/1 - 15:13
 */
public class NettyByteBuf02 {
    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello，world", Charset.forName("utf-8"));

        // 如果有内容
        if (byteBuf.hasArray()) {
            byte[] content = byteBuf.array();

            // 偏移量
            System.out.println(byteBuf.arrayOffset());
            System.out.println(byteBuf.readerIndex());
            System.out.println(byteBuf.writerIndex());

            System.out.println(byteBuf.capacity());

            // 不会对readerIndex有影响
            System.out.println(byteBuf.getByte(0));
            // 会对readerIndex有影响
            System.out.println(byteBuf.readByte());

            // 可用字节
            System.out.println(byteBuf.readableBytes());

            for (int i = 0; i < byteBuf.capacity(); i++) {
                System.out.println((char) byteBuf.getByte(i));
            }

            // 从第0个位置，截取4个
            System.out.println(byteBuf.getCharSequence(0,4,Charset.forName("utf-8")));

        }
    }
}
