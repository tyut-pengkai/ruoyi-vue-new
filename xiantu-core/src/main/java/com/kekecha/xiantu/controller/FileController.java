package com.kekecha.xiantu.controller;

import com.kekecha.xiantu.service.impl.FileServiceImpl;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.framework.web.domain.server.Sys;
import org.springframework.beans.factory.annotation.Autowired;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/file")
public class FileController extends BaseController {

    @Autowired
    private FileServiceImpl fileServiceImpl;

    // 当前只有这些界面允许上传
    @PreAuthorize("@ss.hasAnyPermi({'data:car:list', 'data:new:list','data:knowledge:list','data:content:list'})")
    @PostMapping("")
    public AjaxResult upload(@RequestParam("file") MultipartFile file) throws Exception
    {
        String ret = fileServiceImpl.upload(file);
        if (!ret.isEmpty()) {
            AjaxResult ajax = AjaxResult.success("上传成功");
            ajax.put("url", ret);
            return ajax;
        } else {
            return AjaxResult.error("上传失败");
        }
    }

    @PreAuthorize("@ss.hasAnyPermi({'data:car:list', 'data:new:list','data:knowledge:list','data:content:list'})")
    @DeleteMapping("")
    public AjaxResult delete(@RequestParam("url") String fileUrl) throws Exception
    {
        Path p = Paths.get(fileUrl).normalize();
        if(!p.startsWith("/profile/upload/")) {
            return AjaxResult.error("路径非法");
        }
        try {
            fileServiceImpl.delete(fileUrl);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return AjaxResult.success("删除成功");
    }
}
