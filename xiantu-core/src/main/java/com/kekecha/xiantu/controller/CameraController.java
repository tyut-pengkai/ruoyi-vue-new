package com.kekecha.xiantu.controller;

import com.kekecha.xiantu.domain.Camera;
import com.kekecha.xiantu.domain.Site;
import com.kekecha.xiantu.service.ICameraService;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/camera")
public class CameraController extends BaseController {
    @Autowired
    private ICameraService cameraService;

    @Anonymous
    @GetMapping("")
    public AjaxResult list(String filter, int start, int end)
    {
        if (start < 0 || start > end) {
            return AjaxResult.error("参数不合法");
        }

        AjaxResult ajaxResult = AjaxResult.success("查询成功");

        List<Camera> cameraList = cameraService.selectAll(filter);

        int total = cameraList.size();
        ajaxResult.put("total", total);

        int search_end = Math.min(end, total);
        if (start >= total) {
            List<Camera> sub_list = new ArrayList<>();
            ajaxResult.put("data", sub_list);
        } else {
            List<Camera> sub_list = cameraList.subList(start, search_end);
            ajaxResult.put("data", sub_list);
        }
        return ajaxResult;
    }

    @Anonymous
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody Camera camera)
    {
        Camera old_camera = cameraService.selectByName(camera.getName());
        if (old_camera != null) {
            System.out.println(old_camera.toString());
            cameraService.update(camera);
            return AjaxResult.success("更新成功");
        } else {
            System.out.println("old camera " + camera.getName() + " not found");
            cameraService.insert(camera);
            return AjaxResult.success("创建成功");
        }
    }

    @Anonymous
    @DeleteMapping("")
    public AjaxResult delete(String name)
    {
        if (cameraService.delete(name) <= 0) {
            System.out.println("Camera " + name + " not exist");
        }
        return AjaxResult.success("删除成功");
    }

}
