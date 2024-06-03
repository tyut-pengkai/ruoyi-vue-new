package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SysConfigWithdraw;
import com.ruoyi.system.service.ISysConfigWithdrawService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 网站Controller
 *
 * @author zwgu
 * @date 2022-03-22
 */
@RestController
@RequestMapping("/system/withdraw/config")
public class SysConfigWithdrawController extends BaseController {

    @Resource
    private ISysConfigWithdrawService sysWithdrawService;

    /**
     * 获取网站配置信息
     */
    @GetMapping
    public AjaxResult getConfig() {

        SysConfigWithdraw withdraw = sysWithdrawService.getById(1);
        return AjaxResult.success(withdraw);
    }

    /**
     * 修改配置
     */
    @Log(title = "网站配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysConfigWithdraw withdraw) {
        withdraw.setId(1L);
        withdraw.setUpdateBy(getUsername());
        withdraw.setUpdateTime(DateUtils.getNowDate());
        return toAjax(sysWithdrawService.saveOrUpdate(withdraw));
    }
}
