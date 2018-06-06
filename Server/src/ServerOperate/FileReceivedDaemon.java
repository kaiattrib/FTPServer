package ServerOperate;

import sun.awt.SunHints;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Ken on 2017/7/5.
 */
public class FileReceivedDaemon{
    //TODO
    public static ServerSocket serverSocket=null;
    private Socket clinet;
    private String tasklist[];
    public FileReceivedDaemon(int Port,String tasklist[]){
        this.tasklist=tasklist;
        try {
            if (serverSocket==null)
            serverSocket=new ServerSocket(Port);
        } catch (IOException e) {
            System.out.println("FileReceivedDaemon端口错误");
        }
    }

    public void StartReceivedFile() throws IOException {
        //服务器开始接收
        System.out.println("服务器开始 建立文件接收线程");
        clinet=serverSocket.accept();
        new ServerFileReceived(tasklist[3],clinet, Long.parseLong(tasklist[5]),tasklist).start();
    }
}
