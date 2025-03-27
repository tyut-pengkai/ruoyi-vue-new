package com.kekecha.xiantu.controller;

import com.kekecha.xiantu.domain.CameraInstance;
import com.kekecha.xiantu.domain.CameraPlatform;
import com.kekecha.xiantu.domain.Site;
import com.kekecha.xiantu.service.ICameraService;
import com.kekecha.xiantu.service.ISiteService;
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
    @Autowired
    private ISiteService siteService;

    @Anonymous
    @GetMapping("")
    public AjaxResult list(
            @RequestParam(name="filter", required = false) String filter,
            @RequestParam(name="pageNum", defaultValue = "0") int pageNum,
            @RequestParam(name="pageSize", defaultValue = "0") int pageSize)
    {
        boolean select_all = false;
        try {
            if (pageNum <= 0) {
                return AjaxResult.error("参数不合法");
            }
            if (pageSize <= 0) {
                select_all = true;
            }
            AjaxResult ajaxResult = AjaxResult.success("查询成功");
            List<CameraPlatform> cameraPlatformList = cameraService.selectAll(filter);
            int total = cameraPlatformList.size();
            ajaxResult.put("total", total);
            if (select_all) {
                ajaxResult.put("data", cameraPlatformList);
            } else {
                int search_start = (pageNum - 1) * pageSize;
                int search_end = Math.min((pageNum * pageSize), total);

                search_end = Math.min(search_end, total);
                if (search_start >= total) {
                    List<CameraPlatform> sub_list = new ArrayList<>();
                    ajaxResult.put("data", sub_list);
                } else {
                    List<CameraPlatform> sub_list = cameraPlatformList.subList(search_start, search_end);
                    ajaxResult.put("data", sub_list);
                }
            }
            return ajaxResult;
        } catch (Exception e) {
            return AjaxResult.error("系统异常，获取数据失败");
        }
    }

    @PreAuthorize("@ss.hasPermi('data:camera:list')")
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody CameraPlatform cameraPlatform)
    {
        try {
            CameraPlatform old_cameraPlatform = cameraService.selectByName(cameraPlatform.getName());
            if (old_cameraPlatform != null) {
                cameraService.update(cameraPlatform);
                return AjaxResult.success("更新成功");
            } else {
                cameraService.insert(cameraPlatform);
                return AjaxResult.success("创建成功");
            }
        } catch (Exception e) {
            return AjaxResult.error("创建失败, 内部参数错误");
        }
    }

    @PreAuthorize("@ss.hasPermi('data:camera:list')")
    @PostMapping("/link")
    public AjaxResult link(@RequestParam(required = false, defaultValue = "2") int type,
                           @RequestParam String indexCode,
                           @RequestParam String siteName)
    {
        if (indexCode == null || indexCode.isEmpty() || siteName == null || siteName.isEmpty()) {
            return AjaxResult.error("参数错误");
        }

        Site site = siteService.selectOne(type, siteName);
        if (site == null) {
            return AjaxResult.error("目标地点不存在");
        }

        try {
            cameraService.clearCameraLink(indexCode);
            cameraService.linkCameraToSite(indexCode, site.getId());
            return AjaxResult.success();
        } catch (Exception e) {
            return AjaxResult.error("创建失败, 内部参数错误" + e.getMessage());
        }
    }

    @PreAuthorize("@ss.hasPermi('data:camera:list')")
    @DeleteMapping("")
    public AjaxResult delete(@RequestParam("name") String name)
    {
        try {
            cameraService.delete(name);
            return AjaxResult.success("删除成功");
        } catch (Exception e) {
            return AjaxResult.error("删除失败");
        }
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

    @Anonymous
    @GetMapping("/list")
    public AjaxResult getCameraList(@RequestParam(name="pageNum", defaultValue = "0") int pageNum,
                                    @RequestParam(name="pageSize", defaultValue = "0") int pageSize,
                                    @RequestParam(name="platform", defaultValue = "") String platformName)
    {
        if (platformName == null || platformName.isEmpty()) {
            return AjaxResult.error("查询的平台名称异常");
        }

        boolean select_all = false;
        try {
            if (pageNum <= 0) {
                return AjaxResult.error("参数不合法");
            }
            if (pageSize <= 0) {
                select_all = true;
            }
            AjaxResult ajaxResult = AjaxResult.success("查询成功");
            /* 获取平台信息 */
            CameraPlatform cameraPlatform = cameraService.selectByName(platformName);
            if (cameraPlatform == null) {
                return AjaxResult.error("查询的平台不存在");
            }
            /* 构建查询 */
            List<CameraInstance> list = cameraService.getPlatformCameraInstances(cameraPlatform);
            int total = list.size();
            ajaxResult.put("total", total);
            if (select_all) {
                ajaxResult.put("data", list);
            } else {
                int search_start = (pageNum - 1) * pageSize;
                int search_end = Math.min((pageNum * pageSize), total);

                search_end = Math.min(search_end, total);
                if (search_start >= total) {
                    List<CameraInstance> sub_list = new ArrayList<>();
                    ajaxResult.put("data", sub_list);
                } else {
                    List<CameraInstance> sub_list = list.subList(search_start, search_end);
                    ajaxResult.put("data", sub_list);
                }
            }
            return ajaxResult;
        } catch (Exception e) {
            return AjaxResult.error("系统异常，获取数据失败");
        }
    }

    /**
     * 获取播放地址
     */
    @Anonymous
    @GetMapping("/getPreviewURL")
    public AjaxResult getPreviewURL(
            @RequestParam(required = true) String platform,
            @RequestParam(required = true) String indexCode) throws Exception {
        try {
            if (platform.isEmpty() || indexCode.isEmpty()) {
                return AjaxResult.error("参数异常");
            }

            CameraPlatform cameraPlatform = cameraService.selectByName(platform);
            if (cameraPlatform == null) {
                return AjaxResult.error("平台名称异常");
            }

            String url = cameraService.getHikvisionPreviewURL(cameraPlatform, indexCode);
            AjaxResult ajaxResult = AjaxResult.success();
            ajaxResult.put("url", url);
            return ajaxResult;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 获取回放地址
     */
    @Anonymous
    @GetMapping("/getPlaybackURL")
    public AjaxResult getPlaybackURL(
            @RequestParam(required = true) String platform,
            @RequestParam(required = true) String indexCode,
            @RequestParam(required = true) String beginTime,
            @RequestParam(required = true) String endTime) throws Exception {
        if (platform.isEmpty() || indexCode.isEmpty() || beginTime.isEmpty() || endTime.isEmpty()) {
            return AjaxResult.error("参数异常");
        }

        CameraPlatform cameraPlatform = cameraService.selectByName(platform);
        if (cameraPlatform == null) {
            return AjaxResult.error("平台名称异常");
        }

        AjaxResult ajaxResult = AjaxResult.success("获取成功");
        String previewUrl = cameraService.getHikvisionPlaybackURL(cameraPlatform, indexCode, beginTime, endTime);
        ajaxResult.put("url", previewUrl);
        return ajaxResult;
    }

    /**
     * 云台操作
     */
    @Anonymous
    @GetMapping("/controlling")
    public AjaxResult controlling(
            @RequestParam(required = true) String platform,
            @RequestParam(required = true) String indexCode,
            @RequestParam(required = true) Integer action,
            @RequestParam(required = true) String command,
            @RequestParam(required = false) Integer speed) {

        /*
        * action 0-开始 ，1-停止
        * command
        *   LEFT 左转 RIGHT右转 UP 上转 DOWN 下转
        *   ZOOM_IN 焦距变大 ZOOM_OUT 焦距变小
        *   LEFT_UP 左上 LEFT_DOWN 左下 RIGHT_UP 右上 RIGHT_DOWN 右下
        *   FOCUS_NEAR 焦点前移 FOCUS_FAR 焦点后移
        *   IRIS_ENLARGE 光圈扩大 IRIS_REDUCE 光圈缩小
        * speed 云台速度，取值范围为1-100，默认50
        */

        if (platform.isEmpty() || indexCode.isEmpty() || command.isEmpty()) {
            return AjaxResult.error("参数异常");
        }

        CameraPlatform cameraPlatform = cameraService.selectByName(platform);
        if (cameraPlatform == null) {
            return AjaxResult.error("平台名称异常");
        }

        return AjaxResult.success(
                cameraService.hikvisionControlling(cameraPlatform, indexCode, action, command, speed));
    }

}
