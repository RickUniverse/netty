package org.netty.dubborpc.consumer;

import org.netty.dubborpc.netty.NettyClient;
import org.netty.dubborpc.publicinterface.HelloService;

/**
 * @author lijichen
 * @date 2021/2/3 - 21:09
 */
public class ClientBootstrap {
    // 设置协议头
    public static final String protocolHead = "HelloService#hello#";

    public static void main(String[] args) {
        // 创建消费者
        NettyClient nettyClient = new NettyClient();

        // 创建代理对象
        HelloService helloService = (HelloService) nettyClient.getBean(HelloService.class,protocolHead);

        for (;;) {

            try {
                Thread.sleep(3 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 执行远程调用
            String result = helloService.hello("hello dubbo，远程调用！！！！");

            System.out.println("\t\t");
            System.out.println("远程调用的返回结果：" + result);
        }
    }
}
