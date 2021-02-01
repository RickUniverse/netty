package org.netty.zerocope;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @author lijichen
 * @date 2021/1/30 - 20:02
 */
public class IOClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost",6666));
        String fileName = "xxx";

        FileChannel channel = new FileInputStream(fileName).getChannel();

        long startTime = System.currentTimeMillis();

        // linux 下一个transferfrom就可以完成文件的传输
        // window 下，每次只能发送8m, 需要分段传输
        // transferfrom 底层就是用到零拷贝
        long transferCount = channel.transferTo(0, channel.size(), socketChannel);

        System.out.println("发送的总字节数："+transferCount+"耗时" + (System.currentTimeMillis() - startTime));

        channel.close();
    }
}
