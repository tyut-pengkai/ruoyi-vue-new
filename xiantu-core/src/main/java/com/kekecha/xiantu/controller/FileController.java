package com.kekecha.xiantu.controller;

import com.kekecha.xiantu.service.impl.FileServiceImpl;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController extends BaseController {

    @Autowired
    private FileServiceImpl fileServiceImpl;

    @Anonymous
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

    @Anonymous
    @DeleteMapping("")
    public AjaxResult delete(@RequestParam("url") String fileUrl) throws Exception
    {
        if (!fileServiceImpl.isValidPath(fileUrl)) {
            return AjaxResult.error("路径非法");
        }

        String ret = fileServiceImpl.delete(fileUrl);
        if (ret.isEmpty()) {
            return AjaxResult.success("删除成功");
        } else {
            return AjaxResult.error("删除失败: " + ret);
        }
    }
}
