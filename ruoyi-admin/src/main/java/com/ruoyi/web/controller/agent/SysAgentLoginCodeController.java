package com.ruoyi.web.controller.agent;

import com.ruoyi.agent.domain.SysAgent;
import com.ruoyi.agent.domain.SysAgentItem;
import com.ruoyi.agent.service.ISysAgentItemService;
import com.ruoyi.agent.service.ISysAgentUserService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BalanceChangeType;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.TemplateType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.service.PermissionService;
import com.ruoyi.system.domain.SysBalanceLog;
import com.ruoyi.system.domain.SysLoginCode;
import com.ruoyi.system.domain.SysLoginCodeTemplate;
import com.ruoyi.system.service.ISysBalanceLogService;
import com.ruoyi.system.service.ISysLoginCodeService;
import com.ruoyi.system.service.ISysLoginCodeTemplateService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 单码Controller
 *
 * @author zwgu
 * @date 2021-12-03
 */
@RestController
@RequestMapping("/agent/agentLoginCode")
public class SysAgentLoginCodeController extends BaseController {
    @Autowired
    private ISysLoginCodeService sysLoginCodeService;
    @Resource
    private ISysAgentItemService sysAgentItemService;
    @Resource
    private ISysAgentUserService sysAgentService;
    @Resource
    private ISysLoginCodeTemplateService sysLoginCodeTemplateService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private ISysUserService userService;
    @Resource
    private ISysBalanceLogService sysBalanceLogService;

    /**
     * 查询单码列表
     */
    @PreAuthorize("@ss.hasPermi('agent:agentLoginCode:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysLoginCode sysLoginCode) {
        startPage();
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            sysLoginCode.setCreateBy(getUsername());
        }
        List<SysLoginCode> list = sysLoginCodeService.selectSysLoginCodeList(sysLoginCode);
        return getDataTable(list);
    }

    /**
     * 导出单码列表
     */
    @PreAuthorize("@ss.hasPermi('agent:agentLoginCode:export')")
    @Log(title = "单码", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysLoginCode sysLoginCode) {
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            sysLoginCode.setCreateBy(getUsername());
        }
        List<SysLoginCode> list = sysLoginCodeService.selectSysLoginCodeList(sysLoginCode);
        ExcelUtil<SysLoginCode> util = new ExcelUtil<SysLoginCode>(SysLoginCode.class);
        return util.exportExcel(list, "单码数据");
    }

    /**
     * 获取单码详细信息
     */
    @PreAuthorize("@ss.hasPermi('agent:agentLoginCode:query')")
    @GetMapping(value = "/{cardId}")
    public AjaxResult getInfo(@PathVariable("cardId") Long cardId) {
        return AjaxResult.success(sysLoginCodeService.selectSysLoginCodeByCardId(cardId));
    }

    /**
     * 新增单码
     */
    @PreAuthorize("@ss.hasPermi('agent:agentLoginCode:add')")
    @Log(title = "单码", businessType = BusinessType.INSERT)
    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult add(@RequestBody SysLoginCode sysLoginCode) {
        if (sysLoginCode.getTemplateId() == null) {
            return AjaxResult.error("未指定卡类，批量制卡失败");
        }
        if (sysLoginCode.getGenQuantity() == null || sysLoginCode.getGenQuantity() < 0) {
            return AjaxResult.error("制卡数量有误，批量制卡失败");
        }
        SysLoginCodeTemplate sysLoginCodeTemplate = sysLoginCodeTemplateService.selectSysLoginCodeTemplateByTemplateId(sysLoginCode.getTemplateId());
        if (sysLoginCodeTemplate == null) {
            return AjaxResult.error("卡类不存在，批量制卡失败");
        }
        // 判断是否有代理权限
        SysAgent agent = sysAgentService.selectSysAgentByUserId(getUserId());
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            if (agent != null) {
                if (agent.getExpireTime() != null && !agent.getExpireTime().after(DateUtils.getNowDate())) {
                    throw new ServiceException("您代理该卡的权限已过期，请联系您的上级代理", 400);
                }
                // 判断是否有代理该卡的权限
                SysAgentItem agentItem = null;
                SysAgentItem agentItemS = new SysAgentItem();
                agentItemS.setAgentId(agent.getAgentId());
                agentItemS.setTemplateType(TemplateType.LOGIN_CODE);
                agentItemS.setTemplateId(sysLoginCode.getTemplateId());
                List<SysAgentItem> agentItems = sysAgentItemService.selectSysAgentItemList(agentItemS);
                if (agentItems.size() == 1) {
                    agentItem = agentItems.get(0);
                    if (agentItem.getExpireTime() != null && !agentItem.getExpireTime().after(DateUtils.getNowDate())) {
                        throw new ServiceException("您代理该卡的权限已过期，请联系您的上级代理", 400);
                    }
                } else {
                    throw new ServiceException("代理信息有误", 400);
                }
                // 计算金额
                BigDecimal unitPrice = agentItem.getAgentPrice();
                // 获取代理价格
                if (unitPrice == null) {
                    throw new ServiceException("商品未设定价格", 400);
                }
                BigDecimal totalFee = unitPrice.multiply(BigDecimal.valueOf(sysLoginCode.getGenQuantity()));
                // 获取用户余额
                SysUser user = userService.selectUserById(getUserId());
                if (user.getAvailablePayBalance().compareTo(totalFee) >= 0) {
                    // 记录金额变动日志
                    SysBalanceLog balanceLog = new SysBalanceLog();
                    balanceLog.setUserId(getUserId());
                    balanceLog.setSourceUserId(null);
                    balanceLog.setChangeAvailablePayAmount(totalFee.multiply(BigDecimal.valueOf(-1)));
                    balanceLog.setChangeFreezePayAmount(BigDecimal.ZERO);
                    balanceLog.setChangeType(BalanceChangeType.CONSUME);
                    balanceLog.setChangeAvailableFreeAmount(BigDecimal.ZERO);
                    balanceLog.setChangeFreezeFreeAmount(BigDecimal.ZERO);
                    balanceLog.setFreezeFreeAfter(user.getFreezeFreeBalance());
                    balanceLog.setFreezeFreeBefore(user.getFreezeFreeBalance());
                    balanceLog.setAvailableFreeAfter(user.getAvailableFreeBalance());
                    balanceLog.setAvailableFreeBefore(user.getAvailableFreeBalance());
                    balanceLog.setFreezePayAfter(user.getFreezePayBalance());
                    balanceLog.setFreezePayBefore(user.getFreezePayBalance());
                    balanceLog.setAvailablePayAfter(user.getAvailablePayBalance().subtract(totalFee));
                    balanceLog.setAvailablePayBefore(user.getAvailablePayBalance());
                    balanceLog.setChangeDesc("批量制卡：[" + sysLoginCodeTemplate.getApp().getAppName() + "]" + sysLoginCodeTemplate.getCardName() + "，" + sysLoginCode.getGenQuantity() + "张，单价" + unitPrice);
                    balanceLog.setSaleOrderId(null);
                    balanceLog.setWithdrawCashId(null);
                    balanceLog.setCreateBy(getUsername());
                    sysBalanceLogService.insertSysBalanceLog(balanceLog);
                    // 扣款
                    user.setAvailablePayBalance(user.getAvailablePayBalance().subtract(totalFee));
                    userService.updateUser(user);
                } else {
                    throw new ServiceException("您的余额不足", 400);
                }
            } else {
                throw new ServiceException("代理信息缺失", 400);
            }
        }
        return toAjax(sysLoginCodeTemplateService.genSysLoginCodeBatch(sysLoginCodeTemplate, sysLoginCode.getGenQuantity(), sysLoginCode.getOnSale(), UserConstants.YES, sysLoginCode.getRemark()).size());
    }

    /**
     * 修改单码
     */
    @PreAuthorize("@ss.hasPermi('agent:agentLoginCode:edit')")
    @Log(title = "单码", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysLoginCode sysLoginCode) {
        sysLoginCode.setUpdateBy(getUsername());
        return toAjax(sysLoginCodeService.updateSysLoginCode(sysLoginCode));
    }

    /**
     * 删除单码
     */
    @PreAuthorize("@ss.hasPermi('agent:agentLoginCode:remove')")
    @Log(title = "单码", businessType = BusinessType.DELETE)
    @DeleteMapping("/{cardIds}")
    public AjaxResult remove(@PathVariable Long[] cardIds) {
        return toAjax(sysLoginCodeService.deleteSysLoginCodeByCardIds(cardIds));
    }

    /**
     * 查询单码类别列表
     */
    @PreAuthorize("@ss.hasRole('agent')")
    @GetMapping("/loginCodeTemplate/listAll")
    public TableDataInfo listAll(SysLoginCodeTemplate sysLoginCodeTemplate) {
        List<SysLoginCodeTemplate> list = new ArrayList<>();
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            Set<Long> appIds = new HashSet<>();
            // 获取我的代理ID
            SysAgent agent = sysAgentService.selectSysAgentByUserId(getLoginUser().getUserId());
            // 获取代理的所有卡类
            List<SysAgentItem> agentItems = sysAgentItemService.selectSysAgentItemByAgentId(agent.getAgentId());
            // 获取所有卡类信息
            for (SysAgentItem item : agentItems) {
                if (item.getTemplateType() == TemplateType.LOGIN_CODE) {
                    SysLoginCodeTemplate template = sysLoginCodeTemplateService.selectSysLoginCodeTemplateByTemplateId(item.getTemplateId());
                    if (!appIds.contains(template.getTemplateId())) {
                        template.setAgentPrice(item.getAgentPrice());
                        list.add(template);
                        appIds.add(template.getTemplateId());
                    }
                }
            }
        } else {
            list.addAll(sysLoginCodeTemplateService.selectSysLoginCodeTemplateList(sysLoginCodeTemplate));
        }
        return getDataTable(list);
    }
}
