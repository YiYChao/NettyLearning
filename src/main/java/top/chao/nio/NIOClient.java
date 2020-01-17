package top.chao.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 *  @Description:NIO通信的客户端
 *  @author: YiYChao
 *  @Date: 2020/1/17 17:43
 *  @Version: V1.0
 */
public class NIOClient {
    public static void main(String[] args) throws Exception{
        // 开启客户端
        SocketChannel socketChannel = SocketChannel.open();
        // 连接地址
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 9999);
        // 还没有连接成功
        if (!socketChannel.connect(address)){
            // 还没有完成连接
            while (!socketChannel.finishConnect()){
                System.out.println("Waiting for finishing connection! doing other jobs!!!");
            }
        }
        // 连接成功，发送数据
        String str = "Hello, Netty Learning Person";
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());    // 按需分配，获得缓冲区
        socketChannel.write(buffer);    // 将缓冲区的数据写入通道
        System.in.read();   // 等待，用于保存连接
    }
}
