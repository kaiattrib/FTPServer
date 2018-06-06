package LibraryGUI;

import javax.print.attribute.standard.DocumentName;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;

public class TaskProgressBar extends Thread {
    JProgressBar bar;
    ClientFileReceived ThreadDown;
    ClientFileSend ThreadUp;
    boolean SendMark=false;
    boolean ReceivedMark=false;
    boolean PauseMark=false;
    boolean taskMark=false;
    boolean DownFileMark=false;
    boolean UpFileMark=false;
    boolean taskEndMark=false;
    JLabel buttonStartOrPause;
    JLabel buttonDel;
    Thread changeBar=null;
    String taskList[]=null;
    ControlChannel controlChannel=null;
    String cmdDownFile;
    String cmdUpFile;
    String ServerIP;
    int ServerPort;



    //TODO 下载进度条
    public TaskProgressBar(JProgressBar bar,JLabel buttonStartOrPause,JLabel buttonDel, ClientFileReceived threadDown) {
        ReceivedMark=true;
        ThreadDown = threadDown;
        this.bar = bar;
        this.buttonStartOrPause=buttonStartOrPause;
        this.buttonDel=buttonDel;
        bar.setMinimum(0);
        bar.setMaximum(100);
        bar.setStringPainted(true);//设置进度条上是否显示进度具体进度如50%
        buttonDel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (taskEndMark){
                    JOptionPane.showMessageDialog(null,"0没有进行的任务，不用删除");
                    return;
                }
                changeBar.interrupt();
                if (SendMark) {
                    ThreadUp.interrupt();
                }
                if (ReceivedMark) {
                    ThreadDown.interrupt();
                }
                taskEndMark=true;
                bar.setValue(0);
                try {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    SqlUtils.DeleteSQL(taskList);
                    System.out.println("任务删除成功");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                bar.setString("任务已经删除");
                System.out.println("点了删除，删除任务taskEndMark:"+taskEndMark);
            }
        });

        buttonStartOrPause.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("点击了PauseMark"+PauseMark);
                if (taskEndMark)
                {
                    JOptionPane.showMessageDialog(null,"任务结束，无法操作");
                    return;
                }
                if (PauseMark==true){//继续任务 可暂停任务
                    PauseMark=false;
                    buttonStartOrPause.setText("暂停");
                    buttonStartOrPause.setIcon(new IconSet().getPauseIcon());
                    taskGoOn();
                    showBar();
                }
                else
                {
                    PauseMark=true;//暂停任务
                    buttonStartOrPause.setText("开始");
                    buttonStartOrPause.setIcon(new IconSet().getPlayIcon());
                    changeBar.interrupt();
                    if (SendMark)
                        ThreadUp.interrupt();
                    if (ReceivedMark)
                        ThreadDown.interrupt();
                    System.out.println("任务已经暂停");
                    bar.setString("任务已暂停");
                }
            }
        });
    }




    //TODO 上传进度条
    public TaskProgressBar(JProgressBar bar,JLabel buttonStartOrPause,JLabel buttonDel, ClientFileSend threadUp) {
        SendMark=true;
        ThreadUp = threadUp;
        this.bar = bar;
        bar.setMinimum(0);
        bar.setMaximum(100);
        bar.setStringPainted(true);//设置进度条上是否显示进度具体进度如50%
        this.buttonStartOrPause=buttonStartOrPause;
        this.buttonDel=buttonDel;
        buttonDel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (taskEndMark||!taskMark){
                    JOptionPane.showMessageDialog(null,"没有进行的任务，不用删除");
                    return;
                }
                changeBar.interrupt();
                if (SendMark) {
                    ThreadUp.interrupt();
                }
                if (ReceivedMark) {
                    ThreadDown.interrupt();
                }
                taskEndMark=true;
                bar.setValue(0);
                try {
                    SqlUtils.DeleteSQL(taskList);
                    System.out.println("任务删除成功");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                bar.setString("任务已经删除");
                System.out.println("点了删除，删除任务taskEndMark:"+taskEndMark);
            }
        });

        buttonStartOrPause.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("点击了PauseMark:"+PauseMark);
                System.out.println(taskEndMark);
                if (taskEndMark)
                {
                    JOptionPane.showMessageDialog(null,"0任务结束，无法操作");
                    return;
                }

                if (PauseMark==true){//继续任务 可暂停任务
                    PauseMark=false;
                    buttonStartOrPause.setText("暂停");
                    buttonStartOrPause.setIcon(new IconSet().getPauseIcon());
                    taskGoOn();
                    showBar();
                }
                else
                {
                    PauseMark=true;//暂停任务
                    buttonStartOrPause.setText("开始");
                    buttonStartOrPause.setIcon(new IconSet().getPlayIcon());
                    changeBar.interrupt();
                    if (SendMark)
                        ThreadUp.interrupt();
                    if (ReceivedMark)
                        ThreadDown.interrupt();
                    System.out.println("任务已经暂停");
                    bar.setString("任务已暂停");
                }
            }
        });

    }


    //TODO 进度条的初始化
    public void initProgressBar(String Choose,ControlChannel controlChannel,String[] taskList,String cmd,String IP,int Port){
        if (Choose.equals("FileDown")) {
            cmdDownFile = cmd;
            DownFileMark=true;
        }else
        {
            cmdUpFile=cmd;
            UpFileMark=true;
        }
        this.controlChannel=controlChannel;
        this.taskList=taskList;
        ServerIP=IP;
        ServerPort=Port;
        taskMark=false;
        taskEndMark=false;
    }

    public void showBar(){
        taskMark=true;
        changeBar=new ChangeBar();
        changeBar.start();
        buttonStartOrPause.setText("暂停");
        buttonStartOrPause.setIcon(new IconSet().getPauseIcon());
        System.out.println("新建进度条线程，更新进度");
    }

    //TODO 任务的继续
    public void taskGoOn() {
        if (taskEndMark)
        {
            return;
        }
        //获取Pointer
        String Point = null;
        Point = SqlUtils.getPointer(taskList);
        taskList[5] = Point;
        if (DownFileMark) {
            ClientFileReceived FileReceived = null;
            controlChannel.SendInfoToServer(cmdDownFile + Point);
            try {
                controlChannel.GetInfoFromSever().equals("Reply%DownOK");
                FileReceived = new ClientFileReceived(ServerIP, 7777, taskList[3], Long.valueOf(Point), taskList);
            } catch (IOException e) {
                System.out.println("任务继续错误");
            }
            ThreadDown = FileReceived;
            FileReceived.start();
            System.out.println("下载任务继续....新建线程文件接收线程....");
            return;
        }

       else{
            ClientFileSend FileSend=null;
            controlChannel.SendInfoToServer(cmdUpFile+Point);
            try {
                controlChannel.GetInfoFromSever().equals("Reply%UpOK");
                FileSend= new ClientFileSend(ServerIP, 6666, taskList[2], Long.valueOf(Point));
            } catch (IOException e) {
                System.out.println("任务继续错误");
            }
            ThreadUp = FileSend;
            FileSend.start();
            System.out.println("上传任务继续....新建线程文件接收线程....");
            return;
        }
    }
    /**
     * Created by Ken on 2017/7/8.


     */
    //内部类  线程 响应事件  负责  进度条 值的更新
    class ChangeBar extends Thread {
        public void run() {
            if (taskEndMark)
                return;
            if (SendMark)
            {
                    while (true){
                        int value=ThreadUp.getProgressBarValue();
                        bar.setValue(value);
                        bar.setString("正在传输"+value+"%");
                        if (bar.getValue()>=100) {
                            bar.setString("文件上传完成");
                            System.out.println("进度条完成");
                            taskEndMark=true;
                            return;
                        }
                        if (Thread.currentThread().isInterrupted()){
                            return;
                        }
                    }
            }
            if (ReceivedMark){
                while (true){
                    int value=ThreadDown.getProgressBarValue();
                    bar.setValue(value);
                    bar.setString("正在传输"+value+"%");
                    if (bar.getValue()>=100){
                        bar.setString("文件下载完成");
                        System.out.println("进度条完成");
                        taskEndMark=true;
                        return;
                        }
                        if (Thread.currentThread().isInterrupted()){
                            return;
                        }
                    }
            }
        }

    }
}
