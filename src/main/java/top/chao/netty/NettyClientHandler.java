package top.chao.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client Handler" + ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello, Server。。。", CharsetUtil.UTF_8));
    }

    // 当通道有读取事件时会触发此事件
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // 将msg转换成bytebuf，是netty封装的NIO的ByteBuffer
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("Receive Server replay:" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("Server Address:" + ctx.channel().remoteAddress());
    }


    // 异常处理，一般是关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
