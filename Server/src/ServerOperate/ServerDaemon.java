package ServerOperate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Ken on 2017/7/5.
 */
public class ServerDaemon extends Thread{
        ServerSocket serverSocket;
        String username;
        int Port;
        public ServerDaemon(int Port) throws IOException {
            this.Port=Port;
            serverSocket=new ServerSocket(Port);
        }

        @Override
        public void run(){
               System.out.println("服务器主程序启动\n开始监听 端口:"+Port);
            while (!Thread.currentThread().isInterrupted()){
                Socket client=new Socket();//获取单个客服端连接
                BufferedReader BR=null;
                try {
                    client=serverSocket.accept();
                } catch (IOException e) {
                    System.out.println("Socket错误");
                }
                try {
                    BR=new BufferedReader(new InputStreamReader(client.getInputStream()));
                    username=BR.readLine();
                } catch (IOException e) {
                    System.out.println("获取客服端用户名错误");
                    continue;
                }
                System.out.println(username+"连接成功");
                new ResponseClient(client,username).start();//开启一个线程处理这个客服端的请求
            }
        }
}
