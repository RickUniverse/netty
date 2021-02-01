package org.netty.netty.codec2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.netty.netty.codec.StudentPOJO;

import java.util.Random;

/**
 * @author lijichen
 * @date 2021/1/31 - 17:11
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当通道就绪就会触发该方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        int random = new Random().nextInt(3);

        MyDataInfo.MyMessage myMessage = null;

        // 返回一个Student类型
        if (random == 0) {
            myMessage = MyDataInfo.MyMessage.newBuilder()
                    .setDataType(MyDataInfo.MyMessage.DataType.StudentType)
                        .setStudent(MyDataInfo.Student.newBuilder().setId(2221).setName("超级无敌鸭子！！！！！！").build())
                    .build();

        } else { // 返回一个Worker类型
            myMessage = MyDataInfo.MyMessage.newBuilder()
                    .setDataType(MyDataInfo.MyMessage.DataType.WorkerType)
                        .setWorker(MyDataInfo.Worker.newBuilder().setId(2221).setAge(123123).build())
                    .build();
        }

        ctx.writeAndFlush(myMessage);
    }

    /**
     * 通道有读取事件时就会发生
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("服务器的消息："+new String(byteBuf.toString(CharsetUtil.UTF_8)));
        System.out.println("服务器的地址："+ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 打印错误
        cause.printStackTrace();
        ctx.close();
    }
}
