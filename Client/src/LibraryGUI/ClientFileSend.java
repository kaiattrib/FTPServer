package LibraryGUI;

import java.io.*;
import java.net.Socket;

/**
 * Created by Ken on 2017/7/4.
 */
public class ClientFileSend extends Thread{
    Socket server ;//连接客服端
    String filename;
    volatile boolean isKill;
    RandomAccessFile randomAccessFile;//便于随机读取文件
    long filePionterStart;//保存文件断点
    long filePionterEnd;//保存文件断点
    long filePionterSize;
    boolean Finish=false;
    DataOutputStream out = null;
    BufferedReader BR;
    int ProgressBarValue;
    long serverRceivedSize;
    //传入文件名，从断点开始发送
    public ClientFileSend(String ServerIP, int Port,String filename,long filePionter) throws IOException {
        this.filename=filename;
        this.server=new Socket(ServerIP,Port);
        this.filePionterStart=filePionter;
        randomAccessFile=new RandomAccessFile(new File(filename),"r" );
        this.filePionterSize=randomAccessFile.length();
        randomAccessFile.seek(filePionterStart);
        isKill=false;
    }

    //判断是否正常退出
    public boolean getFinish(){return Finish;}
    public void killTask(){
        isKill=true;
    }

    public int getProgressBarValue(){
        return ProgressBarValue;
    }
    public void run() {
        try {
            BR=new BufferedReader(new InputStreamReader(server.getInputStream()));//获取服务端发送过来的接收信息
            out = new DataOutputStream(server.getOutputStream());//装配
            System.out.println("开始: 客服端开始从断点"+filePionterStart+"发送文件"+"文件大小:"+randomAccessFile.length());
            byte[] buf = new byte[1024];
            int length;
            while ((length = randomAccessFile.read(buf)) > 0)//循环读取发送
            {
                out.write(buf, 0, buf.length);
                out.flush();
                filePionterEnd=randomAccessFile.length();
                serverRceivedSize=Long.valueOf(BR.readLine());//从服务端读取收到的大小
                ProgressBarValue= Math.toIntExact(100*serverRceivedSize / filePionterSize);
                if (Thread.currentThread().isInterrupted())
                    throw new Exception();
            }
            out.close();
            randomAccessFile.close();
        } catch (IOException e) {
            System.out.println("错误:文件传客服端发送错误");
            System.out.println("结束:文件服务端发送退出"+filename+" 发送断点"+filePionterEnd);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            try {
                out.close();
                server.close();
            } catch (IOException e1) {
            }
            System.out.println("Scoket流关闭错误");
            return;
        } catch (Exception e) {
            System.out.println("中断: 客服端文件发送中断");
            System.out.println("结束:文件服务端发送退出"+filename+" 发送断点"+filePionterEnd);
            try {
                out.close();
                server.close();
            } catch (IOException e1) {
                System.out.println("Scoket流关闭错误");
            }
            return;
        }
        Finish = true;
        System.out.println(filename + "从断点"+filePionterStart+"发送成功，正常关闭文件传输");
    }
}
