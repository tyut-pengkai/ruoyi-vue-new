package com.ruoyi.system.service;

import cn.hutool.core.util.ArrayUtil;
import com.ruoyi.common.utils.PathUtils;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@SpringBootTest
public class QuickAccessTest {

    @Test
    public void test() {
        try {
            byte[] split = "||||".getBytes(StandardCharsets.UTF_8);
            byte[] apiUrl = "1".getBytes(StandardCharsets.UTF_8);
            byte[] appSecret = "2".getBytes(StandardCharsets.UTF_8);
            byte[] bytes = FileUtils.readFileToByteArray(new File(PathUtils.getUserPath() + File.separator + ".." + File.separator + "template.exe"));
            byte[] joinedBytes = ArrayUtil.addAll(bytes, split, apiUrl, split, appSecret);
            FileUtils.writeByteArrayToFile(new File(PathUtils.getUserPath() + File.separator + ".." + File.separator + "template_new.exe"), joinedBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}