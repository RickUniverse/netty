package org.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Scattering:将数据写入到buffer时，可以采用buffer数组【分散】，一次写入
 * GatherIing：可以采用buffer数组的方式依次读取
 * @author lijichen
 * @date 2021/1/29 - 21:17
 */
public class ScatteringAndGatherIingTest {
    public static void main(String[] args) throws Exception {

        // 使用ServerSocketChannel 和 ServerSocket 网络
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(6666);

        // 绑定端口到socket
        serverSocketChannel.socket().bind(inetSocketAddress);

        // 创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        // 等客户端链接
        SocketChannel socketChannel = serverSocketChannel.accept();
        // 假设从客户端接受8个字符
        int messageLength = 8;

        // 循环读取客户端传来的数据
        while (true) {

            // 如果没有读取完毕，即：socketChannel.read(byteBuffers); 能读到数据，就能循环下去
            int byteRead = 0;
            // byteRead < messageLength : 这个判断条件只是为了测试并不严谨！！
            while (byteRead < messageLength) {
                // 读取数据
                long read = socketChannel.read(byteBuffers);//byteBuffers : 总大小是8 所以每次只能读取到8 个
                byteRead += read;//累计
                System.out.println("byteRead:"+byteRead);
                // 输出当前buffer数组中的信息
                Arrays.asList(byteBuffers).stream().map(buffer -> "Read ===> position:" + buffer.position() + "; limit:" + buffer.limit())
                        .forEach(System.out::println);
            }

            // 所有buffer flip
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip());

            // 数据显示到客户端
            long byteWrite = 0;
            while (byteWrite < messageLength) {
                long write = socketChannel.write(byteBuffers);
                byteWrite += write;
            }

            // 所有bufferclear
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.clear());

            System.out.println("Finished ==> byteRead:" + byteRead + "byteWrite:"+byteWrite + "messageLength:" + messageLength);

        }

    }
}
