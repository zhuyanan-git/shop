package com.qfant.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 下载文件工具类
 * @author 乐乐
 * @site www.javanood.com
 * @create 2019-09-30 17:28
 */
public class DownloadFileZipUtil {

    public static void downloadZip(File file, HttpServletResponse response) {
        try {
            // 以流的形式下载文件。
            BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
            file.delete();  //将生成的服务器端文件删除
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}