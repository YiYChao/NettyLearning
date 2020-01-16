package top.chao.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 *  @Description:通过文件通道进行文件的拷贝
 *  @author: YiYChao
 *  @Date: 2020/1/16 17:27
 *  @Version: V1.0
 */
public class FileChannelCopy {
    public static void main(String[] args) throws IOException {
        FileInputStream inputStream = new FileInputStream("F:/land.jpg");
        FileChannel inChannel = inputStream.getChannel();   // 获得文件的输入通道

        FileOutputStream outputStream = new FileOutputStream("F:/land.copy2.jpg");
        FileChannel outChannel = outputStream.getChannel();     // 获得文件的输出通道

        // 通过TransferFrom方法实现文件的拷贝
//        outChannel.transferFrom(inChannel,0,inChannel.size());
        // 通过TransferTo方法实现文件的拷贝
        inChannel.transferTo(0,inChannel.size(),outChannel);

        // 关闭相关资源
        inChannel.close();
        outChannel.close();
        inputStream.close();
        outputStream.close();
    }
}
