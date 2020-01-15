package top.chao.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {

    public static void main(String[] args) throws IOException {
        // 1、创建线程连接池
        ExecutorService threadPool = Executors.newCachedThreadPool();
        // 2、如果有客户端连接，就创建线程与之通信
        ServerSocket serverSocket = new ServerSocket(6666);     // 创建ServerSocket
        System.out.println("Server Starting at Port：6666");
        System.out.println("Server Thread id : " + Thread.currentThread().getId() + ",name = " + Thread.currentThread().getName());
        while (true) {
            // 监听，等待客户端连接
            System.out.println("Server Thread id : " + Thread.currentThread().getId() + ",name = " + Thread.currentThread().getName());
            System.out.println("Server wait for connecting...");
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端：" + socket.getRemoteSocketAddress() + socket.getPort());
            // 启动一个线程与之通信
            threadPool.execute(new Runnable() {
                public void run() {
                    handler(socket);
                }
            });
        }
    }

    // 编写一个handler方法，与客户端通信
    public static void handler(Socket socket) {
        try {
            System.out.println("Handler Thread id : " + Thread.currentThread().getId() + ",name = " + Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
            final InputStream inputStream = socket.getInputStream();
            // 循环读取客户端发送过来的信息
            while (true) {
                System.out.println("Handler Thread id : " + Thread.currentThread().getId() + ",name = " + Thread.currentThread().getName());
                System.out.println("Wait for Reading...");
                final int read = inputStream.read(bytes);
                // 读取到内容，即内容未读完
                if (read != -1) {
                    System.out.println(new String(bytes, 0, read));   // 将读取到的内容进行输出
                } else {
                    break;  // 内容读取完毕
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭与客户端的连接");
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
