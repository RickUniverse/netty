package org.netty.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author lijichen
 * @date 2021/2/1 - 17:59
 */
public class TestHeartbeatServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 处理空闲事件
     * @param ctx ： 上下文
     * @param evt ： 事件
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            String IdleType = "";
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    IdleType = "发生了读空闲";
                    break;
                case WRITER_IDLE:
                    IdleType = "发生了写空闲";
                    break;
                case ALL_IDLE:
                    IdleType = "发生了读写闲";
                    break;
            }
            System.out.println(ctx.channel().remoteAddress() + "空闲事件：" + IdleType);
            System.out.println("服务器正在处理...");
            // 发生空闲关闭通道
//            ctx.channel().close();
        }
    }
}
