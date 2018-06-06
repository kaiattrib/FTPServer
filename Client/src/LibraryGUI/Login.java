package LibraryGUI;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

/**
 * @author Brainrain
 */
public class Login extends JFrame implements Runnable {
    private Thread mainThread=null;
    private Thread loginThread=null;
    private String userName=null;
    public Login(Thread main) throws SQLException, ClassNotFoundException, IllegalAccessException, UnsupportedLookAndFeelException, InstantiationException {
        new SqlUtils();
        mainThread=main;
        System.out.println("主线程传入成功");
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");//设置观感Window观感
        initComponents();
        DIsplay();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        textField1.setText("TestUser");
        passwordField1.setText("123456");

    }
    private void button2ActionPerformed(ActionEvent e) {
        frame2.setVisible(true);// TODO add your code here
    }

    public void DIsplay() {
        frame1.setVisible(true);
    }

    private void button3ActionPerformed(ActionEvent e) {

        if (textField2.getText().equals("") || textField3.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "用户名或密码不能为空", "warning", JOptionPane.INFORMATION_MESSAGE);
            // TODO a// dd your code here
        }
    }

    private void button1ActionPerformed(ActionEvent e) {
        if (textField1.getText().equals("") || passwordField1.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "用户名或密码不能为空", "warning", JOptionPane.INFORMATION_MESSAGE); // TODO add your code here
        }
    }

    private void button1MouseClicked(MouseEvent e)  {
        String UN = textField1.getText();
        String PW = passwordField1.getText();
        try {
            if (SqlUtils.isLegalLogin(UN, PW) == false) {
                JOptionPane.showMessageDialog(null, "用户名或密码错误", "warning", JOptionPane.INFORMATION_MESSAGE);
            } else {
                if (SqlUtils.Check(UN).equals("0")) {

                    JOptionPane.showMessageDialog(null, "登录成功！", "warning", JOptionPane.INFORMATION_MESSAGE);
                    SqlUtils.UpdateSQL("UPDATE FTPDepot2.User SET State='1'WHERE User='" + UN + "'");
                    String UserName = UN;
                    userName=UserName;
                    frame1.setVisible(false);
                    mainThread.interrupt();
                    loginThread.interrupt();
                } else {
                    JOptionPane.showMessageDialog(null, "登录重复！", "warning", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }


    private void button3MouseClicked(MouseEvent e) {
        String Un = textField2.getText();
        String Pw = textField3.getText();

        if (Un == null || Pw == null) {
            JOptionPane.showMessageDialog(null, "用户名或密码不能为空！", "warning", JOptionPane.INFORMATION_MESSAGE);
        } /*else if (CK.next()) {
            JOptionPane.showMessageDialog(null, "用户名已经被注册，请重新输入！", "warning", JOptionPane.INFORMATION_MESSAGE);
        }
        */ else {
            if (SqlUtils.UpdateSQL("insert into FTPDepot2.User(User,PassWord,State) values('" + Un + "','" + Pw + "','0')") == false) {
                JOptionPane.showMessageDialog(null, "用户名已经被注册，请重新输入！", "warning", JOptionPane.INFORMATION_MESSAGE);
                ;
            } else
                JOptionPane.showMessageDialog(null, "注册成功！", "congradulations", JOptionPane.INFORMATION_MESSAGE);
            frame2.dispose();
        }
    }

    private void frame1WindowClosed(WindowEvent e) {
        System.exit(0);
    }

    private void frame1WindowClosing(WindowEvent e) {
        System.exit(0);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        frame1 = new JFrame();
        textField1 = new JTextField();
        passwordField1 = new JPasswordField();
        button2 = new JButton();
        label1 = new JLabel();
        label2 = new JLabel();
        button1 = new JButton();
        frame2 = new JFrame();
        label3 = new JLabel();
        label4 = new JLabel();
        textField2 = new JTextField();
        textField3 = new JTextField();
        button3 = new JButton();

        //======== frame1 ========
        {
            frame1.setResizable(false);
            frame1.setIconImage(new ImageIcon(getClass().getResource("/IconFile/mainicon.png")).getImage());
            frame1.setTitle("FTP Client 2017");
            frame1.setBackground(new Color(0, 153, 153));
            frame1.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    frame1WindowClosed(e);
                }
                @Override
                public void windowClosing(WindowEvent e) {
                    frame1WindowClosing(e);
                }
            });
            Container frame1ContentPane = frame1.getContentPane();
            frame1ContentPane.setLayout(null);
            frame1ContentPane.add(textField1);
            textField1.setBounds(95, 50, 210, 35);
            frame1ContentPane.add(passwordField1);
            passwordField1.setBounds(95, 100, 210, 35);

            //---- button2 ----
            button2.setText("\u6ce8\u518c");
            button2.addActionListener(e -> {
			button2ActionPerformed(e);
			button2ActionPerformed(e);
		});
            frame1ContentPane.add(button2);
            button2.setBounds(195, 170, button2.getPreferredSize().width, 30);

            //---- label1 ----
            label1.setText("\u8d26\u53f7\uff1a");
            label1.setIcon(new ImageIcon(getClass().getResource("/IconFile/user_24px_1137545_easyicon.net.png")));
            frame1ContentPane.add(label1);
            label1.setBounds(15, 50, 85, 35);

            //---- label2 ----
            label2.setText("\u5bc6\u7801\uff1a");
            label2.setIcon(new ImageIcon(getClass().getResource("/IconFile/settings_Password_24px_1107399_easyicon.net.png")));
            frame1ContentPane.add(label2);
            label2.setBounds(15, 110, 83, label2.getPreferredSize().height);

            //---- button1 ----
            button1.setText("\u767b\u5f55");
            button1.addActionListener(e -> button1ActionPerformed(e));
            button1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button1MouseClicked(e);
                }
            });
            frame1ContentPane.add(button1);
            button1.setBounds(110, 170, button1.getPreferredSize().width, 30);

            { // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < frame1ContentPane.getComponentCount(); i++) {
                    Rectangle bounds = frame1ContentPane.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = frame1ContentPane.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                frame1ContentPane.setMinimumSize(preferredSize);
                frame1ContentPane.setPreferredSize(preferredSize);
            }
            frame1.setSize(385, 285);
            frame1.setLocationRelativeTo(frame1.getOwner());
        }

        //======== frame2 ========
        {
            frame2.setResizable(false);
            frame2.setIconImage(new ImageIcon(getClass().getResource("/IconFile/user_24px_1137545_easyicon.net.png")).getImage());
            frame2.setTitle("Register");
            Container frame2ContentPane = frame2.getContentPane();
            frame2ContentPane.setLayout(null);

            //---- label3 ----
            label3.setText("\u8d26\u53f7\uff1a");
            label3.setIcon(new ImageIcon(getClass().getResource("/IconFile/user_24px_1137545_easyicon.net.png")));
            frame2ContentPane.add(label3);
            label3.setBounds(new Rectangle(new Point(15, 60), label3.getPreferredSize()));

            //---- label4 ----
            label4.setText("\u5bc6\u7801\uff1a");
            label4.setIcon(new ImageIcon(getClass().getResource("/IconFile/settings_Password_24px_1107399_easyicon.net.png")));
            frame2ContentPane.add(label4);
            label4.setBounds(new Rectangle(new Point(10, 110), label4.getPreferredSize()));
            frame2ContentPane.add(textField2);
            textField2.setBounds(85, 60, 165, textField2.getPreferredSize().height);
            frame2ContentPane.add(textField3);
            textField3.setBounds(85, 110, 165, textField3.getPreferredSize().height);

            //---- button3 ----
            button3.setText("\u6ce8\u518c");
            button3.addActionListener(e -> button3ActionPerformed(e));
            button3.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button3MouseClicked(e);
                }
            });
            frame2ContentPane.add(button3);
            button3.setBounds(new Rectangle(new Point(130, 175), button3.getPreferredSize()));

            { // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < frame2ContentPane.getComponentCount(); i++) {
                    Rectangle bounds = frame2ContentPane.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = frame2ContentPane.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                frame2ContentPane.setMinimumSize(preferredSize);
                frame2ContentPane.setPreferredSize(preferredSize);
            }
            frame2.setSize(340, 260);
            frame2.setLocationRelativeTo(frame2.getOwner());
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JFrame frame1;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton button2;
    private JLabel label1;
    private JLabel label2;
    private JButton button1;
    private JFrame frame2;
    private JLabel label3;
    private JLabel label4;
    private JTextField textField2;
    private JTextField textField3;
    private JButton button3;

    // JFormDesigner - End of variables declaration  //GEN-END:variables
    @Override
    public void run() {
        loginThread=Thread.currentThread();
        System.out.println("登录线程开启");
        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            this.disable();
            System.out.println("登录线程结束，退出");
            return;
        }
    }

    public String getUserName() {
        return  userName;
    }
}
