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
public class NIOFileChannel02 {
    public static void main(String[] args) throws IOException {
        File file = new File("d:\\file01.txt");

        FileInputStream fileInputStream = new FileInputStream(file);

        FileChannel channel = fileInputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        //将通道中的数据读入缓冲区ByteBuffer
        channel.read(byteBuffer);

        // 输出
        System.out.println(new String(byteBuffer.array()));

        fileInputStream.close();
    }
}
