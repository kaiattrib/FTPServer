package ServerOperate;

import java.io.*;
import java.net.Socket;

/**
 * Created by Ken on 2017/7/5.
 */
public class ResponseClient extends Thread{
    String username;
    Socket client;
    String tasklist[];
    String infoFormClinet[];
    BufferedReader BR;
    PrintWriter PW;
    public ResponseClient(Socket client, String username){
        this.client=client;
        this.username=username;
        tasklist=new String[7];
    }
    public void run(){
                System.out.println(username+"线程启动成功");
                File userHomeDir=new File("D:\\FTPHome\\"+username);
                if (!userHomeDir.exists())
                    userHomeDir.mkdirs();//如果家目录不存在，就创建
            try {
                BR=new BufferedReader(new InputStreamReader(client.getInputStream()));
                PW=new PrintWriter(client.getOutputStream());
            } catch (IOException e) {
                System.out.println(username+"输入流获取错误");
            }

        while (true){
                String infoTemp=new String();
            try {
                infoTemp=BR.readLine();
                infoFormClinet=infoTemp.split("%");//消息拆分读取
            } catch (IOException e) {
                System.out.println("消息读取错误请检查"+username+"用户断开\n" +username+"线程结束");
                SqlUtils.Reset(username);
                return;
            }
            if (infoTemp.isEmpty())
                continue;
            System.out.println("服务器收到命令 开始解析"+infoTemp);
            if (infoFormClinet==null)
                continue;
            else {
                if ("File".equals(infoFormClinet[0]))
                    FileOperate(infoFormClinet);
                else if ("Dir".equals(infoFormClinet[0]))
                    DirOperate(infoFormClinet);
                else if ("GetInfo".equals(infoFormClinet[0]))
                    InfoToClient(infoFormClinet);
                else if ("Command".equals(infoFormClinet[0])){
                    if ("Exit".equals(infoFormClinet[1]))
                        return;//线程结束
                }
            }
        }
    }

    //文件的操作
    public void FileOperate(String[] infoFormClient){
        System.out.println("收到文件操作");
        //文件上传和下载File%Up%FileName%FilePathFrom%FilePathTo%FileSize%Point
        if ("Up".equals(infoFormClinet[1])||"Down".equals(infoFormClient[1])) {
                tasklist[0]=username;
                tasklist[1]=infoFormClinet[2];
                tasklist[2]=infoFormClinet[3];
                tasklist[3]=infoFormClinet[4];
                tasklist[4]=infoFormClinet[1];
                tasklist[5]=infoFormClinet[6];
                tasklist[6]=infoFormClinet[5];
                try {
                    if ("Up".equals(infoFormClient[1])) {
                        PW.println("Reply%UpOK");
                        PW.flush();
                        System.out.println("Reply%UpOK发到客服端");
                        new FileReceivedDaemon(6666, tasklist).StartReceivedFile();
                    }
                    else {
                        PW.println("Reply%DownOK");
                        System.out.println("Reply%DownOK到客服端");
                        PW.flush();
                        new FileSendDaemon(7777, tasklist).StartSendFile();
                    }
                } catch (IOException e) {
                    System.out.println(infoFormClient[1]+" 文件新开线程失败");
                }
            return;
            }

        if ("DeleteFile".equals(infoFormClient[1])) {
                FileFunction.Delete(new File(infoFormClinet[2]));
                return;
            }

        if ("ReNameFile".equals(infoFormClinet[1])){
                FileFunction.Rename(new File(infoFormClinet[2]),new File(infoFormClinet[3]));
                return;
            }

        if ("MoveFile".equals(infoFormClinet[1]))
            {
                FileFunction.Move(new File(infoFormClinet[2]),new File(infoFormClinet[3]));
                return;
            }
    }

    //目录的操作
    public void DirOperate(String[] infoFormClient){
        System.out.println("收到目录操作");
        //TODO
        if ("CreateFolder".equals(infoFormClient[1])) {
            DirectoryFunction.createfolder(new File(infoFormClient[2]));
            return;
        }


        if ("DeleteFolder".equals(infoFormClient[1])) {
            DirectoryFunction.deletefolders(new File(infoFormClient[2]));
            return;
        }


        if ("RenametoFoldet".equals(infoFormClient[1])) {
            DirectoryFunction.renametofolder(new File(infoFormClient[2]),new File(infoFormClient[3]));
            return;
        }

        if ("MoveFolder".equals(infoFormClient[1])) {
            DirectoryFunction.movefolder(new File(infoFormClient[2]),new File(infoFormClient[3]));
            return;
        }
    }
    //信息的获取输出
    public void InfoToClient(String[] infoFormClient){
        System.out.println("收到信息读取操作");
        if ("FileSize".equals(infoFormClinet[1])){
            File file=new File(infoFormClinet[2]);
            String FileSize=null;
            try {
                long fileSize=new RandomAccessFile(file,"rw").length();
                FileSize=fileSize+"";
            } catch (IOException e) {
                e.printStackTrace();
            }
            PW.println("Reply%FileSize%"+FileSize);
            System.out.println("Reply%FileSize%"+infoFormClinet[2]+FileSize);
            PW.flush();
            System.out.println("推出"+FileSize);
        }
        else {
            String string=new String("DirInfo%");
            string=string+DirectoryFunction.scaner(new File(infoFormClient[1]));
            PW.println(string);
            PW.flush();
            System.out.println("推出"+string);
            return;
        }
    }
}
