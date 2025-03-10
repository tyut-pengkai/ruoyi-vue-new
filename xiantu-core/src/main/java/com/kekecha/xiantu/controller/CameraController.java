package com.kekecha.xiantu.controller;

import com.kekecha.xiantu.domain.Camera;
import com.kekecha.xiantu.domain.Site;
import com.kekecha.xiantu.service.ICameraService;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public AjaxResult list(
            @RequestParam(name="filter", required = false) String filter,
            @RequestParam(name="pageNum", defaultValue = "0") int pageNum,
            @RequestParam(name="pageSize", defaultValue = "0") int pageSize)
    {
        boolean select_all = false;

        if (pageNum <= 0) {
            return AjaxResult.error("参数不合法");
        }

        if (pageSize <= 0) {
            select_all = true;
        }

        AjaxResult ajaxResult = AjaxResult.success("查询成功");

        List<Camera> cameraList = cameraService.selectAll(filter);

        int total = cameraList.size();
        ajaxResult.put("total", total);

        if (select_all) {
            ajaxResult.put("data", cameraList);
        } else {
            int search_start = (pageNum - 1) * pageSize;
            int search_end = Math.min((pageNum * pageSize), total);
            // 如果 start 超过列表大小，返回空列表
            search_end = Math.min(search_end, total);
            if (search_start >= total) {
                List<Camera> sub_list = new ArrayList<>();
                ajaxResult.put("data", sub_list);
            } else {
                List<Camera> sub_list = cameraList.subList(search_start, search_end);
                ajaxResult.put("data", sub_list);
            }
        }


        return ajaxResult;
    }

    @PreAuthorize("@ss.hasPermi('data:camera:list')")
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

    @PreAuthorize("@ss.hasPermi('data:camera:list')")
    @DeleteMapping("")
    public AjaxResult delete(@RequestParam("name") String name)
    {
        if (cameraService.delete(name) <= 0) {
            System.out.println("Camera " + name + " not exist");
        }
        return AjaxResult.success("删除成功");
    }

    @PreAuthorize("@ss.hasPermi('data:camera:list')")
    @GetMapping("/state")
    public AjaxResult getCameraState(@RequestParam(name="pageNum", defaultValue = "0") int pageNum,
                                     @RequestParam(name="pageSize", defaultValue = "0") int pageSize)
    {
        AjaxResult ajaxResult = AjaxResult.success();
        ajaxResult.put("total", 0);
        ajaxResult.put("normal", 0);
        ajaxResult.put("error", 0);
        ajaxResult.put("disabled", 0);
        return ajaxResult;
    }
}
