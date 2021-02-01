package org.netty.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author lijichen
 * @date 2021/1/29 - 19:08
 */
public class NIOFileChannel01 {
    public static void main(String[] args) throws IOException {
        String str = "qwerty,你好";

        FileOutputStream fileOutputStream = new FileOutputStream("d:\\file01.txt");

        // 获取对应的channel
        // 真实类型是，FileChannel的实现类：FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();

        // 创建一个缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        buffer.put(str.getBytes());

        // 反转进行,读操作
        buffer.flip();

        // 读取到通道中
        fileChannel.write(buffer);

        // 关闭
        fileOutputStream.close();

    }
}
