package top.chao.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *  @Description:通过文件通道将数据输出到文件
 *  @author: YiYChao
 *  @Date: 2020/1/16 16:04
 *  @Version: V1.0
 */
public class FileChannelWrite {
    public static void main(String[] args) throws IOException {
        // 要输出到文件的内容
        String str = new String("Hello Netty");
        // 获取文件的输出流
        FileOutputStream outputStream = new FileOutputStream("F:/file.txt");
        // 通过文件的输出流获取到文件的通道
        FileChannel fileChannel = outputStream.getChannel();

        // 创建字节缓冲区
        ByteBuffer byteBuffer =  ByteBuffer.allocate(1024);
        // 将字符串 写入到缓冲区
        byteBuffer.put(str.getBytes());

        byteBuffer.flip();  // 缓冲区由写变为读，需要反转一下
        // 将字节缓冲区byteBuffer的数据写入到文件通道fileChannel中
        fileChannel.write(byteBuffer);
        outputStream.close();   // 关闭文件的输出流
    }
}
