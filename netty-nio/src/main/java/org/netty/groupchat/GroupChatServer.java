package org.netty.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @author lijichen
 * @date 2021/1/30 - 16:41
 */
public class GroupChatServer {

    // 定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6666;

    // 构造器，初始化
    public GroupChatServer() {
        try {
            // 获取选择器
            selector = Selector.open();
            // ServerSocketChannel , 通道
            listenChannel = ServerSocketChannel.open();
            // 绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            // 非阻塞
            listenChannel.configureBlocking(false);
            // 注册到selector中
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 监听方法
    private void listen() {

        try {
            while (true) {
                int select = selector.select();
                // 如果有事件发生
                if (select > 0) {
                    // 得到selector集合
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()) {
                        // 取出SelectionKey
                        SelectionKey key = keyIterator.next();

                        if (key.isAcceptable()) {
                            SocketChannel socketChannel = listenChannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector,SelectionKey.OP_READ);
                            // 提示
                            System.out.println(socketChannel.getRemoteAddress() + "==> 上线!");
                        }

                        // 如果是读的方法
                        if (key.isReadable()) {
                            readData(key);
                        }

                        // 防止重复处理
                        keyIterator.remove();

                    }
                } else {
                    System.out.println("等待。。。。");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

    }

    // 读取数据
    private void readData(SelectionKey selectionKey) {

        // 获取到关联的SocketChannel
        SocketChannel socketChannel = null;

        try {
            // 获取关联的通道
            socketChannel = (SocketChannel) selectionKey.channel();
            // 创建buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            // 读取数据
            int count = socketChannel.read(buffer);
            if (count > 0) {
                String msg = new String(buffer.array());
                System.out.println("来自：" + msg + "的消息");
                // 向其他客户端转发消息
                sendMessage2OtherClient(msg, socketChannel);
            }
        } catch (Exception e) {
//            e.printStackTrace();
            try {
                System.out.println(socketChannel.getRemoteAddress() + "==> 掉线了!");
                // 取消注册
                selectionKey.cancel();
                // 关闭通道
                socketChannel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }

    // 转发消息给其他客户端
    private void sendMessage2OtherClient(String msg, SocketChannel self) throws IOException {

        System.out.println("消息转发中....");
        // 便利得到所有客户端并排除自己
        for (SelectionKey key : selector.keys()) {
            // 通过key取出对应的socketChannel
            Channel targetChannel = key.channel();

            // 排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                SocketChannel dest = (SocketChannel) targetChannel;
                // 转发操作
                dest.write(ByteBuffer.wrap(msg.getBytes()));
            }

        }

    }

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
