package com.code.testonline.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件工具
 */
public class FileUtils {

    /**
     * 获取app文件缓存路径
     *
     * @param context
     * @return
     */
    public static String getDiscFileDir(Context context) {
        String filePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            filePath = context.getExternalFilesDir(null).getAbsolutePath() + "/temp";
        } else {
            filePath = context.getFilesDir().getAbsolutePath() + "/temp";
        }
        File file = new File(filePath);
        if(!file.exists()){
            file.mkdirs();
        }
        return filePath;
    }

    /**
     * 在SD卡上创建文件
     *
     * @param fileName 文件名
     * @param dir 子目录
     * @return 新建的File
     * @throws IOException
     */
    public static File createFileInSDCard(Context context, String fileName, String dir) throws IOException {
        File file = new File(getDiscFileDir(context) + dir + File.separator + fileName);
        if(file.createNewFile()){
            return file;
        }
        return null;
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dir 目录
     * @return 文件夹对象
     * @throws IOException
     */
    public static File createSDDir(Context context, String dir) {
        File dirFile = new File(getDiscFileDir(context) + dir);
        dirFile.mkdirs();
        return dirFile;
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName 文件名
     * @param dir 文件所在目录
     * @return
     */
    public static boolean isFileExist(Context context, String fileName, String dir) {
        File file = new File(getDiscFileDir(context) + dir + File.separator + fileName);
        return file.exists();
    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     *
     * @param fileName 文件名
     * @param dir 目录
     * @param input 输入流
     * @return
     */
    public static File write2SDFromInput(Context context,String fileName, String dir, InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            //创建目录
            createSDDir(context, dir);
            //创建文件
            file = createFileInSDCard(context, fileName, dir);
            //写数据流
            output = new FileOutputStream(file);
            byte buffer[] = new byte[4 * 1024];//每次存4K
            int temp;
            //写入数据
            while ((temp = input.read(buffer)) != -1) {
                output.write(buffer, 0, temp);
            }
            output.flush();

        } catch (Exception e) { //输出错误
            System.out.println("写数据异常：" + e);
        } finally { //关闭资源
            try {
                if (output != null) {
                    output.close();
                }
            } catch (Exception e2) {
                System.out.println(e2.getMessage());
            }
        }

        //返回结果
        return file;
    }

}

