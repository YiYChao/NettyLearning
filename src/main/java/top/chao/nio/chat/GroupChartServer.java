package top.chao.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 *  @Description:群聊系统服务端实现
 *  @author: YiYChao
 *  @Date: 2020/1/18 14:33
 *  @Version: V1.0
 */
public class GroupChartServer {
    // 全局属性定义
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private static final int PORT = 9998;

    // 构造函数，进行初始化的定义
    public GroupChartServer() {
        try {
            selector = Selector.open(); //开启选择器
            serverSocketChannel = ServerSocketChannel.open();   // 开启服务端的通道
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT)); // 绑定端口
            // 设置为非阻塞模式
            serverSocketChannel.configureBlocking(false);
            // 注册到选择器
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void listen(){
        try {
            while (true){
                int select = selector.select();     // 等待
                if (select > 0){    // 由事件需要处理
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();   // 进行迭代遍历
                    if (iterator.hasNext()){
                        SelectionKey selectionKey = iterator.next();
                        if (selectionKey.isAcceptable()){   // 连接事件
                            SocketChannel socketChannel = serverSocketChannel.accept();     // 处理连接请求
                            socketChannel.configureBlocking(false);     // 设置为非组测
                            // 将处理客户端的通道注册到selector
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            System.out.println(socketChannel.getRemoteAddress().toString().substring(1) + "上线了😀");  // subString 去掉之前的 /
                        }
                        if (selectionKey.isReadable()){     // 读取事件
                            readData(selectionKey);
                        }
                        iterator.remove();  // 手动移除，防止重复操作
                    }

                }else {
                    System.out.println("Server waiting...");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void readData(SelectionKey selectionKey){
        SocketChannel channel = null;
        try {
            channel = (SocketChannel) selectionKey.channel();   // 获得通道
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int read = channel.read(buffer);
            if (read > 0){  // 读取到内容
                String msg = new String(buffer.array());        // 将数据转换成字符出
                System.out.println("Receive Client:" + msg);

                // 调用方法，进行消息转发
                forwardInfo(channel, msg);
            }
        }catch (IOException e){
            try {
                System.out.println(channel.getRemoteAddress().toString().substring(1) + " offline...");
                selectionKey.cancel();  // 取消注册
                channel.close();    // 关闭通道
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void forwardInfo(SocketChannel channel, String msg) throws IOException {
        System.out.println("Forwarding information...");
        for (SelectionKey selectionKey : selector.keys()){
            SelectableChannel selectableChannel = selectionKey.channel();
            // 确定为网络通道，且不是自身
            if (selectableChannel instanceof SocketChannel && selectableChannel != channel){
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());    // 按需开辟缓冲区
                ((SocketChannel) selectableChannel).write(buffer);      // 将数据从buffer写入缓冲区
            }
        }
    }

    public static void main(String[] args) {
        GroupChartServer server = new GroupChartServer();   // 创建服务器对象
        server.listen();
    }
}
