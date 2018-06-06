package ServerOperate; /**
 * Created by 赟婷 on 2017/7/4.
 */

import java.io.File;
public class DirectoryFunction {
    //文件夹的创建
    public static void createfolder(File folder)
    {
        if(folder.exists())
        {
            System.out.println("文件夹已存在");
        }else{
            try {
                folder.mkdirs();
                System.out.println("文件夹创建成功");
            } catch (Exception e) {
                System.out.println("文件夹创建失败");
            }

        }
    }

    //文件夹的删除
    public static void deletefolders(File folder)
    {
        if(folder.exists())
        {
            if(folder.delete())
                System.out.println("空文件夹删除成功");
            else
                {
                    System.out.println("文件夹里有文件 进入递归删除");
                    removedir(folder);
                    System.out.println("递归文件夹删除成功");
                }
        }else
        {
            System.out.println("文件夹不存在");
        }
    }

        public static void removedir(File file)
        {
            File[] files=file.listFiles();
            for(File f:files)
            {
                if(f.isDirectory())//递归调用
                {
                    removedir(f);
                }
                else {
                    f.delete();
                }
            }
            //一层目录下的内容都删除以后，删除掉这个文件夹
            file.delete();
        }
    //文件夹的重命名
    public static void renametofolder(File folder,File newfolder)
    {
        if(folder.exists())
        {
            if(folder.renameTo(newfolder))
            {
                System.out.println("文件已重命名成功");
            }
            else
            {
                System.out.println("文件重命名失败");
            }
        }
        else
        {
            System.out.println("文件夹不存在，无法重命名");
        }
    }
    //文件夹的移动
    public static void movefolder(File file,File newaddress)
    {
        if(file.exists())
        {
               if(file.renameTo(newaddress))
               {
                   System.out.println("文件移动成功");
                   System.out.println("文件的新路径为" + file.getAbsolutePath());
               }else
               {
                   System.out.println("文件移动失败");
               }

        }else
            {
                System.out.println("文件不存在，无法移动");
            }

        }

    //文件夹的遍历
    public static String scaner(File folder)
    {
        String floderList=new String(folder.getAbsolutePath())+"%";
        if(folder.isDirectory())
        {
            File next []=folder.listFiles();
            for(int i=0;i<next.length;i++)
            {
                if(next[i].isDirectory())
                    floderList=floderList +"Dir:"+next[i].getName()+"%";
                else
                    floderList=floderList+"File:"+next[i].getName()+"%";
            }
        }
        return floderList;
    }


}
