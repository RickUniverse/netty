package org.netty.zerocope;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author lijichen
 * @date 2021/1/30 - 19:56
 */
public class IOServer {
    public static void main(String[] args) throws IOException {
        InetSocketAddress address = new InetSocketAddress(6666);
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverSocketChannel.socket();

        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);

        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();

            int readCount = 0;
            while (-1 != readCount) {
                try {
                    readCount = socketChannel.read(byteBuffer);
                } catch (IOException e) {
                    //e.printStackTrace();
                    break;
                }
                // 倒带position mark作废
                byteBuffer.rewind();

            }
        }
    }
}
