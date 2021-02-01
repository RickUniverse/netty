package org.netty.nio;

import java.nio.IntBuffer;

/**
 * @author lijichen
 * @date 2021/1/29 - 17:08
 */

/**
 * buffer 的使用
 */
public class BasicBuffer {
    public static void main(String[] args) {
        // 创建一个buffer 大小为5，可以存放五个int
        IntBuffer intBuffer = IntBuffer.allocate(5);

        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i * 2);
        }

        // 只读的buffer
        IntBuffer asReadOnlyBuffer = intBuffer.asReadOnlyBuffer();

        // 如何从buffer读写数据
        // 读写转换
        intBuffer.flip();

        // 设置属性
        intBuffer.position(1);
        intBuffer.limit(4);

        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());// 每次获取指针移动一次
        }
    }
}
