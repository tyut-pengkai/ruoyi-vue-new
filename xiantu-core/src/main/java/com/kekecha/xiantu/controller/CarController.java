package com.kekecha.xiantu.controller;

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
    public TableDataInfo list()
    {
        startPage();
        List<CarOverview> list = carService.selectCarOverviewList();
        return getDataTable(list);
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

    @Anonymous
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

    @Anonymous
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

    @Anonymous
    @PostMapping("/image/upload")
    public AjaxResult uploadImageFile(@RequestParam("name") String name,
                                 @RequestParam("file") MultipartFile file) throws Exception
    {
        try
        {
            // 图片上传真实路径
            String imageUploadRealPath = RuoYiConfig.getUploadPath();
            // 上传到真实路径后，返回虚拟的图片路径
            String imageUploadMaskPath = FileUploadUtils.upload(imageUploadRealPath, file);

            Car car = carService.appendImageToCar(imageUploadMaskPath, name);
            if (car != null) {
                AjaxResult ajax = AjaxResult.success();
                ajax.put("image", car.getImageUrl().split(","));
                return ajax;
            } else {
                return AjaxResult.error("车型不存在");
            }
        }
        catch (Exception e)
        {
            System.out.println("exception : " + e.getMessage());
            return AjaxResult.error(e.getMessage());
        }
    }

    @Anonymous
    @DeleteMapping("/image")
    public AjaxResult deleteImageFile(@RequestParam("name") String name,
                                 @RequestParam("file") String imageFilePath) throws Exception
    {
        try
        {
            if (carService.removeRealPathFile(imageFilePath) == 1) {
                return AjaxResult.error("路径错误");
            }

            Car car = carService.removeImageFromCar(imageFilePath, name);
            if (car != null) {
                return AjaxResult.success("删除成功");
            } else {
                return AjaxResult.error("车型不存在");
            }
        }
        catch (Exception e)
        {
            System.out.println("exception : " + e.getMessage());
            return AjaxResult.error(e.getMessage());
        }
    }
}
