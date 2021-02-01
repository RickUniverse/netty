package org.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author lijichen
 * @date 2021/1/30 - 15:19
 */
public class NIOServer {
    public static void main(String[] args) throws IOException {
        // 创建:ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 得到一个selector对象
        Selector selector = Selector.open();

        // 绑定一个端口，在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        // serverSocketChannel 注册到selector 关联的事件为: OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环等待客户端链接
        while (true) {

            // 等待一秒， 如果没有事件发生就返回
            if (selector.select(1000) == 0) {
                System.out.println("服务器等待了一秒。。");
                continue;
            }

            // 如果selector大于0 表示获取到了关注的事件
            // selector.selectedKeys(); : 获取所有关注的事件，反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            // 获取selectorkey 迭代器
            Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();

            while (selectionKeyIterator.hasNext()) {
                // 获取到SelectionKey
                SelectionKey key = selectionKeyIterator.next();
                // 根据key对应的通道发生的事情做出相应的处理
                // 如果是OP_ACCEPT , 表示有新的客户端链接
                if (key.isAcceptable()) {
                    // 生成一个socketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    // 将SocketChannel设置为非阻塞的
                    socketChannel.configureBlocking(false);

                    System.out.println("生成了一个socketChannel:" + socketChannel);
                    System.out.println("生成了一个socketChannel哈希:" + socketChannel.hashCode());

                    // 注册到selector，关注事件是OP_READ，并关联一个buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                    Set<SelectionKey> keys = selector.keys();// 只是获取所有的通道（注册过的）
                    Set<SelectionKey> selectionKeys1 = selector.selectedKeys();// 获取所有发生了事件的通道（注册过的）
                }

                // 如果发生OP_READ事件
                if (key.isReadable()) {
                    // 通过key反向获取到对应的channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    // 获取该channel关联的buffer
                    ByteBuffer attachment = (ByteBuffer) key.attachment();
                    channel.read(attachment);
                    System.out.println("来自客户端：" + new String(attachment.array()));
                }

                // 手动从集合中移除当前的key，防止重复操作
                selectionKeyIterator.remove();
            }


        }
    }
}
