package top.chao.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *  @Description:通过文件通道读取文件的内容
 *  @author: YiYChao
 *  @Date: 2020/1/16 16:28
 *  @Version: V1.0
 */
public class FileChannelRead {
    public static void main(String[] args) throws IOException {
        // 获取文件的输入流
        FileInputStream inputStream = new FileInputStream("F:/file.txt");
        //通过文件的输入流获取到文件通道fileChannel
        FileChannel fileChannel = inputStream.getChannel();

        // 定义一个字节缓冲区,空间大小为1024
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);  // 此处为节省空，1024可替换为 new Fiel("F:/file.txt").length()
        // 从通道读取数据并放入到缓冲区中
        fileChannel.read(byteBuffer);

        // 因为前面是缓冲区的写，后面没有对缓冲区进行读，所以此处可以不进行反转
        byteBuffer.flip();

        // 一次性全部获取字节缓冲区内的数据
        byte[] bytes = byteBuffer.array();  // 将内容转换成数组
        System.out.println(new String(bytes));
        // 关闭文件的输入流
        inputStream.close();
    }
}
