package ServerOperate;

import java.io.*;
import java.net.Socket;

/**
 * Created by Ken on 2017/7/4.
 */
public class ServerFileSend extends Thread{
    Socket client;//连接客服端
    String filename;
    volatile boolean isKill;
    RandomAccessFile randomAccessFile;//便于随机读取文件
    long filePionter;//保存文件断点
    long filePionterEnd;//保存文件断点


    boolean Finish=false;
    DataOutputStream out = null;


    //传入文件名，从断点开始发送
    public ServerFileSend(String filename,Socket client,long filePionter) throws IOException {
        this.filename=filename;
        this.client=client;
        this.filePionter=filePionter;
        randomAccessFile=new RandomAccessFile(new File(filename),"r" );
        randomAccessFile.seek(filePionter);
        isKill=false;
    }

    //判断是否正常退出
    public boolean getFinish(){return Finish;}
    public void killTask(){
        isKill=true;
    }

    public void run() {
        try {
            out = new DataOutputStream(client.getOutputStream());//装配
            System.out.println("服务器开始 断点"+filePionter+"发送文件..."+filename+" 文件大小:"+randomAccessFile.length());
            byte[] buf = new byte[1024];
            int length;
            while ((length = randomAccessFile.read(buf)) > 0)//循环读取发送
            {
                out.write(buf, 0, buf.length);
                out.flush();
                filePionterEnd=randomAccessFile.getFilePointer();
                if (isKill)
                    throw new Exception();

            }
            System.out.println("服务器文件发送完成");
            System.out.println("发送断点:"+randomAccessFile.getFilePointer());
            out.close();
            randomAccessFile.close();
        } catch (IOException e) {
            System.out.println("错误:文件传输服务端发送错误");
            System.out.println("结束:文件服务端发送退出"+filename+" 发送断点"+filePionterEnd);
            return;
        } catch (Exception e) {
            System.out.println("中断: 服务器文件发送中断");
            System.out.println("结束:文件服务端发送退出"+filename+" 发送断点"+filePionterEnd);
            return;
        }
        Finish = true;
        System.out.println(filename + "从断点"+filePionter+"发送成功，正常关闭文件传输");
    }
}
