package com.kekecha.xiantu.controller;

import com.kekecha.xiantu.domain.CarWanted;
import com.kekecha.xiantu.service.ICarWantedService;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.user.CaptchaException;
import com.ruoyi.common.exception.user.CaptchaExpireException;
import com.ruoyi.framework.web.service.SysLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/carwanted")
public class CarWantedController {
    @Autowired
    private ICarWantedService carWantedService;

    @Autowired
    private SysLoginService loginService;

    @PreAuthorize("@ss.hasPermi('data:carwanted:list')")
    @GetMapping("")
    public AjaxResult getList(
            @RequestParam(name="pageNum", defaultValue = "1")int pageNum,
            @RequestParam(name="pageSize", defaultValue = "10")int pageSize)
    {
        boolean select_all = false;

        if (pageNum <= 0) {
            return AjaxResult.error("参数不合法");
        }

        if (pageSize <= 0) {
            select_all = true;
        }

        AjaxResult ajaxResult = AjaxResult.success("查询成功");
        List<CarWanted> list = carWantedService.select();

        int total = list.size();
        ajaxResult.put("total", total);

        if (select_all) {
            ajaxResult.put("data", list);
        } else {
            int search_start = (pageNum - 1) * pageSize;
            int search_end = Math.min((pageNum * pageSize), total);
            if (search_start >= total) {
                List<CarWanted> result_list = new ArrayList<>();
                ajaxResult.put("data", result_list);
            } else {
                List<CarWanted> result_list = list.subList(search_start, search_end);
                ajaxResult.put("data", result_list);
            }
        }
        return ajaxResult;
    }

    @Anonymous
    @PostMapping("")
    public AjaxResult postCarWanted(@RequestBody Map<String, Object> jsonMap) {
        try {
            String uuid = jsonMap.get("uuid").toString();
            String code = jsonMap.get("code").toString();
            String name = jsonMap.get("name").toString();
            String phone = jsonMap.get("phone").toString();
            String company = jsonMap.get("company").toString();

            if (uuid.isEmpty() || code.isEmpty()) {
                return AjaxResult.error("验证失败");
            }

            loginService.justValidateCaptcha(code, uuid);

            if (name.isEmpty() || company.isEmpty() || phone.isEmpty()) {
                return AjaxResult.error("填写的表单信息不正确");
            }

            CarWanted carWanted = new CarWanted();
            carWanted.setName(name);
            carWanted.setPhone(phone);
            carWanted.setCompany(company);
            if (jsonMap.containsKey("city")) {
                String city = jsonMap.get("city").toString();
                carWanted.setCity(city);
            }
            long createTime = Instant.now().getEpochSecond();
            carWanted.setCreateTime(createTime);

            carWantedService.insert(carWanted);
            return AjaxResult.success("创建成功");
        } catch (CaptchaExpireException | CaptchaException e) {
            throw e;
        } catch (Exception e) {
            return AjaxResult.error("提交表单失败，请重试: " + e.getMessage());
        }
    }

    @PreAuthorize("@ss.hasPermi('data:carwanted:list')")
    @DeleteMapping("")
    public AjaxResult delete(int id) {
        try {
            int res = carWantedService.delete(id);
            if (res > 0) {
                return AjaxResult.success("删除成功");
            } else {
                return AjaxResult.error("删除的用车申请不存在");
            }
        } catch (Exception e) {
            return AjaxResult.error("删除失败");
        }
    }
}

