package com.kekecha.xiantu.controller;

import com.kekecha.xiantu.domain.MemberShip;
import com.kekecha.xiantu.domain.PostWanted;
import com.kekecha.xiantu.service.IMemberShipService;
import com.kekecha.xiantu.service.IPostWantedService;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.user.CaptchaException;
import com.ruoyi.common.exception.user.CaptchaExpireException;
import com.ruoyi.framework.web.service.SysLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/membership")
public class MemberShipController {
    @Autowired
    private IMemberShipService memberShipService;

    @Autowired
    private SysLoginService loginService;

    @PreAuthorize("@ss.hasPermi('data:membership:list')")
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
        List<MemberShip> list = memberShipService.select();

        int total = list.size();
        ajaxResult.put("total", total);

        if (select_all) {
            ajaxResult.put("data", list);
        } else {
            int search_start = (pageNum - 1) * pageSize;
            int search_end = Math.min((pageNum * pageSize), total);
            if (search_start >= total) {
                List<MemberShip> result_list = new ArrayList<>();
                ajaxResult.put("data", result_list);
            } else {
                List<MemberShip> result_list = list.subList(search_start, search_end);
                ajaxResult.put("data", result_list);
            }
        }
        return ajaxResult;
    }

    @Anonymous
    @PostMapping("")
    public AjaxResult postMemberShip(@RequestBody Map<String, Object> jsonMap) {
        try {
            String uuid = jsonMap.get("uuid").toString();
            String code = jsonMap.get("code").toString();
            String name = jsonMap.get("name").toString();
            String phone = jsonMap.get("phone").toString();
            String description = jsonMap.get("description").toString();
            int id = 0;
            if (jsonMap.containsKey("id")) {
                id = Integer.parseInt(jsonMap.get("id").toString());
            }

            if (uuid.isEmpty() || code.isEmpty() || phone == null) {
                return AjaxResult.error("验证失败");
            }

            loginService.justValidateCaptcha(code, uuid);

            MemberShip memberShip = new MemberShip();

            memberShip.setId(id);
            memberShip.setName(name);
            memberShip.setPhone(phone);
            memberShip.setDescription(description);

            long createTime = Instant.now().getEpochSecond();
            memberShip.setCreateTime(createTime);

            if (memberShip.getId() == 0) {
                memberShipService.insert(memberShip);
                return AjaxResult.success("创建成功");
            } else {
                memberShipService.update(memberShip);
                return AjaxResult.success("更新成功");
            }
        } catch (CaptchaExpireException | CaptchaException e) {
            throw e;
        } catch (Exception e) {
            return AjaxResult.error("提交表单失败，请重试");
        }
    }

    @PreAuthorize("@ss.hasPermi('data:membership:list')")
    @DeleteMapping("")
    public AjaxResult delete(int id) {
        try {
            int res = memberShipService.delete(id);
            if (res > 0) {
                return AjaxResult.success("删除成功");
            } else {
                return AjaxResult.error("删除的目标岗位不存在");
            }
        } catch (Exception e) {
            return AjaxResult.error("删除失败");
        }
    }
}
