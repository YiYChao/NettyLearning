package top.chao.nio;

import java.nio.IntBuffer;

/**
 *  @Description: 查看Buffer的基本使用
 *  @author: YiYChao
 *  @Date: 2020/1/16 13:49
 *  @Version: V1.0
 */
public class BasicBuffer {
    public static void main(String[] args) {
        IntBuffer buffer =IntBuffer.allocate(5);    // 定义一个整型的缓冲区，大小为5
        for (int i = 0; i < 5; i++) {
            buffer.put(i * 3);  // 向缓冲区内存放数据
        }
        buffer.flip();  // 反转，进行读写切换，否则读取不到数据
        // 缓冲区内有数据，则hasRemaining返回true，否则false
        while (buffer.hasRemaining()){
            System.out.println(buffer.get());   // 取出数据
        }
    }
}
