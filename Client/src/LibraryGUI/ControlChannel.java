package LibraryGUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Ken on 2017/7/6.
 */
public class  ControlChannel {
    private String IP;
    private Socket Server;
    private int Port;
    private BufferedReader BR;
    private PrintWriter PW;
    public ControlChannel(String IP, int port) throws IOException {
        this.IP = IP;
        this.Port =port;
        Server=new Socket(IP,Port);
        BR=new BufferedReader(new InputStreamReader(getServer().getInputStream()));
        PW=new PrintWriter(Server.getOutputStream());
    }

    public Socket getServer() {
        return Server;
    }


    //发送一行消息
    public boolean SendInfoToServer(String info){
        if (info.isEmpty())
            return false;
        PW.println(info);
        PW.flush();
        return true;
    }

    //读取一行消息
    public String GetInfoFromSever() throws IOException {
        String readInfo=null;
        readInfo=BR.readLine();
        return readInfo;
    }

}
