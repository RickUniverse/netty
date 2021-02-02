package org.netty.netty.protocoltcp;

/**
 * 协议包
 * @author lijichen
 * @date 2021/2/2 - 17:27
 */
public class MessageProtocol {
    private int len; // 关键
    private byte[] content;

    public int getLen() {
        return len;
    }

    public MessageProtocol setLen(int len) {
        this.len = len;
        return this;
    }

    public byte[] getContent() {
        return content;
    }

    public MessageProtocol setContent(byte[] content) {
        this.content = content;
        return this;
    }
}
