package com.ruoyi.system.service;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.junit.Test;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandTest {

    @Test
    public void testCommmond() {
        String command = "";
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Windows")) {
            command = "cmd /c start \"D:\\网络验证\\红叶\\release\\红叶网络验证系统_v1.0.0_20221018203100_test_update\\upload\\hy1.bat\"";
        } else if (osName.startsWith("Linux")) {
            command = "sh hy.sh restart";
        }
        System.out.println(command);
        CommandLine commandLine = CommandLine.parse(command);
        DefaultExecutor exec = new DefaultExecutor();
        try {
            exec.execute(commandLine);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            Runtime.getRuntime().exec("cmd /c start dir");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    @Test
    public void testUrl() {
        String url = "http://www.baidu.com/www/88990";
        Pattern p = Pattern.compile("(https?://[^/]+/?).*?");
        Matcher matcher = p.matcher(url);
        if(matcher.matches()) {
            System.out.println(matcher.group(1));
        }
    }


}
