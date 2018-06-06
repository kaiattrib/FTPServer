/*
 * Created by JFormDesigner on Thu Jul 06 20:12:53 CST 2017
 */

package LibraryGUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author K
 */
public class MainWindow extends JFrame {
    //TODO 添加变量
    private String ServerIP;
    private int ServerPort;
    private String Title=new String("FTPClient 2017    ");
    private String userHomeDir=new String("D:\\FTPHome");
    private String userName;

    private String currentDir;
    public ControlChannel controlChannel;
    boolean FileMoveMark=false;
    boolean FileChooseMark=false;
    private String FileMoveSrcPath=null;
    public DefaultListModel listModel = new DefaultListModel();
    public MainWindow(String userName,String ServerIP,int Port) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, IOException {
        this.ServerIP=ServerIP;
        this.ServerPort=Port;
        this.userName=userName;
        controlChannel=new ControlChannel(ServerIP,Port);
        controlChannel.SendInfoToServer(userName);
        this.Title=this.Title+userName+"在线...";
        this.userHomeDir=userHomeDir+"\\"+userName;
        currentDir=userHomeDir;
        initFirst();
        initComponents();
        this.setTitle(this.Title);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//屏蔽默认的事件
    }

    private void exitButtonMouseClicked(MouseEvent e) {

        int Choose=JOptionPane.
                showConfirmDialog(null,
                        "真的要退出吗？", "提示:", JOptionPane.YES_NO_OPTION);//询问一个确认问题，如 yes/no/cancel
        if (0==Choose)
        {System.exit(0);
            SqlUtils.Reset(userName);
        }
        else
            return;
    }

    public void addStringArraytoJList(String[] strings){
        listModel.clear();
        for (int i = 2; i < strings.length; i++) {
            listModel.addElement(strings[i]);
        }
    }

    public void ClearJList(){
        listModel.clear();
    }
    private void initFirst() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");//设置观感Window观感
        this.setResizable(false);//设为不可调整窗口大小
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        try {
////            new SqlUtils();
//        } catch (SQLException e) {
//            System.out.println("数据库 初始化错误");
//        }
    }

    private void MianWindowClosing(WindowEvent e) {
        int Choose=JOptionPane.
                showConfirmDialog(null,
                        "真的要退出:？", "提示:", JOptionPane.YES_NO_OPTION);//询问一个确认问题，如 yes/no/cancel
        if (0==Choose)
            System.exit(0);
        else
            return;
    }

    private void labelExitMouseClicked(MouseEvent e) {
        exitButtonMouseClicked(e);
    }

    //目录刷新操作

    public void initFileList(){
        labelFlashMouseClicked(null);
    }
    private void labelFlashMouseClicked(MouseEvent e)  {
        //TODO 目录刷新操作
        String dirInfo[]=null;
        controlChannel.SendInfoToServer("GetInfo%"+currentDir);
        try {
            dirInfo=controlChannel.GetInfoFromSever().split("%");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        System.out.println("获取到消息");
        labelFileList2.setText("当前操作路径"+dirInfo[1].replace("D:\\FTPHome",""));
        addStringArraytoJList(dirInfo);
        textArea.append("目录已经刷新\n");
    }

    private void labelCreateDirMouseClicked(MouseEvent e) {
        // TODO 新建目录
        String folderName;
        folderName=JOptionPane.showInputDialog(null,"请输入文件夹名称:");
        if (folderName==null) {
            return;
        }
        if (folderName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "文件名无效，请重试");
            return;
        }
        String CmdCreateFolder="Dir%CreateFolder%"+currentDir+"\\"+folderName;
        controlChannel.SendInfoToServer(CmdCreateFolder);
        labelFlashMouseClicked(e);
    }

    private void labelFileDelMouseClicked(MouseEvent e)  {
        // TODO 删除文件或目录
        String DelName[];
        DelName= Filelist.getSelectedValue().toString().split(":");
        if (DelName==null)
            return;
        System.out.println(DelName);
        if (DelName[0].equals("Dir")) {
            String tips=null;

            try {
                if (isFolderEmpty(currentDir+"\\"+DelName[1]))
                    tips="文件夹为空";
                else
                    tips="文件夹里有文件";
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            int Choose=JOptionPane.
                    showConfirmDialog(null,
                            tips+"  真的要删除文件夹 ？"+DelName[1], "提示:", JOptionPane.YES_NO_OPTION);//询问一个确认问题，如 yes/no/cancel
            if (1==Choose)
                return;
                controlChannel.SendInfoToServer("Dir%DeleteFolder%"+currentDir+"\\"+DelName[1]);
             }
        if (DelName[0].equals("File")) {
            int Choose=JOptionPane.
                    showConfirmDialog(null,
                            "真的要删除文件 ？"+DelName[1], "提示:", JOptionPane.YES_NO_OPTION);//询问一个确认问题，如 yes/no/cancel
            if (1==Choose)
                return;
            controlChannel.SendInfoToServer("File%DeleteFile%"+currentDir+"\\"+DelName[1]);
        }
        labelFlashMouseClicked(e);
    }

    private void labelOpenDirMouseClicked(MouseEvent e){
        //TODO 进入指定目录
        if (Filelist.getSelectedValue()==null){
            JOptionPane.showMessageDialog(null,"没有选择目录");
            return;
        }

        if (Filelist.getSelectedValue().toString().split(":")[0].equals("File")){
            JOptionPane.showMessageDialog(null,"你选择的是文件，请选择目录");
            return;
        }
        System.out.println("当前"+currentDir);
        String dirInfo[]=null;
        String DesDir=currentDir+"\\"+ Filelist.getSelectedValue().toString().split(":")[1];
        currentDir=DesDir;
        System.out.println("进入"+currentDir);
        controlChannel.SendInfoToServer("GetInfo%"+currentDir);
        try {
            dirInfo=controlChannel.GetInfoFromSever().split("%");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        labelFileList2.setText("当前操作路径"+dirInfo[1].replace("D:\\FTPHome",""));
        addStringArraytoJList(dirInfo);
    }

    private void labelBackDirMouseClicked(MouseEvent e){
        // TODO 返回上级目录
        System.out.println("当前"+currentDir);
        if (currentDir.equals(userHomeDir)){
            System.out.println(currentDir);
            JOptionPane.showMessageDialog(null,"已是用户家目录");
            return;
        }
        String dirInfo[]=null;
        String tempCurrentDir[]=currentDir.replace("\\","%").split("%");
        String newCurrentDir=new String();
        for (int i = 0; i < tempCurrentDir.length-1; i++) {
            if (i < tempCurrentDir.length-2)
                newCurrentDir=newCurrentDir+tempCurrentDir[i]+"\\";
            else
                newCurrentDir=newCurrentDir+tempCurrentDir[i];

        }
        currentDir=newCurrentDir;
        System.out.println("回到:"+currentDir);
        controlChannel.SendInfoToServer("GetInfo%"+currentDir);
        try {
            dirInfo=controlChannel.GetInfoFromSever().split("%");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        labelFileList2.setText("当前操作路径"+dirInfo[1].replace("D:\\FTPHome",""));
        addStringArraytoJList(dirInfo);
    }

    private void labelRnameFileMouseClicked(MouseEvent e){
        // TODO 文件或文件夹的重命名
        String DelName[];
        DelName= Filelist.getSelectedValue().toString().split(":");
        String oldName=currentDir+"\\"+DelName[1];
        String newName;

        if (DelName[0].equals("File")){
            newName=JOptionPane.showInputDialog(null,"请输入新的文件名，包括后缀名");
            if (newName==null||newName.isEmpty()) {
                textArea.append("没有输入新文件名 或错误\n");
                return;
            }
            newName=currentDir+"\\"+newName;
            controlChannel.SendInfoToServer("File%ReNameFile%"+oldName+"%"+newName);
            return;
        }
        if (DelName[0].equals("Dir")){
            newName=JOptionPane.showInputDialog(null,"请输入新的文件夹名");
            if (newName==null||newName.isEmpty()) {
                textArea.append("没有输入新文件夹名 或错误\n");
            }
            newName=currentDir+"\\"+newName;
            controlChannel.SendInfoToServer("Dir%RenametoFoldet%"+oldName+"%"+newName);
        }
        labelFlashMouseClicked(e);
    }

    private void labelFileMoveMarkMouseClicked(MouseEvent e) {
        // TODO 文件移动标记
        String oldFileName[]=null;
        if (FileMoveMark==false) {
            FileMoveMark=true;
            oldFileName= Filelist.getSelectedValue().toString().split(":");
            FileMoveSrcPath=currentDir+"\\"+ oldFileName[1];
            JOptionPane.showMessageDialog(null,"已经标记需要移动的文件");
            textArea.append("已经标记需要移动的文件\n");
            labelFileMoveMark.setText("取消标记");
        }
        else
        {
            FileMoveMark=false;
            FileMoveSrcPath=null;
            textArea.append("已经取消标记需要移动的文件\n");
            JOptionPane.showMessageDialog(null,"已经取消需要移动的文件");
            labelFileMoveMark.setText("标记移动");
        }
    }

    private void labelFileMoveMouseClicked(MouseEvent e) {
        // TODO 开始文件的移动
        if (FileMoveMark==false||FileMoveSrcPath==null){
            JOptionPane.showMessageDialog(null,"你没有已经标记的文件，请先标记");
            return;
        }
        String newPath=currentDir;
        System.out.println("需要移动的目标"+FileMoveSrcPath);
        String temp[]=FileMoveSrcPath.replace("\\","%").split("%");
        int length=temp.length;
        String fineName=temp[length-1];
        newPath=newPath+"\\"+fineName;

        if (FileMoveSrcPath.equals(newPath)){
            JOptionPane.showMessageDialog(null,"错误 还是原本目录 不需要移动");
            textArea.append("错误 还是原本目录 不需要移动\n");
            return;
        }

        if (fineName.split(".").length>1)
            {
                String CmdMoveFile=new String("File%MoveFile%"+FileMoveSrcPath+"%"+newPath);
                controlChannel.SendInfoToServer(CmdMoveFile);
                textArea.append("文件 移动消息发送成功\n");
            }
        else
        {
            String CmdMoveFile=new String("Dir%MoveFolder%"+FileMoveSrcPath+"%"+newPath);
            controlChannel.SendInfoToServer(CmdMoveFile);
            textArea.append("文件夹 移动消息发送成功\n");
        }
        FileMoveMark=false;
        labelFileMoveMark.setText("标记移动");
        labelFlashMouseClicked(e);
    }

    private void label1FileDownMouseClicked(MouseEvent e) {
        // TODO 文件的下载

        String taskList[]=new String[7];
        taskList[0]=userName;

        String FileName=Filelist.getSelectedValue().toString().split(":")[1];
        taskList[1]=FileName;
        String FilePathFrom=currentDir+"\\"+FileName;
        taskList[2]=FilePathFrom;
        String FilePathTo=fileChooserLocalFile.getCurrentDirectory().getAbsolutePath()+"\\"+FileName;
        taskList[3]=FilePathTo;
        taskList[4]="DOWN";
        taskList[5]="0";//Pointer
        String FileSize="0";
        //从服务端获取文件大小
        controlChannel.SendInfoToServer("GetInfo%FileSize%"+FilePathFrom);
        try {

            FileSize=controlChannel.GetInfoFromSever().split("%")[2];
            System.out.println(FileSize);
        } catch (IOException e1) {
            System.out.println("获取文件大小失败");
        }
        taskList[6]=FileSize;
        System.out.println("标记"+FileSize);
        //获取Pointer
        String Point=null;
        Point=SqlUtils.getPointer(taskList);
        taskList[5]=Point;
        //下载消息发发送
        //File%Down%FileName%FilePathFrom%FilePathTo%FileSize%Point
        String cmdFileDown=new String("File%Down%"+FileName+"%"+FilePathFrom+"%"+FilePathTo+"%"+FileSize+"%");
        controlChannel.SendInfoToServer(cmdFileDown+Point);
        try {
            if (controlChannel.GetInfoFromSever().equals("Reply%DownOK"))
                try {
                    initProgressBarButton("Down");
                    ClientFileReceived FileReceived= new ClientFileReceived(ServerIP,7777,FilePathTo,Long.valueOf(Point),taskList);
                    FileReceived.start();
                    TaskProgressBar taskProgressBar=new TaskProgressBar(progressBarDown1,labelDownBar1StartOrPause,labelDownBar1Del,FileReceived);
                    taskProgressBar.initProgressBar("FileDown",controlChannel,taskList,cmdFileDown,ServerIP,7777);
                    taskProgressBar.showBar();
            } catch (IOException e1) {
                    textArea.append("文件下载请求错误\n");
                }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        JOptionPane.showMessageDialog(null,"下载文件请求成功,任务面板查看");
        textArea.append("发起文件下载请求\n"+Filelist.getSelectedValue().toString());
        textArea.append("下载到目录:"+fileChooserLocalFile.getCurrentDirectory().getAbsolutePath()+"\n");
    }


    //TODO 多个文件上传
    private void MulitFileUp(){
        File files[]=fileChooserLocalFile.getSelectedFiles();
        for (int i = 0; i < files.length; i++) {

            String taskList[]=new String[7];
            taskList[0]=userName;

            String FileName=files[i].getName();
            taskList[1]=FileName;
            String FilePathFrom=files[i].getAbsolutePath();
            taskList[2]=FilePathFrom;
            String FilePathTo=currentDir+"\\"+FileName;
            taskList[3]=FilePathTo;
            taskList[4]="UP";
            taskList[5]="0";//Pointer
            String FileSize= null;
            //从本地文件大小
            try {
                FileSize = new RandomAccessFile(FilePathFrom,"rw").length()+"";
            } catch (IOException e1) {
                System.out.println("本地文件大小读取错误");
            }
            taskList[6]=FileSize;
            System.out.println("标记"+FileSize);
            //获取Pointer
            String Point=null;
            Point=SqlUtils.getPointer(taskList);
            taskList[5]=Point;

            //上传消息发发送
            //File%Up%FileName%FilePathFrom%FilePathTo%FileSize%Point
            controlChannel.SendInfoToServer("File%Up%"+FileName+"%"+FilePathFrom+"%"+FilePathTo+"%"+FileSize+"%"+Point);
            try {
                if (controlChannel.GetInfoFromSever().equals("Reply%UpOK"))
                    try {
                        ClientFileSend FileSend= new ClientFileSend(ServerIP,6666,FilePathFrom,Long.valueOf(Point));
                        FileSend.start();
                    } catch (IOException e1) {
                        textArea.append("文件下载请求错误\n");
                    }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            textArea.append("发起文件上传请求\n"+currentDir);
        }
    }
    private void labelFileUpMouseClicked(MouseEvent e) {
        // TODO 文件的上传

        if (FileChooseMark==false)
            {
                JOptionPane.showMessageDialog(null,"没有选中上传文件");
                return;
            }

        if (fileChooserLocalFile.getSelectedFiles().length>1){
            System.out.println("进入多个文件上传");
            MulitFileUp();
            labelFlashMouseClicked(e);
            return;
        }

        String taskList[]=new String[7];
        taskList[0]=userName;

        String FileName=fileChooserLocalFile.getSelectedFile().getName();
        taskList[1]=FileName;
        String FilePathFrom=fileChooserLocalFile.getSelectedFile().getAbsolutePath();
        taskList[2]=FilePathFrom;
        String FilePathTo=currentDir+"\\"+FileName;
        taskList[3]=FilePathTo;
        taskList[4]="UP";
        taskList[5]="0";//Pointer
        String FileSize= null;
        //从本地文件大小
        try {
            FileSize = new RandomAccessFile(FilePathFrom,"rw").length()+"";
        } catch (IOException e1) {
            System.out.println("本地文件大小读取错误");
        }
        taskList[6]=FileSize;
        System.out.println("标记"+FileSize);
        //获取Pointer
        String Point=null;
        Point=SqlUtils.getPointer(taskList);
        taskList[5]=Point;

        //上传消息发发送
        //File%Up%FileName%FilePathFrom%FilePathTo%FileSize%Point
        String cmdUpFile="File%Up%"+FileName+"%"+FilePathFrom+"%"+FilePathTo+"%"+FileSize+"%";
        controlChannel.SendInfoToServer(cmdUpFile+Point);
        try {
            if (controlChannel.GetInfoFromSever().equals("Reply%UpOK"))
                try {
                    initProgressBarButton("Up");
                    ClientFileSend FileSend= new ClientFileSend(ServerIP,6666,FilePathFrom,Long.valueOf(Point));
                    FileSend.start();
                    TaskProgressBar taskProgressBar=new TaskProgressBar(progressBarUp1,labelUpBar1StartOrPause,labelUpBar1Del1,FileSend);
                    taskProgressBar.initProgressBar("FileUp",controlChannel,taskList,cmdUpFile,ServerIP,6666);
                    taskProgressBar.showBar();
                } catch (IOException e1) {
                    textArea.append("文件下载请求错误\n");
                }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        textArea.append("发起文件上传请求\n"+currentDir);
        labelFlashMouseClicked(e);
        JOptionPane.showMessageDialog(null,"下载文件请求成功,任务面板查看");
    }


    //进度条的按钮更新
    public void initProgressBarButton(String Mode){

        if (Mode.equals("Down")){

            panelFileDown.remove(labelDownBar1StartOrPause);
            panelFileDown.remove(labelDownBar1Del);
            labelDownBar1StartOrPause = new JLabel();
            labelDownBar1Del = new JLabel();
            //---- labelDownBar1StartOrPause ----
            labelDownBar1StartOrPause.setText("\u5f00\u59cb");
            labelDownBar1StartOrPause.setIcon(new ImageIcon(getClass().getResource("/IconFile/playIcon.png")));
            panelFileDown.add(labelDownBar1StartOrPause);
            labelDownBar1StartOrPause.setBounds(880, 10, 85, 50);

            //---- labelDownBar1Del ----
            labelDownBar1Del.setText("\u5220\u9664");
            labelDownBar1Del.setIcon(new ImageIcon(getClass().getResource("/IconFile/delIcon.png")));
            panelFileDown.add(labelDownBar1Del);
            labelDownBar1Del.setBounds(970, 10, 90, 50);
        }
        else if (Mode.equals("Up")){
            panelFileUp.remove(labelUpBar1StartOrPause);
            panelFileUp.remove(labelUpBar1Del1);
            //新建对象
            labelUpBar1StartOrPause = new JLabel();
            labelUpBar1Del1 = new JLabel();

            //---- labelUpBar1StartOrPause ----
            labelUpBar1StartOrPause.setText("\u5f00\u59cb");
            labelUpBar1StartOrPause.setIcon(new ImageIcon(getClass().getResource("/IconFile/playIcon.png")));
            panelFileUp.add(labelUpBar1StartOrPause);
            labelUpBar1StartOrPause.setBounds(880, 10, 85, 50);

            //---- labelUpBar1Del1 ----
            labelUpBar1Del1.setText("\u5220\u9664");
            labelUpBar1Del1.setIcon(new ImageIcon(getClass().getResource("/IconFile/delIcon.png")));
            panelFileUp.add(labelUpBar1Del1);
            labelUpBar1Del1.setBounds(970, 10, 90, 50);
        }
        }
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        tabbedPane = new JTabbedPane();
        panelFileHome = new JPanel();
        scrollPaneFileList = new JScrollPane();
        Filelist = new JList();
        labelFileList = new JLabel();
        labelFileList2 = new JLabel();
        labelFlash = new JLabel();
        labelFileUp = new JLabel();
        label1FileDown = new JLabel();
        labelFileDel = new JLabel();
        labelOpenDir = new JLabel();
        labelCreateDir = new JLabel();
        label3 = new JLabel();
        labelBackDir = new JLabel();
        labelRnameFile = new JLabel();
        fileChooserLocalFile = new JFileChooser();
        labelFileMoveMark = new JLabel();
        scrollPane1 = new JScrollPane();
        textArea = new JTextArea();
        labelFileMove = new JLabel();
        panelFileUp = new JPanel();
        progressBarUp1 = new JProgressBar();
        labelUpBar1StartOrPause = new JLabel();
        labelUpBar1Del1 = new JLabel();
        panelFileDown = new JPanel();
        progressBarDown1 = new JProgressBar();
        labelDownBar1StartOrPause = new JLabel();
        labelDownBar1Del = new JLabel();
        panelUserSet = new JPanel();
        labelExit = new JLabel();

        //======== this ========
        setIconImage(new ImageIcon(getClass().getResource("/IconFile/mainicon.png")).getImage());
        setTitle("FTP Client 2017");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MianWindowClosing(e);
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(null);

            //======== tabbedPane ========
            {

                //======== panelFileHome ========
                {
                    panelFileHome.setLayout(null);

                    //======== scrollPaneFileList ========
                    {

                        //---- Filelist ----
                        Filelist.setVisibleRowCount(10);
                        scrollPaneFileList.setViewportView(Filelist);
                    }
                    panelFileHome.add(scrollPaneFileList);
                    scrollPaneFileList.setBounds(15, 120, 300, 445);

                    //---- labelFileList ----
                    labelFileList.setText("\u8fdc\u7a0b\u6587\u4ef6\u5217\u8868");
                    labelFileList.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 20));
                    panelFileHome.add(labelFileList);
                    labelFileList.setBounds(new Rectangle(new Point(20, 10), labelFileList.getPreferredSize()));

                    //---- labelFileList2 ----
                    labelFileList2.setText("\u5f53\u524d\u64cd\u4f5c\u8def\u5f84:");
                    labelFileList2.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 20));
                    panelFileHome.add(labelFileList2);
                    labelFileList2.setBounds(20, 40, 580, 24);

                    //---- labelFlash ----
                    labelFlash.setText("\u5237\u65b0\u5217\u8868");
                    labelFlash.setIcon(new ImageIcon(getClass().getResource("/IconFile/FlushIcon.png")));
                    labelFlash.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            labelFlashMouseClicked(e);
                        }
                    });
                    panelFileHome.add(labelFlash);
                    labelFlash.setBounds(new Rectangle(new Point(330, 120), labelFlash.getPreferredSize()));

                    //---- labelFileUp ----
                    labelFileUp.setText("\u4e0a\u4f20\u6587\u4ef6");
                    labelFileUp.setIcon(new ImageIcon(getClass().getResource("/IconFile/UPico.png")));
                    labelFileUp.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            labelFileUpMouseClicked(e);
                        }
                    });
                    panelFileHome.add(labelFileUp);
                    labelFileUp.setBounds(330, 250, 135, 60);

                    //---- label1FileDown ----
                    label1FileDown.setText("\u4e0b\u8f7d\u6587\u4ef6");
                    label1FileDown.setIcon(new ImageIcon(getClass().getResource("/IconFile/downIcon.png")));
                    label1FileDown.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            label1FileDownMouseClicked(e);
                        }
                    });
                    panelFileHome.add(label1FileDown);
                    label1FileDown.setBounds(new Rectangle(new Point(330, 190), label1FileDown.getPreferredSize()));

                    //---- labelFileDel ----
                    labelFileDel.setText("\u5220\u9664");
                    labelFileDel.setIcon(new ImageIcon(getClass().getResource("/IconFile/close_48px_1205793_easyicon.net.png")));
                    labelFileDel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            labelFileDelMouseClicked(e);
                        }
                    });
                    panelFileHome.add(labelFileDel);
                    labelFileDel.setBounds(new Rectangle(new Point(220, 70), labelFileDel.getPreferredSize()));

                    //---- labelOpenDir ----
                    labelOpenDir.setText("\u524d\u8fdb");
                    labelOpenDir.setIcon(new ImageIcon(getClass().getResource("/IconFile/48px_Enter.png")));
                    labelOpenDir.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            labelOpenDirMouseClicked(e);
                        }
                    });
                    panelFileHome.add(labelOpenDir);
                    labelOpenDir.setBounds(new Rectangle(new Point(15, 70), labelOpenDir.getPreferredSize()));

                    //---- labelCreateDir ----
                    labelCreateDir.setText("\u65b0\u5efa\u76ee\u5f55");
                    labelCreateDir.setIcon(new ImageIcon(getClass().getResource("/IconFile/list_48px_1205813_easyicon.net.png")));
                    labelCreateDir.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            labelCreateDirMouseClicked(e);
                        }
                    });
                    panelFileHome.add(labelCreateDir);
                    labelCreateDir.setBounds(new Rectangle(new Point(335, 325), labelCreateDir.getPreferredSize()));

                    //---- label3 ----
                    label3.setText("\u6d88\u606f\u6846:");
                    label3.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 20));
                    panelFileHome.add(label3);
                    label3.setBounds(new Rectangle(new Point(600, 5), label3.getPreferredSize()));

                    //---- labelBackDir ----
                    labelBackDir.setText("\u540e\u9000");
                    labelBackDir.setIcon(new ImageIcon(getClass().getResource("/IconFile/48Xback.png")));
                    labelBackDir.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            labelBackDirMouseClicked(e);
                        }
                    });
                    panelFileHome.add(labelBackDir);
                    labelBackDir.setBounds(new Rectangle(new Point(115, 70), labelBackDir.getPreferredSize()));

                    //---- labelRnameFile ----
                    labelRnameFile.setText("\u91cd\u65b0\u547d\u540d");
                    labelRnameFile.setIcon(new ImageIcon(getClass().getResource("/IconFile/repeat_52.317991631799px_1205841_easyicon.net.png")));
                    labelRnameFile.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            labelRnameFileMouseClicked(e);
                        }
                    });
                    panelFileHome.add(labelRnameFile);
                    labelRnameFile.setBounds(new Rectangle(new Point(335, 395), labelRnameFile.getPreferredSize()));
                    panelFileHome.add(fileChooserLocalFile);
                    fileChooserLocalFile.setBounds(500, 115, 565, 455);

                    //---- labelFileMoveMark ----
                    labelFileMoveMark.setText("\u6807\u8bb0\u79fb\u52a8");
                    labelFileMoveMark.setIcon(new ImageIcon(getClass().getResource("/IconFile/move_48px_1205817_easyicon.net.png")));
                    labelFileMoveMark.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            labelFileMoveMarkMouseClicked(e);
                        }
                    });
                    panelFileHome.add(labelFileMoveMark);
                    labelFileMoveMark.setBounds(new Rectangle(new Point(335, 455), labelFileMoveMark.getPreferredSize()));

                    //======== scrollPane1 ========
                    {
                        scrollPane1.setViewportView(textArea);
                    }
                    panelFileHome.add(scrollPane1);
                    scrollPane1.setBounds(600, 30, 455, 80);

                    //---- labelFileMove ----
                    labelFileMove.setText("\u79fb\u5230\u6b64\u5904");
                    labelFileMove.setIcon(new ImageIcon(getClass().getResource("/IconFile/checked_48px_1205792_easyicon.net.png")));
                    labelFileMove.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            labelFileMoveMouseClicked(e);
                        }
                    });
                    panelFileHome.add(labelFileMove);
                    labelFileMove.setBounds(new Rectangle(new Point(335, 515), labelFileMove.getPreferredSize()));

                    { // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panelFileHome.getComponentCount(); i++) {
                            Rectangle bounds = panelFileHome.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panelFileHome.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panelFileHome.setMinimumSize(preferredSize);
                        panelFileHome.setPreferredSize(preferredSize);
                    }
                }
                tabbedPane.addTab("\u4e3b\u76ee\u5f55", new ImageIcon(getClass().getResource("/IconFile/grid_32px_1205809_easyicon.net.png")), panelFileHome);

                //======== panelFileUp ========
                {
                    panelFileUp.setLayout(null);

                    //---- progressBarUp1 ----
                    progressBarUp1.setStringPainted(true);
                    progressBarUp1.setToolTipText("\u6b63\u5728\u4e0b\u8f7d");
                    progressBarUp1.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 20));
                    panelFileUp.add(progressBarUp1);
                    progressBarUp1.setBounds(10, 5, 865, 60);

                    //---- labelUpBar1StartOrPause ----
                    labelUpBar1StartOrPause.setText("\u5f00\u59cb");
                    labelUpBar1StartOrPause.setIcon(new ImageIcon(getClass().getResource("/IconFile/playIcon.png")));
                    panelFileUp.add(labelUpBar1StartOrPause);
                    labelUpBar1StartOrPause.setBounds(880, 10, 85, 50);

                    //---- labelUpBar1Del1 ----
                    labelUpBar1Del1.setText("\u5220\u9664");
                    labelUpBar1Del1.setIcon(new ImageIcon(getClass().getResource("/IconFile/delIcon.png")));
                    panelFileUp.add(labelUpBar1Del1);
                    labelUpBar1Del1.setBounds(970, 10, 90, 50);

                    { // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panelFileUp.getComponentCount(); i++) {
                            Rectangle bounds = panelFileUp.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panelFileUp.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panelFileUp.setMinimumSize(preferredSize);
                        panelFileUp.setPreferredSize(preferredSize);
                    }
                }
                tabbedPane.addTab("\u6587\u4ef6\u4e0a\u4f20", new ImageIcon(getClass().getResource("/IconFile/cloud_computing_44.359788359788px_1205798_easyicon.net.png")), panelFileUp);

                //======== panelFileDown ========
                {
                    panelFileDown.setLayout(null);

                    //---- progressBarDown1 ----
                    progressBarDown1.setStringPainted(true);
                    progressBarDown1.setToolTipText("\u6b63\u5728\u4e0b\u8f7d");
                    progressBarDown1.setFont(new Font("\u5b8b\u4f53", Font.PLAIN, 20));
                    panelFileDown.add(progressBarDown1);
                    progressBarDown1.setBounds(10, 5, 865, 60);

                    //---- labelDownBar1StartOrPause ----
                    labelDownBar1StartOrPause.setText("\u5f00\u59cb");
                    labelDownBar1StartOrPause.setIcon(new ImageIcon(getClass().getResource("/IconFile/playIcon.png")));
                    panelFileDown.add(labelDownBar1StartOrPause);
                    labelDownBar1StartOrPause.setBounds(880, 10, 85, 50);

                    //---- labelDownBar1Del ----
                    labelDownBar1Del.setText("\u5220\u9664");
                    labelDownBar1Del.setIcon(new ImageIcon(getClass().getResource("/IconFile/delIcon.png")));
                    panelFileDown.add(labelDownBar1Del);
                    labelDownBar1Del.setBounds(970, 10, 90, 50);

                    { // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panelFileDown.getComponentCount(); i++) {
                            Rectangle bounds = panelFileDown.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panelFileDown.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panelFileDown.setMinimumSize(preferredSize);
                        panelFileDown.setPreferredSize(preferredSize);
                    }
                }
                tabbedPane.addTab("\u6587\u4ef6\u4e0b\u8f7d", new ImageIcon(getClass().getResource("/IconFile/cloud_computing_44.359788359788px_1205795_easyicon.net.png")), panelFileDown);

                //======== panelUserSet ========
                {
                    panelUserSet.setLayout(null);

                    //---- labelExit ----
                    labelExit.setText("\u9000\u51fa");
                    labelExit.setIcon(new ImageIcon(getClass().getResource("/IconFile/48XExit.png")));
                    labelExit.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            labelExitMouseClicked(e);
                        }
                    });
                    panelUserSet.add(labelExit);
                    labelExit.setBounds(965, 495, 95, 70);

                    { // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panelUserSet.getComponentCount(); i++) {
                            Rectangle bounds = panelUserSet.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panelUserSet.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panelUserSet.setMinimumSize(preferredSize);
                        panelUserSet.setPreferredSize(preferredSize);
                    }
                }
                tabbedPane.addTab("\u7528\u6237\u8bbe\u7f6e", new ImageIcon(getClass().getResource("/IconFile/48Xuseset.png")), panelUserSet);
            }
            dialogPane.add(tabbedPane);
            tabbedPane.setBounds(-5, 0, 1075, 635);

            { // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < dialogPane.getComponentCount(); i++) {
                    Rectangle bounds = dialogPane.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = dialogPane.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                dialogPane.setMinimumSize(preferredSize);
                dialogPane.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        setSize(1095, 685);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
        //TODO 自定义变量初始化
        textArea.setEditable(false);
        Filelist = new JList(listModel);
        scrollPaneFileList.setViewportView(Filelist);
        Filelist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2)
                {
                    textArea.append("双击进入目录\n");
                    labelOpenDirMouseClicked(e);
                }
            }
        }); {
        }
        fileChooserLocalFile.setApproveButtonText("选择");
        fileChooserLocalFile.setMultiSelectionEnabled(true);//设置多选
        fileChooserLocalFile.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooserLocalFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileChooseMark=true;
                if ((JFileChooser.APPROVE_OPTION)==0){
                    if (fileChooserLocalFile.getSelectedFiles().length==1)
                    JOptionPane.showMessageDialog(null,"已经中一个文件");
                    else
                        JOptionPane.showMessageDialog(null,"已经中多个文件");

                }
            }
        });
    }




    //TODO 判断一个目录是否为空
    public boolean isFolderEmpty(String DirPath) throws IOException {
        controlChannel.SendInfoToServer("GetInfo%"+DirPath);
        String[] dirInfo = controlChannel.GetInfoFromSever().split("%");
        System.out.println("目录文件数量 "+dirInfo.length);
        if (dirInfo.length>2)
            return false;
        return true;
    }
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JTabbedPane tabbedPane;
    private JPanel panelFileHome;
    private JScrollPane scrollPaneFileList;
    private JList Filelist;
    private JLabel labelFileList;
    private JLabel labelFileList2;
    private JLabel labelFlash;
    private JLabel labelFileUp;
    private JLabel label1FileDown;
    private JLabel labelFileDel;
    private JLabel labelOpenDir;
    private JLabel labelCreateDir;
    private JLabel label3;
    private JLabel labelBackDir;
    private JLabel labelRnameFile;
    private JFileChooser fileChooserLocalFile;
    private JLabel labelFileMoveMark;
    private JScrollPane scrollPane1;
    private JTextArea textArea;
    private JLabel labelFileMove;
    private JPanel panelFileUp;
    private JProgressBar progressBarUp1;
    private JLabel labelUpBar1StartOrPause;
    private JLabel labelUpBar1Del1;
    private JPanel panelFileDown;
    private JProgressBar progressBarDown1;
    private JLabel labelDownBar1StartOrPause;
    private JLabel labelDownBar1Del;
    private JPanel panelUserSet;
    private JLabel labelExit;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
