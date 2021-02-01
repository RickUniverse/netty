package org.netty.netty.codec2.org.netty.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lijichen
 * @date 2021/1/29 - 15:52
 */
public class BIOServer {
    public static void main(String[] args) throws Exception {
        // 创建线程池
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

        ServerSocket serverSocket = new ServerSocket(6666);

        System.out.println("服务器启动了！");

        while (true) {
            // 监听等待客户端连接
            final Socket socket = serverSocket.accept();
            System.out.println("连接到客户端！");

            //单独写一个方法与之通信
            newCachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    // 调用
                    handler(socket);
                }
            });
        }

    }

    public static void handler(Socket socket) {

        System.out.println(Thread.currentThread().getName()+"::::::::::"+Thread.currentThread().getId());

        try {
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();

            while (true) {

                System.out.println(Thread.currentThread().getName()+"::::::::::"+Thread.currentThread().getId());

                int read = inputStream.read(bytes);
                if (read != -1) {
                    System.out.println(new String(bytes,0,read));
                } else {
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭和client的链接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
