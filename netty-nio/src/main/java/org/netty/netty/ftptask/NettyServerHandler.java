package org.netty.netty.ftptask;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author lijichen
 * @date 2021/1/31 - 16:31
 */

/**
 * 定义一个handler，才能称之为一个handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    /**
     * ！！！！！！
     * 1，static ： 所有NettyServerHandler 共享
     * 2，任务线程池，可以将耗时任务提交到该线程池
     * 3，这里创建了16个线程
     * 4，用于让耗时任务异步执行，不再阻塞
     */
    static final EventExecutorGroup TASKGROUP = new DefaultEventExecutorGroup(16);


    /**
     * 读取实际数据，读取客户端发送的数据
     * @param ctx ： 上下文对象，含有管道pipeline，通道channel，地址
     * @param msg ： 就是客户端发送的数据，默认是object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("NettyServerHandler channelRead方法线程:" + Thread.currentThread().getName());
        
        // 提交到任务线程中，异步执行
        TASKGROUP.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {

                System.out.println("NettyServerHandler channelRead方法 execute TASKGROUP.submit call线程:" + Thread.currentThread().getName());

                ByteBuf byteBuf = (ByteBuf) msg;

                byte[] bytes = new byte[((ByteBuf) msg).readableBytes()];

                String message = new String(bytes, "utf-8");

                Thread.sleep(10 * 1000);

                System.out.println(message);

                // 发送
                ctx.writeAndFlush(Unpooled.copiedBuffer("我是非阻塞消息,TASKGROUP.submit 任务线程处理的消息",CharsetUtil.UTF_8));

                return null;
            }
        });
        TASKGROUP.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {

                System.out.println("NettyServerHandler channelRead方法 execute TASKGROUP.submit call线程:" + Thread.currentThread().getName());

                ByteBuf byteBuf = (ByteBuf) msg;

                byte[] bytes = new byte[((ByteBuf) msg).readableBytes()];

                String message = new String(bytes, "utf-8");

                Thread.sleep(10 * 1000);

                System.out.println(message);

                // 发送
                ctx.writeAndFlush(Unpooled.copiedBuffer("我是非阻塞消息,TASKGROUP.submit 任务线程处理的消息",CharsetUtil.UTF_8));

                return null;
            }
        });
        TASKGROUP.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {

                System.out.println("NettyServerHandler channelRead方法 execute TASKGROUP.submit call线程:" + Thread.currentThread().getName());

                ByteBuf byteBuf = (ByteBuf) msg;

                byte[] bytes = new byte[((ByteBuf) msg).readableBytes()];

                String message = new String(bytes, "utf-8");

                Thread.sleep(10 * 1000);

                System.out.println(message);

                // 发送
                ctx.writeAndFlush(Unpooled.copiedBuffer("我是非阻塞消息,TASKGROUP.submit 任务线程处理的消息",CharsetUtil.UTF_8));

                return null;
            }
        });

        // 解决问题方案：用户程序自定义的普通任务，任务添加到taskQueue中
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("NettyServerHandler channelRead方法 execute1 普通任务 线程:" + Thread.currentThread().getName());
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                    System.out.println("发生异常！");
                }
                ctx.writeAndFlush(Unpooled.copiedBuffer("我是非阻塞消息,普通任务",CharsetUtil.UTF_8));
            }
        });

        // 用户定时任务,任务添加到scheduleTaskQueue
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("NettyServerHandler channelRead方法 execute2 定时任务 线程:" + Thread.currentThread().getName());
                try {
                    Thread.sleep(5 * 1000);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                    System.out.println("发生异常！");
                }
                ctx.writeAndFlush(Unpooled.copiedBuffer("我是非阻塞消息，定时任务",CharsetUtil.UTF_8));
            }
        },5, TimeUnit.SECONDS);

        System.out.println("这句话没有阻塞。。。。");

//        System.out.println("server ctx ==>" + ctx);
//        // 将msg转为netty提供的bytebuffer
//        ByteBuf byteBuf = (ByteBuf) msg;
//        System.out.println("客户端发送的消息为：" + byteBuf.toString(CharsetUtil.UTF_8));
//        System.out.println("客户端发送的地址为：" + ctx.channel().remoteAddress());
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
