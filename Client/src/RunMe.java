/**
 * Created by Ken on 2017/7/6.
 */

import LibraryGUI.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

public class RunMe {
        public static void main(String arg[]) {

//
            Login login=null;

            try {
                login=new Login(Thread.currentThread());
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            new Thread(login).start();

            try {
                Thread.sleep(5000000);
            } catch (InterruptedException e) {
                System.out.println("主线程被打断，开始登录");
            }

            MainWindow mainWindow = null;
                try {
                    mainWindow = new MainWindow(login.getUserName(),"127.0.0.1", 5555);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mainWindow.setVisible(true);
                mainWindow.initFileList();

            System.out.println("主线程结束");
        }
}

