package top.chao.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 *  @Description:群聊系统客户端实现
 *  @author: YiYChao
 *  @Date: 2020/1/18 15:17
 *  @Version: V1.0
 */
public class GroupChartClient {
    private final String HOST = "127.0.0.1";    // 服务端IP地址
    private final int PORT = 9998;              // 服务端端口号
    private Selector selector;
    private SocketChannel socketChannel;
    private String clientName;

    // 构造函数，完成初始化定义
    public GroupChartClient() throws IOException {
        selector = Selector.open();
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));  // 连接服务器
        socketChannel.configureBlocking(false);     // 设置为非组测
        socketChannel.register(selector, SelectionKey.OP_READ);     // 将channel注册到selector
        clientName = socketChannel.getLocalAddress().toString().substring(1);   // 获得客户端名称

        System.out.println(clientName + "is Ready...");
    }

    // 向服务器发送消息
    public void sendMsg(String msg){
        msg = clientName + " broadcasting :" + msg;
        try {
            socketChannel.write(ByteBuffer.wrap(msg.getBytes()));    // 向服务端发送消息
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读取服务端响应回来的消息
    public void readMsg(){
        try {
            int select = selector.select(); // 有活跃通道
            if (select > 0){
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isReadable()){
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        channel.read(buffer);
                        System.out.println(new String(buffer.array()).trim());     // 将读取到的内容进行输出
                    }
                    iterator.remove();  // 防止重复操作
                }
            }else{
                System.out.println("没有可用的通道");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        GroupChartClient client = new GroupChartClient();
        new Thread(){
          public void run(){
              while (true){
                  client.readMsg();
                  try {
                      Thread.sleep(3000);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
              }
          }
        }.start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String msg = scanner.nextLine();
            client.sendMsg(msg);
        }
    }
}
