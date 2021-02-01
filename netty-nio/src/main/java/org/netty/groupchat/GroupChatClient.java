package org.netty.groupchat;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author lijichen
 * @date 2021/1/30 - 17:47
 */
public class GroupChatClient {
    // 定义相关属性
    private final String HOST = "127.0.0.1";
    // 服务器端口
    private final int POST = 6666;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    public GroupChatClient() throws IOException {
        selector = Selector.open();
        socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",POST));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        username = socketChannel.getLocalAddress().toString().substring(1);

        System.out.println(username+"初始化完成！");
    }

    // 发送信息
    public void sendInfo(String msg) {
        String info = username + " 说：" + msg;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 读取消息
    public void readInfo() {
        try {
            int readChannel = selector.select(2000);
            // 如果有可用的通道
            if (readChannel > 0) {
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {

                    SelectionKey selectionKey = keyIterator.next();
                    if (selectionKey.isReadable()) {
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        channel.read(buffer);
                        System.out.println(new String(buffer.array()));
                    }
                }
                // 必须有这一步！！！！！！！！防止重复操作
                keyIterator.remove();
            } else {
                //System.out.println("没有可用的通道！");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
        GroupChatClient chatClient = new GroupChatClient();

        // 每三秒接受一次消息
        new Thread(){
            @Override
            public void run() {
                while (true) {
                    chatClient.readInfo();

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();


        // 往服务器发送消息
        Scanner sca = new Scanner(System.in);
        while (sca.hasNextLine()) {
            String msg = sca.nextLine();
            chatClient.sendInfo(msg);
        }
    }
}
