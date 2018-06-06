package ServerOperate;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by 欢 on 2017/7/4.
 */
public class FileFunction {
    public static void main(String args[]){
        File file = new File("a.txt");
        File newfile = new File("b.txt");
//        Create(file);
//        Delete(file);
//        Rename(file,newfile);
//        Move(newfile,new File("src/b.txt"));
        SetAttribute(file,"可写");
    }
    //文件的创建
    public static void Create(File file){
        if (file.exists()){
            System.out.println("文件已存在");
        }else{
            try {
                if (file.createNewFile()){
                    System.out.println("文件创建成功");
                }else {
                    System.out.println("文件创建失败");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //文件的删除
    public static void Delete(File file){
        if (!(file.exists())){
            System.out.println("要删除的文件不存在");
        }else {
            if (file.delete()){
                System.out.println("已删除"+file.getName()+"文件");
            }else {
                System.out.println("无法删除"+file.getName()+"文件");
            }
        }
    }
    //文件的重命名
    public static void Rename(File file,File newFile){
        if (!(file.exists())){
            System.out.println("要重命名的文件不存在");
        }else {
            file.renameTo(newFile);
            System.out.println("重命名成功");
            System.out.println("新的名字为"+newFile.getName());
        }
    }
    //文件的移动
    public static void Move(File file,File newPath){
        if (!(file.exists())){
            System.out.println("文件不存在");
        }else{
            file.renameTo(newPath);
            System.out.println("移动成功");
            System.out.println("新的路径为"+newPath.getAbsolutePath());
        }
    }
    //文件的读写权限设置
    public static void SetAttribute(File file,String select){
        if (select == "只读"){
            file.setReadOnly();
            System.out.println(file.getName()+"设置为只读成功");
        }else if (select == "可读"){
            file.setReadable(true);
            System.out.println(file.getName()+"设置为可读成功");
        }else if (select == "不可读"){
            file.setReadable(false);
            System.out.println(file.getName()+"设置为不可读成功");
        }else if (select == "可写"){
            file.setWritable(true);
            System.out.println(file.getName()+"设置为可写成功");
        }else if (select == "不可写"){
            file.setWritable(false);
            System.out.println(file.getName()+"设置为不可写成功");
        }else {
            System.out.println("输入有误");
        }
    }
}
