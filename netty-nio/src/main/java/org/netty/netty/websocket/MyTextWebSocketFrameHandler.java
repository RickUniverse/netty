package org.netty.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

/**
 * TextWebSocketFrame : 表示一个文本帧
 * @author lijichen
 * @date 2021/2/1 - 19:07
 */
public class MyTextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器收到消息：" + msg.text());

        // 回复消息
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间" + LocalDateTime.now() + "：" + msg.text()));
    }

    // 当客户端连接时 触发
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // id表示唯一的值 longText是唯一的值   shortText不是唯一的
        System.out.println("handlerAdded 被调用：" + ctx.channel().id().asLongText());
        System.out.println("handlerAdded 被调用：" + ctx.channel().id().asShortText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved 被调用：" + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生了：" + cause.getMessage());
        ctx.channel();
    }
}
