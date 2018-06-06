package ServerOperate;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Ken on 2017/7/5.
 */
public class FileSendDaemon {
    //TODO
    public static ServerSocket serverSocket=null;
    private Socket clinet;
    private String tasklist[];
    public FileSendDaemon(int Port,String tasklist[]){
        this.tasklist=tasklist;
        try {
            if (serverSocket==null)
                serverSocket=new ServerSocket(Port);
        } catch (IOException e) {
            System.out.println("FileSendDaemon端口错误");
        }
    }

    public void StartSendFile() throws IOException {
        //服务器开始发送文件
        clinet=serverSocket.accept();
        new ServerFileSend(tasklist[2],clinet, Long.parseLong(tasklist[5])).start();
    }
}
