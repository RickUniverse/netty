package org.netty.dubborpc.provider;

import org.netty.dubborpc.netty.NettyServer;

/**
 * 服务提供者启动类
 * @author lijichen
 * @date 2021/2/3 - 19:31
 */
public class ServerBootstrap {
    public static void main(String[] args) {

        // 开启提供者服务
        NettyServer.startServer("127.0.0.1",8080);
    }
}
