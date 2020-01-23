package top.chao.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务器读取线程" + Thread.currentThread().getName());
        System.out.println("Server ctx" + ctx);
        System.out.println("peek the realationship between channer and pipeline:");
        Channel channel = ctx.channel();
        ChannelPipeline pipeline = ctx.pipeline();      // 本质是一个双向链表

        // 将msg转换成bytebuf，是netty封装的NIO的ByteBuffer
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("Receive client msg:" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("Client Address:" + channel.remoteAddress());
    }

    // 数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将数据写入缓存并刷新
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello, my Client。。。", CharsetUtil.UTF_8));
    }

    // 异常处理，一般是关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
