package com.kekecha.xiantu.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kekecha.xiantu.domain.*;
import com.kekecha.xiantu.service.ICarService;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.config.ServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.web.multipart.MultipartFile;

import static com.ruoyi.common.utils.PageUtils.startPage;

@RestController
@RequestMapping("/car")
public class CarController extends BaseController {

    private ServerConfig serverConfig;

    @Autowired
    private ICarService carService;

    @Anonymous
    @GetMapping("")
    public AjaxResult list(int pageNum, int pageSize)
    {
        boolean select_all = false;

        if (pageNum <= 0) {
            return AjaxResult.error("参数不合法");
        }

        if (pageSize <= 0) {
            select_all = true;
        }

        AjaxResult ajaxResult = AjaxResult.success("查询成功");

        List<CarOverview> list = carService.selectCarOverviewList();
        int total = list.size();
        ajaxResult.put("total", total);

        if (select_all) {
            ajaxResult.put("data", list);
        } else {
            int search_start = (pageNum - 1) * pageSize;
            int search_end = Math.min((pageNum * pageSize), total);
            // 如果 start 超过列表大小，返回空列表
            search_end = Math.min(search_end, total);
            if (search_start >= total) {
                List<CarOverview> sub_list = new ArrayList<>();
                ajaxResult.put("data", sub_list);
            } else {
                List<CarOverview> sub_list = list.subList(search_start, search_end);
                ajaxResult.put("data", sub_list);
            }
        }
        return ajaxResult;
    }

    @Anonymous
    @GetMapping("/detail")
    public AjaxResult getCarDetail(@RequestParam("name") String name)
    {
        Car car = carService.selectCarDetail(name);
        if (car != null) {
            return AjaxResult.success(carService.ConverCarToJson(car));
        } else {
            return AjaxResult.error("查询的车型不存在");
        }
    }

    @PreAuthorize("@ss.hasPermi('data:car:list')")
    @PostMapping("/edit")
    public AjaxResult editCar(@RequestBody Map<String, Object> jsonMap)
    {
        Car car = carService.buildCarFromJson(jsonMap);
        Car db_car = carService.selectCarDetail(car.getName());

        if (db_car != null) {
            /* 更新操作 */
            if (carService.updateCar(car) > 0) {
                return AjaxResult.success("更新成功");
            } else {
                return AjaxResult.error("更新目标车型不存在");
            }
        } else {
            /* 创建操作 */
            if (carService.insertCar(car) > 0) {
                return AjaxResult.success("创建成功");
            } else {
                return AjaxResult.error("创建失败");
            }
        }
    }

    @PreAuthorize("@ss.hasPermi('data:car:list')")
    @DeleteMapping("")
    public AjaxResult deleteCar(@RequestParam("name") String name)
    {
        Car car = carService.selectCarDetail(name);
        if (car == null) {
            return AjaxResult.error("删除车型不存在");
        }

        int res = carService.deleteCar(name);

        if (res > 0) {
            return AjaxResult.success("删除成功");
        } else {
            return AjaxResult.error("删除车型不存在");
        }
    }

//
//    @PostMapping("/image/upload")
//    public AjaxResult uploadImageFile(@RequestParam("name") String name,
//                                 @RequestParam("file") MultipartFile file) throws Exception
//    {
//        try
//        {
//            // 图片上传真实路径
//            String imageUploadRealPath = RuoYiConfig.getUploadPath();
//            // 上传到真实路径后，返回虚拟的图片路径
//            String imageUploadMaskPath = FileUploadUtils.upload(imageUploadRealPath, file);
//
//            Car car = carService.appendImageToCar(imageUploadMaskPath, name);
//            if (car != null) {
//                AjaxResult ajax = AjaxResult.success();
//                ajax.put("image", car.getImageUrl().split(","));
//                return ajax;
//            } else {
//                return AjaxResult.error("车型不存在");
//            }
//        }
//        catch (Exception e)
//        {
//            System.out.println("exception : " + e.getMessage());
//            return AjaxResult.error(e.getMessage());
//        }
//    }
//
//    @Anonymous
//    @DeleteMapping("/image")
//    public AjaxResult deleteImageFile(@RequestParam("name") String name,
//                                 @RequestParam("file") String imageFilePath) throws Exception
//    {
//        try
//        {
//            if (carService.removeRealPathFile(imageFilePath) == 1) {
//                return AjaxResult.error("路径错误");
//            }
//
//            Car car = carService.removeImageFromCar(imageFilePath, name);
//            if (car != null) {
//                return AjaxResult.success("删除成功");
//            } else {
//                return AjaxResult.error("车型不存在");
//            }
//        }
//        catch (Exception e)
//        {
//            System.out.println("exception : " + e.getMessage());
//            return AjaxResult.error(e.getMessage());
//        }
//    }
}
