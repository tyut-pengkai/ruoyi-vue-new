package com.kekecha.xiantu.controller;

import com.kekecha.xiantu.domain.Camera;
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
        try {
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
        } catch (Exception e) {
            return AjaxResult.error("系统异常，获取数据失败");
        }
    }

    @PreAuthorize("@ss.hasPermi('data:camera:list')")
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody Camera camera)
    {
        try {
            Camera old_camera = cameraService.selectByName(camera.getName());
            if (old_camera != null) {
                cameraService.update(camera);
                return AjaxResult.success("更新成功");
            } else {
                cameraService.insert(camera);
                return AjaxResult.success("创建成功");
            }
        } catch (Exception e) {
            return AjaxResult.error("创建失败, 内部参数错误");
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


//    /**
//     * 获取播放地址
//     */
//    @GetMapping("/getPreviewURL")
//    public AjaxResult getPreviewURL(
//            @RequestParam(required = true, defaultValue = "") String cameraName) throws Exception {
//        try {
//            if (cameraName == null || "".equals(cameraName)) {
//                return AjaxResult.error("获取摄像头异常");
//            }
//
//            Camera camera = cameraService.selectByName(cameraName);
//
//            String url = cameraService.getPriviewURL(camera);
//            return AjaxResult.success(url);
//        } catch (Exception e) {
//            return AjaxResult.error(e.getMessage());
//        }
//    }
//    public AjaxResult getPreviewURL(@RequestParam String indexCode, @RequestParam(required = false) String streamType,
//                                   @RequestParam(required = false) String protocol,
//                                   @RequestParam String source) throws Exception {
//        String previewURL = "";
//        if (MonitorVideoSourceEnum.HIKVISION.getSource().equals(source) || MonitorVideoSourceEnum.FORESTFIREMONITORPLATFORM.getSource().equals(source)
//                || MonitorVideoSourceEnum.YANXIFORESTFARM.getSource().equals(source)) {
//            previewURL = monitorVideoService.getHikvisionPreviewURL(indexCode, streamType, source, protocol);
//        } else if (MonitorVideoSourceEnum.GEYE.getSource().equals(source)) {
//            previewURL = monitorVideoService.getGeyePreviewURL(indexCode, streamType, source, protocol);
//        }
//        return R.data(previewURL);
//    }

//    /**
//     * 获取回放地址
//     */
//    @GetMapping("/getPlaybackURL")
//    public AjaxResult getPlaybackURL(@RequestParam String indexCode,
//                                    @RequestParam String source,
//                                    @RequestParam(required = false) String protocol,
//                                    @ApiParam("yyyy-MM-dd hh:mm:ss") @RequestParam String beginTime,
//                                    @ApiParam("yyyy-MM-dd hh:mm:ss") @RequestParam String endTime) throws Exception {
//        String previewURL = "";
//        if (MonitorVideoSourceEnum.HIKVISION.getSource().equals(source) || MonitorVideoSourceEnum.FORESTFIREMONITORPLATFORM.getSource().equals(source)
//                || MonitorVideoSourceEnum.YANXIFORESTFARM.getSource().equals(source)) {
//            previewURL = monitorVideoService.getHikvisionPlaybackURL(indexCode, source, protocol, beginTime, endTime);
//        } else if (MonitorVideoSourceEnum.GEYE.getSource().equals(source)) {
//            previewURL = monitorVideoService.getGeyePlaybackURL(indexCode, source, beginTime, endTime);
//        }
//        return R.data(previewURL);
//    }

//    /**
//     * 云台操作(source=1)
//     */
//    @GetMapping("/controlling")
////    @ApiImplicitParams({
////            @ApiImplicitParam(name = "action", value = "时候  0-开始 ，1-停止 ", paramType = "query", dataType = "number"),
////            @ApiImplicitParam(name = "command", value = "时候 LEFT 左转 RIGHT右转 UP 上转 DOWN 下转 ZOOM_IN 焦距变大 " +
////                    "ZOOM_OUT 焦距变小 LEFT_UP 左上 LEFT_DOWN 左下 RIGHT_UP 右上 RIGHT_DOWN 右下 FOCUS_NEAR 焦点前移 " +
////                    "FOCUS_FAR 焦点后移 IRIS_ENLARGE 光圈扩大 IRIS_REDUCE 光圈缩小", paramType = "query", dataType = "string"),
////            @ApiImplicitParam(name = "speed", value = "云台速度，取值范围为1-100，默认50", paramType = "query", dataType = "number"),
////    })
//    public AjaxResult controlling(@RequestParam String indexCode,
//                         @RequestParam String source,
//                         @ApiIgnore @RequestParam Integer action,
//                         @ApiIgnore @RequestParam String command,
//                         @ApiIgnore @RequestParam(required = false) Integer speed) {
//        return R.status(monitorVideoService.hikvisionControlling(indexCode, action, command, source, speed));
//    }

}
