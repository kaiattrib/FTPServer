package LibraryGUI;

import javax.swing.*;

/**
 * Created by Ken on 2017/7/8.
 */
public class IconSet {
    ImageIcon delIcon=new ImageIcon(getClass().getResource("/IconFile/delIcon.png"));
    ImageIcon playIcon=new ImageIcon(getClass().getResource("/IconFile/playIcon.png"));
    ImageIcon pauseIcon=new ImageIcon(getClass().getResource("/IconFile/pauseIcon.png"));

    public IconSet(){
    }

    public ImageIcon getDelIcon(){
        return  delIcon;
    }
    public ImageIcon getPlayIcon(){
        return playIcon;
    }

    public ImageIcon getPauseIcon(){
        return  pauseIcon;
    }
}
