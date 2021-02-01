package org.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author lijichen
 * @date 2021/1/30 - 15:43
 */
public class NIOClient {
    public static void main(String[] args) throws Exception {
        // 获取网络信道
        SocketChannel socketChannel = SocketChannel.open();
        // 设置非阻塞
        socketChannel.configureBlocking(false);

        // 提供服务器端的ip和端口
        InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1",6666);

        // 连接服务器
        if (!socketChannel.connect(socketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("链接需要时间，不会进行阻塞，可以做其他工作");
            }
        }

        // 如果连接成功就发送数据
        String str = "hello，hello明天";
        // wrap 会根据字符串的长度，开辟刚好的空间
        ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());
        // 发送数据，将buffer写入通道channel
        socketChannel.write(byteBuffer);
        System.in.read();

    }
}
