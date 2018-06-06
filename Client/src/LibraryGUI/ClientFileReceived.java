package LibraryGUI;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.sql.SQLException;

/**
 * Created by Ken on 2017/7/4.
 */
public class ClientFileReceived extends Thread{
    private long filePionterSzie;
    Socket server;//连接服务端
    String filename;
    boolean isKill;//标记当前任务是否要中断；
    RandomAccessFile randomAccessFile;//便于随机读取文件
    long filePionterStart;
    long filePionterEnd;
    boolean Finish=false;
    DataInputStream in = null;
    int ProgressBarValue=0;
    String tasklist[];

    //传入文件名，从断点开始接收
    public ClientFileReceived(String ServerIP, int Port,String filename,long filePionter,String tasklist[]) throws IOException {
        isKill=false;
        this.filePionterSzie=Long.valueOf(tasklist[6]);
        this.tasklist=tasklist;
        this.filename=filename;
        this.server=new Socket(ServerIP,Port);
        this.filePionterStart=filePionter;
        randomAccessFile=new RandomAccessFile(new File(filename),"rw" );//没有w,如果文件不存在，会自动创建
        randomAccessFile.seek(filePionterStart);
    }

    //判断是否正常退出
    public boolean getFinish(){return Finish;}

    //设置任务结束
    public void killTask(){
        isKill=true;
    }


    //返回更新的断点
    public long getFilePionterEnd(){
        return filePionterEnd;
    }

    public int getProgressBarValue(){
        return ProgressBarValue;
    }
    public void run() {
        try {
            in = new DataInputStream(server.getInputStream());//装配
            System.out.println("客服端开始接收文件...");
            byte[] buf = new byte[1024];
            int length;
            while ((length = in.read(buf)) > 0)//循环读取接收的文件并写入文件
            {
                randomAccessFile.write(buf, 0, buf.length);
                filePionterEnd = randomAccessFile.getFilePointer();//保存读取到的断点
                ProgressBarValue= Math.toIntExact(filePionterEnd * 100 / filePionterSzie);
                System.out.println("ProgressBarValue"+ProgressBarValue);
                if (Thread.currentThread().isInterrupted())
                    throw new Exception();
            }
            System.out.println("客服端文件接收完成");
            in.close();
            randomAccessFile.close();
        }
        catch (IOException e) {//产生错误 保存断点，如果没有断点记录，会插入记录
            tasklist[5]=filePionterEnd+"";
            try {
                SqlUtils.PionterSQL(tasklist);
            } catch (SQLException e1) {
                System.out.println("文件Pionter修改失败");
            }
                try {
                    in.close();
                    server.close();
                } catch (IOException e1) {
                    System.out.println("Socket关闭错误");
                }
            System.out.println("错误:文件传输服务端接收文件错误" +"接收断点: "+getFilePionterEnd());
            return;
        }
        catch (Exception e) {
            tasklist[5]=filePionterEnd+"";
            try {
                SqlUtils.PionterSQL(tasklist);
            } catch (SQLException e1) {
                System.out.println("文件Pionter修改失败");
            }
            System.out.println("中断:文件传输服务端接收文件错误" +"接收断点: "+getFilePionterEnd());
                    try {
                        in.close();
                        server.close();
                    } catch (IOException e1) {
                        System.out.println("Socket关闭错误");
                    }
            return;
        }
        if (filePionterEnd>=filePionterSzie) {
            Finish = true;
            System.out.println("接收完全"+filename + "从断点 " + filePionterStart + "接收到" + filePionterEnd + "正常关闭文件传输");
            //任务完成，会删除这条记录。如果不存在 会返回
            try {
                SqlUtils.DeleteSQL(tasklist);
            } catch (SQLException e) {
                System.out.println("记录删除失败");
            }
        }
        else {
            tasklist[5]=filePionterEnd+"";
            try {
                SqlUtils.PionterSQL(tasklist);
            } catch (SQLException e1) {
                System.out.println("文件Pionter修改失败");
            }
            System.out.println("接收部分"+filename + "从断点" + filePionterStart + "接收到" + filePionterEnd + "正常关闭文件传输");
        }
    }
}
