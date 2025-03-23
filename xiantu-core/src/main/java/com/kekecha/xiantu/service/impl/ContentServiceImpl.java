package com.kekecha.xiantu.service.impl;

import com.kekecha.xiantu.service.IContentService;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
public class ContentServiceImpl implements IContentService {
    @Override
    public int saveToFile(String type, String content)
    {
        Path baseDir = Paths.get("./content");
        try {
            Files.createDirectories(baseDir);
        } catch (IOException e) {
            throw new RuntimeException("无法创建目录: " + baseDir, e);
        }

        String fileName = String.format("%s.txt", type);
        Path filePath = baseDir.resolve(fileName);

        try {
            Files.write(filePath,
                    content.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.CREATE);
            return 0;
        } catch (IOException e) {
            throw new RuntimeException("文件写入失败", e);
        }
    }

    public String getContentByType(String type) {
        try {
            Path baseDir = Paths.get("./content");
            Path filePath = baseDir.resolve(type + ".txt");

            if (!Files.exists(filePath)) {
                throw new FileNotFoundException("未找到类型为 " + type + " 的文件");
            }

            byte[] bytes = Files.readAllBytes(filePath);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "";
        }
    }
}
