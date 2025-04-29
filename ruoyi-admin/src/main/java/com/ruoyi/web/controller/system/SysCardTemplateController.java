package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.ChargeRule;
import com.ruoyi.common.enums.GenRule;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysCardTemplate;
import com.ruoyi.system.service.ISysCardTemplateService;
import com.ruoyi.utils.poi.ExcelUtil;
import com.ruoyi.web.vo.AddTemplateRapidInnerVo;
import com.ruoyi.web.vo.AddTemplateRapidVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * 卡密模板Controller
 *
 * @author zwgu
 * @date 2021-11-28
 */
@RestController
@RequestMapping("/system/cardTemplate")
public class SysCardTemplateController extends BaseController {

    static Long DAY = 86400L; // 86400秒为一天

    @Autowired
    private ISysCardTemplateService sysCardTemplateService;

    /**
     * 查询卡密模板列表
     */
    @PreAuthorize("@ss.hasPermi('system:cardTemplate:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysCardTemplate sysCardTemplate) {
        startPage();
        List<SysCardTemplate> list = sysCardTemplateService.selectSysCardTemplateList(sysCardTemplate);
        return getDataTable(list);
    }

    /**
     * 查询卡密模板列表
     */
    @PreAuthorize("@ss.hasPermi('system:cardTemplate:list')")
    @GetMapping("/listAll")
    public TableDataInfo listAll(SysCardTemplate sysCardTemplate) {
        List<SysCardTemplate> list = sysCardTemplateService.selectSysCardTemplateList(sysCardTemplate);
        return getDataTable(list);
    }

    /**
     * 导出卡密模板列表
     */
    @PreAuthorize("@ss.hasPermi('system:cardTemplate:export')")
    @Log(title = "卡密模板", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysCardTemplate sysCardTemplate)
    {
        List<SysCardTemplate> list = sysCardTemplateService.selectSysCardTemplateList(sysCardTemplate);
        ExcelUtil<SysCardTemplate> util = new ExcelUtil<SysCardTemplate>(SysCardTemplate.class);
        return util.exportExcel(list, "卡密模板数据");
    }

    /**
     * 获取卡密模板详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:cardTemplate:query')")
    @GetMapping(value = "/{templateId}")
    public AjaxResult getInfo(@PathVariable("templateId") Long templateId)
    {
        return AjaxResult.success(sysCardTemplateService.selectSysCardTemplateByTemplateId(templateId));
    }

    /**
     * 新增卡密模板
     */
    @PreAuthorize("@ss.hasPermi('system:cardTemplate:add')")
    @Log(title = "卡密模板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysCardTemplate sysCardTemplate) {
        sysCardTemplate.setCreateBy(getUsername());
        if (sysCardTemplate.getCardNoGenRule() == GenRule.REGEX && StringUtils.isBlank(sysCardTemplate.getCardNoRegex())) {
            throw new ServiceException("卡号正则表达式不能为空");
        }
        if (sysCardTemplate.getCardPassGenRule() == GenRule.REGEX && StringUtils.isBlank(sysCardTemplate.getCardPassRegex())) {
            throw new ServiceException("密码正则表达式不能为空");
        }
        if(sysCardTemplate.getQuota() < 0 || sysCardTemplate.getQuota() > 1000 * 365 * DAY) {
            throw new ServiceException("面值设置错误，面值需在0-1000年之间");
        }
        if(sysCardTemplate.getEffectiveDuration() < -1 || sysCardTemplate.getEffectiveDuration() > 1000 * 365 * DAY) {
            throw new ServiceException("有效期设置错误，有效期需在-1-1000年(-1代表永久)之间");
        }
        SysCardTemplate template = sysCardTemplateService.selectSysCardTemplateByAppIdAndTemplateName(sysCardTemplate.getAppId(), sysCardTemplate.getCardName());
        if(template != null) {
            throw new ServiceException("卡类不能重名，此软件下已存在名为[" + sysCardTemplate.getCardName() + "]的卡类");
        }
        return toAjax(sysCardTemplateService.insertSysCardTemplate(sysCardTemplate));
    }

    /**
     * 修改卡密模板
     */
    @PreAuthorize("@ss.hasPermi('system:cardTemplate:edit')")
    @Log(title = "卡密模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysCardTemplate sysCardTemplate) {
        sysCardTemplate.setUpdateBy(getUsername());
        if (sysCardTemplate.getCardNoGenRule() == GenRule.REGEX && StringUtils.isBlank(sysCardTemplate.getCardNoRegex())) {
            throw new ServiceException("卡号正则表达式不能为空");
        }
        if (sysCardTemplate.getCardPassGenRule() == GenRule.REGEX && StringUtils.isBlank(sysCardTemplate.getCardPassRegex())) {
            throw new ServiceException("密码正则表达式不能为空");
        }
        if(sysCardTemplate.getQuota() < 0 || sysCardTemplate.getQuota() > 1000 * 365 * DAY) {
            throw new ServiceException("面值设置错误，面值需在0-1000年之间");
        }
        if(sysCardTemplate.getEffectiveDuration() < -1 || sysCardTemplate.getEffectiveDuration() > 1000 * 365 * DAY) {
            throw new ServiceException("有效期设置错误，有效期需在-1-1000年(-1代表永久)之间");
        }
        SysCardTemplate template = sysCardTemplateService.selectSysCardTemplateByAppIdAndTemplateName(sysCardTemplate.getAppId(), sysCardTemplate.getCardName());
        if(template != null && !Objects.equals(template.getTemplateId(), sysCardTemplate.getTemplateId())) {
            throw new ServiceException("卡类不能重名，此软件下已存在名为[" + sysCardTemplate.getCardName() + "]的卡类");
        }
        return toAjax(sysCardTemplateService.updateSysCardTemplate(sysCardTemplate));
    }

    /**
     * 删除卡密模板
     */
    @PreAuthorize("@ss.hasPermi('system:cardTemplate:remove')")
    @Log(title = "卡密模板", businessType = BusinessType.DELETE)
    @DeleteMapping("/{templateIds}")
    public AjaxResult remove(@PathVariable Long[] templateIds)
    {
        return toAjax(sysCardTemplateService.deleteSysCardTemplateByTemplateIds(templateIds));
    }

    /**
     * 快速新增卡密模板
     */
    @PreAuthorize("@ss.hasPermi('system:cardTemplate:add')")
    @Log(title = "卡密模板", businessType = BusinessType.INSERT)
    @PostMapping("/addRapid")
    public AjaxResult addRapid(@RequestBody @Valid AddTemplateRapidVo vo) {
        List<AddTemplateRapidInnerVo> templateList = vo.getTemplateSelectionList();
        for (AddTemplateRapidInnerVo t : templateList) {
            SysCardTemplate template = sysCardTemplateService.selectSysCardTemplateByAppIdAndTemplateName(vo.getAppId(), t.getName());
            if(template != null) {
                continue;
            }
            SysCardTemplate sysCardTemplate = new SysCardTemplate();
            sysCardTemplate.setCreateBy(getUsername());
            sysCardTemplate.setAppId(vo.getAppId());
            sysCardTemplate.setCardNoLen(20);
            sysCardTemplate.setCardNoGenRule(GenRule.NUM_UPPERCASE_LOWERCASE);
            sysCardTemplate.setCardPassLen(20);
            sysCardTemplate.setCardPassGenRule(GenRule.NUM_UPPERCASE_LOWERCASE);
            sysCardTemplate.setChargeRule(ChargeRule.NONE);
            sysCardTemplate.setOnSale(UserConstants.YES);
            sysCardTemplate.setFirstStock(UserConstants.YES);
            sysCardTemplate.setEffectiveDuration(-1L);
            sysCardTemplate.setStatus(UserConstants.NORMAL);
            sysCardTemplate.setEnableAutoGen(UserConstants.NO);
            sysCardTemplate.setCardLoginLimitU(-2);
            sysCardTemplate.setCardLoginLimitM(-2);
            sysCardTemplate.setEnableUnbind(UserConstants.YES);
            sysCardTemplate.setCardName(t.getName());
            sysCardTemplate.setPrice(t.getPrice());
            sysCardTemplate.setRemark(t.getRemark());
            switch(t.getId()) {
                case 1:
                    sysCardTemplate.setQuota(DAY);
                    sysCardTemplate.setCardNoPrefix("TK");
                    break;
                case 2:
                    sysCardTemplate.setQuota(DAY*7);
                    sysCardTemplate.setCardNoPrefix("ZK");
                    break;
                case 3:
                    sysCardTemplate.setQuota(DAY*30);
                    sysCardTemplate.setCardNoPrefix("YK");
                    break;
                case 4:
                    sysCardTemplate.setQuota(DAY*90);
                    sysCardTemplate.setCardNoPrefix("JK");
                    break;
                case 5:
                    sysCardTemplate.setQuota(DAY*180);
                    sysCardTemplate.setCardNoPrefix("BN");
                    break;
                case 6:
                    sysCardTemplate.setQuota(DAY*365);
                    sysCardTemplate.setCardNoPrefix("NK");
                    break;
                case 7:
                    sysCardTemplate.setQuota(DAY*365*10);
                    sysCardTemplate.setCardNoPrefix("YJ");
                    break;
                default: continue;
            }
            sysCardTemplateService.insertSysCardTemplate(sysCardTemplate);
        }
        return AjaxResult.success();
    }
}