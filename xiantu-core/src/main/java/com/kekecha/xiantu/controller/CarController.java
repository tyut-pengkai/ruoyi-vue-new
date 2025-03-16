package com.kekecha.xiantu.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.kekecha.xiantu.domain.*;
import com.kekecha.xiantu.service.ICarService;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.framework.config.ServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;

@RestController
@RequestMapping("/car")
public class CarController extends BaseController {
    @Autowired
    private ICarService carService;

    @Anonymous
    @GetMapping("")
    public AjaxResult list(int pageNum, int pageSize)
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
            List<CarOverview> list = carService.selectCarOverviewList();
            int total = list.size();
            ajaxResult.put("total", total);
            if (select_all) {
                ajaxResult.put("data", list);
            } else {
                int search_start = (pageNum - 1) * pageSize;
                int search_end = Math.min((pageNum * pageSize), total);
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
        } catch (Exception e) {
            return AjaxResult.error("系统异常，获取数据失败");
        }
    }

    @Anonymous
    @GetMapping("/detail")
    public AjaxResult getCarDetail(@RequestParam(name="name", defaultValue = "") String name,
                                   @RequestParam(name="id", defaultValue = "-1") int id)
    {
        Car car = null;
        try {
            if (id != -1) {
                car = carService.selectCarDetailByID(id);
            } else {
                car = carService.selectCarDetail(name);
            }
            if (car != null) {
                return AjaxResult.success(carService.ConverCarToJson(car));
            } else {
                return AjaxResult.error("查询的车型不存在");
            }
        } catch (Exception e) {
            return AjaxResult.error("系统异常，获取数据失败" + e.getMessage());
        }
    }

    @PreAuthorize("@ss.hasPermi('data:car:list')")
    @PostMapping("/edit")
    public AjaxResult editCar(@RequestBody Map<String, Object> jsonMap)
    {
        try {
            Car car = carService.buildCarFromJson(jsonMap);
            Car db_car = carService.selectCarDetail(car.getName());

            if (db_car != null) {
                if (carService.updateCar(car) > 0) {
                    return AjaxResult.success("更新成功");
                } else {
                    return AjaxResult.error("更新目标车型不存在");
                }
            } else {
                if (carService.insertCar(car) > 0) {
                    return AjaxResult.success("创建成功");
                } else {
                    return AjaxResult.error("创建失败");
                }
            }
        } catch (Exception e) {
            return AjaxResult.error("操作失败，请重新尝试");
        }
    }

    @PreAuthorize("@ss.hasPermi('data:car:list')")
    @DeleteMapping("")
    public AjaxResult deleteCar(@RequestParam("name") String name)
    {
        try {
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
        } catch (Exception e) {
            return AjaxResult.error("删除失败，请刷新重试");
        }
    }
}
