package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RateLimiter;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.LimitType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysConfigWebsite;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysConfigWebsiteService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 网站Controller
 *
 * @author zwgu
 * @date 2022-03-22
 */
@RestController
@RequestMapping("/system/website/config")
public class SysConfigWebsiteController extends BaseController {

    @Resource
    private ISysConfigWebsiteService sysWebsiteService;
    @Resource
    private ISysConfigService sysConfigService;
    @Resource
    private RuoYiConfig config;

    /**
     * 获取网站配置信息
     */
    @GetMapping
    @RateLimiter(limitType = LimitType.IP)
    public AjaxResult getConfig() {

        SysConfigWebsite website = sysWebsiteService.getById(1);
        if (StringUtils.isBlank(website.getName())) {
            website.setName(config.getName());
        }
        if (StringUtils.isBlank(website.getShortName())) {
            website.setShortName(config.getShortName());
        }
        if (StringUtils.isNotBlank(website.getSafeEntrance())) {
            website.setIsSafeEntrance('1');
        } else {
            website.setIsSafeEntrance('0');
        }
        String pageSize = sysConfigService.selectConfigByKey("sys.view.pageSize");
        if(StringUtils.isBlank(pageSize)) {
            pageSize = "10";
        }
        website.setPageSize(pageSize);

        try {
            getLoginUser();
        } catch (Exception e) { // 未登录不返回安全入口的值
            website.setSafeEntrance(null);
        }
        return AjaxResult.success(website);
    }

    /**
     * 修改配置
     */
    @Log(title = "网站配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysConfigWebsite sysWebsite) {
        if(StringUtils.isNotBlank(sysWebsite.getSafeEntrance()) && !StringUtils.isLetterDigit(sysWebsite.getSafeEntrance())) {
            throw new ServiceException("自定义后台登录入口设置有误，只能包含字母和数字");
        }
        sysWebsite.setId(1L);
        sysWebsite.setUpdateBy(getUsername());
        sysWebsite.setUpdateTime(DateUtils.getNowDate());
        return toAjax(sysWebsiteService.saveOrUpdate(sysWebsite));
    }
}
