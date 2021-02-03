package org.netty.dubborpc.publicinterface;

/**
 * 接口，提供者provider和消费者consumer，都需要
 * @author lijichen
 * @date 2021/2/3 - 19:27
 */
public interface HelloService {
    String hello(String msg);
}
