package org.netty.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author lijichen
 * @date 2021/1/29 - 17:08
 */

/**
 * buffer 的使用
 */
public class MappedByteBufferTest {
    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("1.txt","rw");
        FileChannel channel = randomAccessFile.getChannel();

        /**
         * FileChannel.MapMode.READ_WRITE : 使用的是读写模式
         * 0  ： 可以直接修改的起始位置
         * 5  ： 映射到内存的大小， 可以修改的位置是 0 --> 4
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mappedByteBuffer.put(0, (byte) 'H');
        mappedByteBuffer.put(3, (byte) '4');
//        mappedByteBuffer.put(5, (byte) 'o');// 报错

        randomAccessFile.close();
    }
}
