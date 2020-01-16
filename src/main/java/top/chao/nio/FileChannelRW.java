package top.chao.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *  @Description:通过采用一个缓冲区进行文件的拷贝
 *  @author: YiYChao
 *  @Date: 2020/1/16 16:48
 *  @Version: V1.0
 */
public class FileChannelRW {
    public static void main(String[] args) throws IOException {
        // 定义一个字节缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 获得文件的输入流
        FileInputStream inputStream = new FileInputStream("src/filechannel.txt");
        // 获得文件的通道
        FileChannel fileChannelR = inputStream.getChannel();

        // 获得文件的输出流
        FileOutputStream outputStream = new FileOutputStream("F:/file.txt");
        FileChannel fileChannelW = outputStream.getChannel();

        while (true){
            byteBuffer.clear(); // 清空Buffer，否则 read!= -1 会一直读下去

            int read = fileChannelR.read(byteBuffer);// 将文件的内容读进缓冲区
            // 文件内容读取完成
            if (read == -1){
                break;
            }
            byteBuffer.flip();  // 反转，开始进行缓冲区的读操作
            fileChannelW.write(byteBuffer);     // 将缓冲区的数据写入到文件通道
        }
        // 关闭相关的流
        inputStream.close();
        outputStream.close();
    }
}
