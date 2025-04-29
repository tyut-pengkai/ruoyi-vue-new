package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.GenRule;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysLoginCodeTemplate;
import com.ruoyi.system.service.ISysLoginCodeTemplateService;
import com.ruoyi.utils.poi.ExcelUtil;
import com.ruoyi.web.vo.AddTemplateRapidInnerVo;
import com.ruoyi.web.vo.AddTemplateRapidVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * 单码类别Controller
 *
 * @author zwgu
 * @date 2022-01-06
 */
@RestController
@RequestMapping("/system/loginCodeTemplate")
public class SysLoginCodeTemplateController extends BaseController {

    static Long DAY = 86400L; // 86400秒为一天

    @Autowired
    private ISysLoginCodeTemplateService sysLoginCodeTemplateService;

    /**
     * 查询单码类别列表
     */
    @PreAuthorize("@ss.hasPermi('system:loginCodeTemplate:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysLoginCodeTemplate sysLoginCodeTemplate) {
        startPage();
        List<SysLoginCodeTemplate> list = sysLoginCodeTemplateService.selectSysLoginCodeTemplateList(sysLoginCodeTemplate);
        return getDataTable(list);
    }

    /**
     * 查询单码类别列表
     */
    @PreAuthorize("@ss.hasPermi('system:loginCodeTemplate:list')")
    @GetMapping("/listAll")
    public TableDataInfo listAll(SysLoginCodeTemplate sysLoginCodeTemplate) {
        List<SysLoginCodeTemplate> list = sysLoginCodeTemplateService.selectSysLoginCodeTemplateList(sysLoginCodeTemplate);
        return getDataTable(list);
    }

    /**
     * 导出单码类别列表
     */
    @PreAuthorize("@ss.hasPermi('system:loginCodeTemplate:export')")
    @Log(title = "单码类别", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysLoginCodeTemplate sysLoginCodeTemplate) {
        List<SysLoginCodeTemplate> list = sysLoginCodeTemplateService.selectSysLoginCodeTemplateList(sysLoginCodeTemplate);
        ExcelUtil<SysLoginCodeTemplate> util = new ExcelUtil<SysLoginCodeTemplate>(SysLoginCodeTemplate.class);
        util.exportExcel(response, list, "单码类别数据");
    }

    /**
     * 获取单码类别详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:loginCodeTemplate:query')")
    @GetMapping(value = "/{templateId}")
    public AjaxResult getInfo(@PathVariable("templateId") Long templateId) {
        return AjaxResult.success(sysLoginCodeTemplateService.selectSysLoginCodeTemplateByTemplateId(templateId));
    }

    /**
     * 新增单码类别
     */
    @PreAuthorize("@ss.hasPermi('system:loginCodeTemplate:add')")
    @Log(title = "单码类别", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysLoginCodeTemplate sysLoginCodeTemplate) {
        sysLoginCodeTemplate.setCreateBy(getUsername());
        if (sysLoginCodeTemplate.getCardNoGenRule() == GenRule.REGEX && StringUtils.isBlank(sysLoginCodeTemplate.getCardNoRegex())) {
            throw new ServiceException("正则表达式不能为空");
        }
        if(sysLoginCodeTemplate.getQuota() < 0 || sysLoginCodeTemplate.getQuota() > 1000 * 365 * DAY) {
            throw new ServiceException("面值设置错误，面值需在0-1000年之间");
        }
        if(sysLoginCodeTemplate.getEffectiveDuration() < -1 || sysLoginCodeTemplate.getEffectiveDuration() > 1000 * 365 * DAY) {
            throw new ServiceException("有效期设置错误，有效期需在-1-1000年(-1代表永久)之间");
        }
        SysLoginCodeTemplate template = sysLoginCodeTemplateService.selectSysLoginCodeTemplateByAppIdAndTemplateName(sysLoginCodeTemplate.getAppId(), sysLoginCodeTemplate.getCardName());
        if(template != null) {
            throw new ServiceException("卡类不能重名，此软件下已存在名为[" + sysLoginCodeTemplate.getCardName() + "]的卡类");
        }
        return toAjax(sysLoginCodeTemplateService.insertSysLoginCodeTemplate(sysLoginCodeTemplate));
    }

    /**
     * 修改单码类别
     */
    @PreAuthorize("@ss.hasPermi('system:loginCodeTemplate:edit')")
    @Log(title = "单码类别", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysLoginCodeTemplate sysLoginCodeTemplate) {
        sysLoginCodeTemplate.setUpdateBy(getUsername());
        if (sysLoginCodeTemplate.getCardNoGenRule() == GenRule.REGEX && StringUtils.isBlank(sysLoginCodeTemplate.getCardNoRegex())) {
            throw new ServiceException("正则表达式不能为空");
        }
        if(sysLoginCodeTemplate.getQuota() < 0 || sysLoginCodeTemplate.getQuota() > 1000 * 365 * DAY) {
            throw new ServiceException("面值设置错误，面值需在0-1000年之间");
        }
        if(sysLoginCodeTemplate.getEffectiveDuration() < -1 || sysLoginCodeTemplate.getEffectiveDuration() > 1000 * 365 * DAY) {
            throw new ServiceException("有效期设置错误，有效期需在-1-1000年(-1代表永久)之间");
        }
        SysLoginCodeTemplate template = sysLoginCodeTemplateService.selectSysLoginCodeTemplateByAppIdAndTemplateName(sysLoginCodeTemplate.getAppId(), sysLoginCodeTemplate.getCardName());
        if(template != null && !Objects.equals(template.getTemplateId(), sysLoginCodeTemplate.getTemplateId())) {
            throw new ServiceException("卡类不能重名，此软件下已存在名为[" + sysLoginCodeTemplate.getCardName() + "]的卡类");
        }
        return toAjax(sysLoginCodeTemplateService.updateSysLoginCodeTemplate(sysLoginCodeTemplate));
    }

    /**
     * 删除单码类别
     */
    @PreAuthorize("@ss.hasPermi('system:loginCodeTemplate:remove')")
    @Log(title = "单码类别", businessType = BusinessType.DELETE)
    @DeleteMapping("/{templateIds}")
    public AjaxResult remove(@PathVariable Long[] templateIds) {
        return toAjax(sysLoginCodeTemplateService.deleteSysLoginCodeTemplateByTemplateIds(templateIds));
    }

    /**
     * 快速新增卡密模板
     */
    @PreAuthorize("@ss.hasPermi('system:loginCodeTemplate:add')")
    @Log(title = "单码类别", businessType = BusinessType.INSERT)
    @PostMapping("/addRapid")
    public AjaxResult addRapid(@RequestBody @Valid AddTemplateRapidVo vo) {
        List<AddTemplateRapidInnerVo> templateList = vo.getTemplateSelectionList();
        for (AddTemplateRapidInnerVo t : templateList) {
            SysLoginCodeTemplate template = sysLoginCodeTemplateService.selectSysLoginCodeTemplateByAppIdAndTemplateName(vo.getAppId(), t.getName());
            if(template != null) {
                continue;
            }
            SysLoginCodeTemplate sysLoginCodeTemplate = new SysLoginCodeTemplate();
            sysLoginCodeTemplate.setCreateBy(getUsername());
            sysLoginCodeTemplate.setAppId(vo.getAppId());
            sysLoginCodeTemplate.setCardNoLen(20);
            sysLoginCodeTemplate.setCardNoGenRule(GenRule.NUM_UPPERCASE_LOWERCASE);
//            sysLoginCodeTemplate.setCardPassLen(20);
//            sysLoginCodeTemplate.setCardPassGenRule(GenRule.NUM_UPPERCASE_LOWERCASE);
//            sysLoginCodeTemplate.setChargeRule(ChargeRule.NONE);
            sysLoginCodeTemplate.setOnSale(UserConstants.YES);
            sysLoginCodeTemplate.setFirstStock(UserConstants.YES);
            sysLoginCodeTemplate.setEffectiveDuration(-1L);
            sysLoginCodeTemplate.setStatus(UserConstants.NORMAL);
            sysLoginCodeTemplate.setEnableAutoGen(UserConstants.NO);
            sysLoginCodeTemplate.setCardLoginLimitU(-2);
            sysLoginCodeTemplate.setCardLoginLimitM(-2);
            sysLoginCodeTemplate.setEnableUnbind(UserConstants.YES);
            sysLoginCodeTemplate.setCardName(t.getName());
            sysLoginCodeTemplate.setPrice(t.getPrice());
            sysLoginCodeTemplate.setRemark(t.getRemark());
            switch(t.getId()) {
                case 1:
                    sysLoginCodeTemplate.setQuota(DAY);
                    sysLoginCodeTemplate.setCardNoPrefix("TK");
                    break;
                case 2:
                    sysLoginCodeTemplate.setQuota(DAY*7);
                    sysLoginCodeTemplate.setCardNoPrefix("ZK");
                    break;
                case 3:
                    sysLoginCodeTemplate.setQuota(DAY*30);
                    sysLoginCodeTemplate.setCardNoPrefix("YK");
                    break;
                case 4:
                    sysLoginCodeTemplate.setQuota(DAY*90);
                    sysLoginCodeTemplate.setCardNoPrefix("JK");
                    break;
                case 5:
                    sysLoginCodeTemplate.setQuota(DAY*180);
                    sysLoginCodeTemplate.setCardNoPrefix("BN");
                    break;
                case 6:
                    sysLoginCodeTemplate.setQuota(DAY*365);
                    sysLoginCodeTemplate.setCardNoPrefix("NK");
                    break;
                case 7:
                    sysLoginCodeTemplate.setQuota(DAY*365*10);
                    sysLoginCodeTemplate.setCardNoPrefix("YJ");
                    break;
                default: continue;
            }
            sysLoginCodeTemplateService.insertSysLoginCodeTemplate(sysLoginCodeTemplate);
        }
        return AjaxResult.success();
    }
}
