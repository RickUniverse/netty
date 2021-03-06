package org.netty.netty.codec2;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.netty.netty.codec.StudentPOJO;

/**
 * @author lijichen
 * @date 2021/1/31 - 16:31
 */

/**
 * 定义一个handler，才能称之为一个handler
 */
//public class NettyServerHandler extends ChannelInboundHandlerAdapter {
public class NettyServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {

    /**
     * 读取实际数据，读取客户端发送的数据
     * @param ctx ： 上下文对象，含有管道pipeline，通道channel，地址
     * @param msg ： 就是客户端发送的数据，默认是object
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {
        // 解决阻塞问题方案：用户程序自定义的普通任务，任务添加到taskQueue中
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {

                MyDataInfo.MyMessage.DataType dataType = msg.getDataType();
                if (dataType == MyDataInfo.MyMessage.DataType.StudentType) {
                    MyDataInfo.Student student = msg.getStudent();

                    System.out.println("student:" + student.getName() + student.getId());

                } else if (dataType == MyDataInfo.MyMessage.DataType.WorkerType){
                    MyDataInfo.Worker Worker = msg.getWorker();

                    System.out.println("Worker:" + Worker.getAge() + Worker.getId());
                } else {
                    System.out.println("数据类型不正确");
                }

                try {
//                    Thread.sleep(10 * 1000);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                    System.out.println("发生异常！");
                }
                ctx.writeAndFlush(Unpooled.copiedBuffer("我是非阻塞消息,普通任务",CharsetUtil.UTF_8));
            }
        });
    }

    /**
     * 数据读取完毕后
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // writeAndFlush 是write + flush
        // 将数据写入到缓存并刷新
        // 对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,client..!",CharsetUtil.UTF_8));
    }

    /**
     * 发生异常进行捕捉
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }


}
