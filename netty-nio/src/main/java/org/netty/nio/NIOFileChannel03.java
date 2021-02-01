package org.netty.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author lijichen
 * @date 2021/1/29 - 19:08
 */
public class NIOFileChannel03 {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("1.txt");
        // 获取输入通道
        FileChannel inputStreamChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        // 获取输出通道
        FileChannel outputStreamChannel = fileOutputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(10);

        while (true) {
            // 必须先clear
            buffer.clear();
            // 获取读取的position
            int read = inputStreamChannel.read(buffer);
            // 是否全部读取完毕
            if (read == -1) {
                break;
            }
            // 反转
            buffer.flip();
            // 读数据
            outputStreamChannel.write(buffer);
        }

        fileInputStream.close();
        fileOutputStream.close();
    }
}
