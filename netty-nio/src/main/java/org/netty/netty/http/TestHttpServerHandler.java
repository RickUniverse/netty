package org.netty.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * SimpleChannelInboundHandler 是ChannelInboundHandlerAdapter子类
 *
 * HttpObject：客户端跟服务器相互通讯的数据被封装为了HttpObject
 * @author lijichen
 * @date 2021/1/31 - 20:10
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * 读取用户数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        // 判断是不是http请求
        if (msg instanceof HttpRequest) {

            // http协议是短链接，用完就删除了，所以每次都会创建新的
            System.out.println("pipelline的hash"+ctx.pipeline().hashCode()+"TestHttpServerHandler的hash：" + this.hashCode());

            System.out.println("类型为：" + msg.getClass());
            System.out.println("客户端地址：" + ctx.channel().remoteAddress());

            HttpRequest httpRequest = (HttpRequest) msg;

            URI uri = new URI(httpRequest.uri());
            // 过滤掉某些请求
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求了favicon.ico，不做相应！");
                return;
            }

            // 信息[http]协议
            ByteBuf content = Unpooled.copiedBuffer("这是发送给浏览器的消息。。", CharsetUtil.UTF_8);

            // 构造一个响应体
            FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    content);
            httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain;charset=utf-8");
            httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());

            // 返回构造好的响应体
            ctx.writeAndFlush(httpResponse);

        }

    }
}
