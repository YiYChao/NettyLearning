package top.chao.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 *  @Description:NIO通信的服务端
 *  @author: YiYChao
 *  @Date: 2020/1/17 17:14
 *  @Version: V1.0
 */
public class NIOServer {
    public static void main(String[] args) throws Exception{
        // 获得服务端的通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 开启服务端的监听端口
        serverSocketChannel.socket().bind(new InetSocketAddress(9999));
        serverSocketChannel.configureBlocking(false);   // 设置为非阻塞
        // 获得一个Selector对象
        Selector selector = Selector.open();
        // 把服务器通道serverSocketChannel注册到selector，并设置时间为 OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        // 等待客户端连接
        while (true){
            // 等待1秒，没有客户端连接则返回
            if (selector.select(1000) == 0){
                System.out.println("Waiting 1 second for no connection...");
                continue;
            }
            // 已经连接上，获得相关的 连接的信息
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();  // 获得selectionKeys的迭代器
            while (keyIterator.hasNext()){
                SelectionKey selectionKey = keyIterator.next();     // 获得当前遍历到的selectionKey
                // 通过key判断通道是不是在进行 连接 事件
                if (selectionKey.isAcceptable()){
                    SocketChannel socketChannel = serverSocketChannel.accept();             // 响应客户端的连接
                    System.out.println("Client Connectinng:" + socketChannel.hashCode());   // 输出打印当前响应通道的信息

                    socketChannel.configureBlocking(false);     // 由于serverSocketChannel为非组测

                    // 将响应客户端连接的通道也注册到selector，并开启读事件
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                // 通过key判断通道是不是在进行 数据读入 事件
                if (selectionKey.isReadable()){
                    // 通过selectionKey获得响应客户端的连接的通道socketChannel
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();     // 获得通道对应的Buffer
                    socketChannel.read(buffer);     // 读取通道中的数据
                    System.out.println("Receiving:" + new String(buffer.array()));
                }
                keyIterator.remove();   // 手动移除keyIterator，防止重复操作
            }
        }
    }
}
