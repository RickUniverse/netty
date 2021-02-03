package org.netty.dubborpc.provider;

import org.netty.dubborpc.publicinterface.HelloService;

/**
 * @author lijichen
 * @date 2021/2/3 - 19:29
 */
public class HelloServiceImpl implements HelloService {

    // 静态表示所有 HelloServiceImpl 实例公用一个计数器count
    private static int conut;

    // 提供者响应数据（字符串）
    @Override
    public String hello(String msg) {
        if (msg!=null) {
            return "服务端provider已经收到消息{" + msg + "},第：" + (++conut) + "次了";
        }
        return "服务器端收到了空消息！！";
    }
}
