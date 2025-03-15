package com.kekecha.xiantu.service.impl;

import com.kekecha.xiantu.service.IFileService;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.file.FileUploadUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements IFileService {
    public String upload(MultipartFile file)
    {
        try
        {
            // 上传真实路径
            String fileUploadRealPath = RuoYiConfig.getUploadPath();
            // 上传到真实路径后，返回虚拟的图片路径
            return FileUploadUtils.upload(fileUploadRealPath, file);
        }
        catch (Exception e)
        {
            return "";
        }
    }

    public String delete(String file)
    {
        try {
            String fileRealPath = RuoYiConfig.getUploadPath() + file.substring("/profile/upload".length());
            Path path = Paths.get(fileRealPath);
            Files.delete(path);
            return "";
        } catch (Exception e)
        {
            return e.getMessage();
        }
    }
}
