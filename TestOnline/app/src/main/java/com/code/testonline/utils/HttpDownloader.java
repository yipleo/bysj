package com.code.testonline.utils;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * http下载器
 */
public class HttpDownloader {

    /**
     * 下载文件，并把文件存储在指定目录（-1：下载失败，0：下载成功，1：文件已存在）
     *
     * @param urlStr url
     * @param path 存文件的目录
     * @param fileName 文件名
     * @return 下载结果
     */
    public static int downloadFiles(Context context, String urlStr, String path, String fileName) {
        try {
            if (FileUtils.isFileExist(context, fileName, path)){
                return 1;//判断文件是否存在
            } else {
                InputStream inputStream = getInputStreamFromUrl(urlStr);
                File resultFile = FileUtils.write2SDFromInput(context, fileName, path, inputStream);
                if (resultFile == null) return -1;
            }
        } catch (Exception e) {
            System.out.println("读写数据异常:" + e);
            return -1;
        }
        return 0;
    }

    /**
     * 获取网络的输入流
     *
     * @param urlStr url
     * @return 输入流
     * @throws IOException
     */
    public static InputStream getInputStreamFromUrl(String urlStr) throws IOException {
        //创建一个URL对象
        URL url = new URL(urlStr);
        //创建一个HTTP链接
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        urlConn.setRequestMethod("GET"); //get请求
        urlConn.setConnectTimeout(6000); //超时6s
        //使用IO流获取数据
        return urlConn.getInputStream();
    }

}

