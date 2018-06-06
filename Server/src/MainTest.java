/**
 * Created by Ken on 2017/7/4.
 */

import ServerOperate.*;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;


public class MainTest {

     public static void main(String arg[]) throws IOException, SQLException, ClassNotFoundException {

         new SqlUtils();
         ServerDaemon ServerMain=new ServerDaemon(5555);
            ServerMain.start();
//          File file=new File("D:\\");
//            String str[]=DirectoryFunction.scaner(new File("D:\\CUDA")).split("%");
//
//         for (int i = 0; i < str.length; i++) {
//             System.out.println(str[i]);
//         }
//         ServerSocket serverSocket=new ServerSocket(5555);
//
//
//             Socket client=serverSocket.accept();
             //测试 服务器 文件接收成功
//             ServerFileReceived serverFileReceived=new ServerFileReceived("D:\\JavaTest.png",client,0);
//             serverFileReceived.start();//开始线程
//
//             //测试 服务器 文件发送
//            ServerFileSend serverFileSend=new ServerFileSend("D:\\JavaTest.mp4",client,0);
//            serverFileSend.start();

//         while (true){
//             Socket client2=serverSocket.accept();
//             ServerFileReceived serverFileReceived2=new ServerFileReceived("D:\\JavaTest2.png",client,400);
//             serverFileReceived2.start();//开始线程
//         }
//         try {
//             SqlUtils sqlUtils = new SqlUtils();
//         } catch (Exception e) {
//             e.printStackTrace();
//             System.out.println("SqlUtils类初始化异常");
//         }
//
//
//         try {
//             SqlUtils.QuerySQL("SELECT * FROM TEST.user");
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }


         //用户登录测试 OK
//         try {
//             System.out.println(SqlUtils.isLegalLogin("testuser","123456"));
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }

     }
}
