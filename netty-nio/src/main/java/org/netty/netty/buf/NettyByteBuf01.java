package org.netty.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author lijichen
 * @date 2021/2/1 - 15:13
 */
public class NettyByteBuf01 {
    public static void main(String[] args) {
        // 创建一个bytebuf
        // 该bytebuf不需要flip进行反转，因为维护了readerindex和writeindex
        ByteBuf buffer = Unpooled.buffer(10);

        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }

        System.out.println("capacity="+buffer.capacity());

        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println(buffer.readByte());
        }
    }
}
