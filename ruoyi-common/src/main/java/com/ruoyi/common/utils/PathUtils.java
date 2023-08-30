package com.ruoyi.common.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class PathUtils {

//    public static String getResourcesPath() {
//        URL url = PathUtils.class.getResource("/");
//        if (url != null) {
//            String path = url.getPath();
//            path = path.substring(1);
//            return handleEncode(path);
//        }
//        return null;
//    }
//
//    public static String getResourcesPath2() {
//        ClassPathResource classPathResource = new ClassPathResource("/");
//        try {
//            String path = classPathResource.getURL().getPath();
//            path = path.substring(1);
//            return handleEncode(path);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static String getResourcesPath(String name) {
//        return getResourcesPath() + name;
//    }

    /**
     * 获取resource目录下的文件，并返回副本（复制一份到临时目录）
     * @param name 文件名
     * @return
     */
    public static File getResourceFile(String name) {
        InputStream inputStream = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource(name);
            inputStream = classPathResource.getInputStream();
            File tempFile = File.createTempFile("temp", ".temp");
            FileUtils.copyInputStreamToFile(inputStream, tempFile);
            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return null;
    }


//    public static String getResourcesPath3() {
//        try {
//            String path = ResourceUtils.getURL("classpath:").getPath();
//            path = path.substring(1);
//            return handleEncode(path);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * 敲命令时所处的路径，取运行目录，jar包所在目录
     */
    public static String getUserPath() {
        String path = System.getProperty("user.dir");
        return handleEncode(path);
    }

//    /**
//     * 敲命令时所处的路径
//     */
//    public static String getUserPath2() {
//        try {
//            File file = new File("");
//            String path = file.getCanonicalPath();
//            return handleEncode(path);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    private static String handleEncode(String str) {
        try {
            return java.net.URLDecoder.decode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
