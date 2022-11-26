package com.ruoyi.update.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Utils {

    public static String makePath(String path) {
        String temp = StringUtils.replace(path, "\\\\", File.separator);
        temp = StringUtils.replace(temp, "\\", File.separator);
        temp = StringUtils.replace(temp, "/", File.separator);
        return temp;
    }

    public static String makeUrl(String url) {
        String temp = StringUtils.replace(url, "\\\\", "/");
        temp = StringUtils.replace(temp, "\\", "/");
        return temp;
    }

    public static String fillPath(String path) {
        if (!path.endsWith(File.separator)) {
            return path + File.separator;
        }
        return path;
    }

    public static String fillUrl(String url) {
        if (!url.endsWith("/")) {
            return url + "/";
        }
        return url;
    }

    public static String makeDirPath(String path) {
        String temp = makePath(path);
        return fillPath(temp);
    }

    public static String readFromUrl(String url) {
        StringBuilder stringBuffer = new StringBuilder();
        try {
            URL uri = new URL(url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(uri.openStream(), StandardCharsets.UTF_8));
            String s = null;
            while ((s = reader.readLine()) != null) {
                stringBuffer.append(stringBuffer.length() > 0 ? System.getProperty("line.separator") : "").append(s);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    public static void main(String[] args) {
        String path = "c:/aa/bb/cc";
        String path2 = "c:\\aa\\bb\\cc";
        String path3 = "c:\\\\aa\\\\bb\\\\cc";
        System.out.println(Utils.makePath(path));
        System.out.println(Utils.makePath(path2));
        System.out.println(Utils.makePath(path3));
    }

    public static void forceDelete(File file) throws IOException {
        IOException e = new IOException();
        try {
            FileUtils.forceDelete(file);
        } catch (IOException e1) {
            int tryCount = 0;
            while (e != null && tryCount++ < 10) {
                try {
                    e = null;
                    System.gc();    //回收资源
                    FileUtils.forceDelete(file);
                } catch (IOException e2) {
                    e = e2;
                }
            }
            if (e != null) {
                throw e;
            }
        }
    }

}
