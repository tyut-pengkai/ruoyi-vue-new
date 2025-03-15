package com.ruoyi.web.controller.system;

import com.ruoyi.agent.domain.SysAgent;
import com.ruoyi.agent.service.ISysAgentUserService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BillType;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.ChargeType;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.PermissionService;
import com.ruoyi.system.domain.SysLoginCode;
import com.ruoyi.system.domain.SysLoginCodeTemplate;
import com.ruoyi.system.domain.vo.BatchNoVo;
import com.ruoyi.system.domain.vo.BatchReplaceVo;
import com.ruoyi.system.service.ISysAppService;
import com.ruoyi.system.service.ISysAppUserService;
import com.ruoyi.system.service.ISysLoginCodeService;
import com.ruoyi.system.service.ISysLoginCodeTemplateService;
import com.ruoyi.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 单码Controller
 *
 * @author zwgu
 * @date 2021-12-03
 */
@RestController
@RequestMapping("/system/loginCode")
public class SysLoginCodeController extends BaseController {
    @Autowired
    private ISysLoginCodeService sysLoginCodeService;
    @Resource
    private ISysLoginCodeTemplateService sysLoginCodeTemplateService;
    @Resource
    private ISysAppService sysAppService;
    @Resource
    private ISysAppUserService sysAppUserService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private ISysAgentUserService sysAgentService;

    /**
     * 查询单码列表
     */
    @PreAuthorize("@ss.hasPermi('system:loginCode:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysLoginCode sysLoginCode) {
        startPage();
        List<SysLoginCode> list = sysLoginCodeService.selectSysLoginCodeList(sysLoginCode);
        return getDataTable(list);
    }

    /**
     * 导出单码列表
     */
    @PreAuthorize("@ss.hasPermi('system:loginCode:export')")
    @Log(title = "单码", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysLoginCode sysLoginCode) {
        List<SysLoginCode> list = sysLoginCodeService.selectSysLoginCodeList(sysLoginCode);
        ExcelUtil<SysLoginCode> util = new ExcelUtil<SysLoginCode>(SysLoginCode.class);
        return util.exportExcel(list, "单码数据");
    }

    @Log(title = "单码", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:loginCode:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysLoginCode> util = new ExcelUtil<>(SysLoginCode.class);
        List<SysLoginCode> cardList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = sysLoginCodeService.importLoginCode(cardList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<SysLoginCode> util = new ExcelUtil<>(SysLoginCode.class);
        util.importTemplateExcel(response, "单码数据");
    }

    /**
     * 获取单码详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:loginCode:query')")
    @GetMapping(value = "/{cardId}")
    public AjaxResult getInfo(@PathVariable("cardId") Long cardId) {
        return AjaxResult.success(sysLoginCodeService.selectSysLoginCodeByCardId(cardId));
    }

    /**
     * 新增单码
     */
    @PreAuthorize("@ss.hasPermi('system:loginCode:add')")
    @Log(title = "单码", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysLoginCode sysLoginCode) {
        if (sysLoginCode.getTemplateId() == null) {
            sysLoginCode.setCreateBy(getUsername());
            sysLoginCode.setIsAgent(UserConstants.NO);
            return toAjax(sysLoginCodeService.insertSysLoginCode(sysLoginCode));
        } else {
            if (sysLoginCode.getGenQuantity() == null || sysLoginCode.getGenQuantity() < 0) {
                return AjaxResult.error("制卡数量有误，批量制卡失败");
            }
            if (sysLoginCode.getGenQuantity() > 10000) {
                return AjaxResult.error("制卡数量过多，一次性最多制卡10000张");
            }
            SysLoginCodeTemplate sysLoginCodeTemplate = sysLoginCodeTemplateService.selectSysLoginCodeTemplateByTemplateId(sysLoginCode.getTemplateId());
            if (sysLoginCodeTemplate == null) {
                return AjaxResult.error("卡类不存在，批量制卡失败");
            }
//            new Thread(() -> sysLoginCodeTemplateService.genSysLoginCodeBatch(sysLoginCodeTemplate, sysLoginCode.getGenQuantity(), sysLoginCode.getOnSale(), UserConstants.NO, sysLoginCode.getRemark())).start();
            List<SysLoginCode> sysLoginCodes = sysLoginCodeTemplateService.genSysLoginCodeBatch(sysLoginCodeTemplate, sysLoginCode.getGenQuantity(), sysLoginCode.getOnSale(), UserConstants.NO, sysLoginCode.getRemark());
            List<Map<String, String>> resultList = new ArrayList<>();
            for (SysLoginCode item : sysLoginCodes) {
                Map<String, String> map = new HashMap<>();
                map.put("cardNo", item.getCardNo());
                map.put("expireTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, item.getExpireTime()));
                resultList.add(map);
            }
            return AjaxResult.success("生成完毕", resultList).put("cardName", sysLoginCodeTemplate.getCardName());
        }
    }

    /**
     * 修改单码
     */
    @PreAuthorize("@ss.hasPermi('system:loginCode:edit')")
    @Log(title = "单码", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysLoginCode sysLoginCode) {
        sysLoginCode.setUpdateBy(getUsername());
        return toAjax(sysLoginCodeService.updateSysLoginCode(sysLoginCode));
    }

    /**
     * 删除单码
     */
    @PreAuthorize("@ss.hasPermi('system:loginCode:remove')")
    @Log(title = "单码", businessType = BusinessType.DELETE)
    @DeleteMapping("/{cardIds}")
    public AjaxResult remove(@PathVariable Long[] cardIds) {
        return toAjax(sysLoginCodeService.deleteSysLoginCodeByCardIds(cardIds));
    }

    /**
     * 获取批次号列表
     *
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:loginCode:list')")
    @GetMapping("/selectBatchNoList")
    public AjaxResult selectBatchNoList() {
        List<BatchNoVo> batchNoList = sysLoginCodeService.selectBatchNoList(null);
        return AjaxResult.success(batchNoList);
    }

    /**
     * 批量换卡
     */
    @PreAuthorize("@ss.hasAnyPermi('system:loginCode:replace,agent:agentLoginCode:replace')")
    @Log(title = "单码", businessType = BusinessType.REPLACE)
    @PostMapping("/batchReplace")
    public AjaxResult batchReplace(@RequestBody BatchReplaceVo vo) {
        String[] split = vo.getContent().split("\n");
        Set<String> set = new HashSet<>(Arrays.asList(split));
        if (set.isEmpty()) {
            return AjaxResult.success("请输入要操作的内容，每行一个");
        }
        List<String> list = set.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        LinkedHashMap<String, List<String>> result = new LinkedHashMap<>();
        String a = "成功替换的单码";
        String b = "非所选软件的单码";
        String c = "不符合换卡条件的单码";
        String d = "不存在的单码";
        String e = "生成新单码异常";
        String f = "非您代理的单码";
        result.put(a, new ArrayList<>());
        result.put(b, new ArrayList<>());
        result.put(c, new ArrayList<>());
        result.put(d, new ArrayList<>());
        result.put(e, new ArrayList<>());
        result.put(f, new ArrayList<>());
        SysApp app = null;
        if(vo.getAppId() != 0) {
            app = sysAppService.selectSysAppByAppId(vo.getAppId());
            if (app == null) {
                return AjaxResult.success("软件不存在");
            }
        }
        String batchNo = DateUtils.dateTimeNow();
        for (String item : list) {
            SysLoginCode loginCode = sysLoginCodeService.selectSysLoginCodeByCardNo(item);
            if (loginCode == null) {
                result.get(d).add(item + "【单码不存在】");
            } else {
                if(!permissionService.hasAnyRoles("sadmin,admin")) {
                    // 检查是否自己代理的卡
                    SysAgent agent = sysAgentService.selectSysAgentByUserId(getUserId());
                    if(!Objects.equals(loginCode.getAgentId(), agent.getAgentId())) {
                        result.get(f).add(item + "【非您代理的卡密】");
                        continue;
                    }
                }
                if (app != null && !Objects.equals(loginCode.getAppId(), app.getAppId())) {
                    result.get(b).add(item + "【所属软件：" + app.getAppName() + "】【" + loginCode.getCardName() + "】");
                } else {
                    app = loginCode.getApp();
                    // 检查换卡开关
                    Long templateId = loginCode.getTemplateId();
                    SysLoginCodeTemplate template = sysLoginCodeTemplateService.selectSysLoginCodeTemplateByTemplateId(templateId);
                    if(templateId == null) {
                        result.get(c).add(item + "【此单码类别不存在】");
                    } else if(!template.getEnableReplace().equals(UserConstants.YES)) {
                        result.get(c).add(item + "【此单码类别未开启换卡】");
                    } else {
                        // 换卡条件：未过期， 未冻结
                        if(UserStatus.DELETED.getCode().equals(loginCode.getDelFlag())) {
                            result.get(c).add(item + "【单码已被删除】");
                        } else if(UserStatus.DISABLE.getCode().equals(loginCode.getStatus())) {
                            result.get(c).add(item + "【单码已停用】");
                        } else if(loginCode.getExpireTime().before(DateUtils.getNowDate())) {
                            result.get(c).add(item + "【单码已过期】");
                        } else {
                            // 检查阈值
                            if(loginCode.getIsCharged().equals(UserConstants.YES)) {
                                SysAppUser appUser = null;
                                if(loginCode.getChargeTo() != null) {
                                    appUser = sysAppUserService.selectSysAppUserByAppUserId(loginCode.getChargeTo());
                                } else {
                                    appUser = sysAppUserService.selectSysAppUserByAppIdAndLoginCode(loginCode.getAppId(), item);
                                }
                                if(template.getReplaceThreshold() == -1) {
                                    result.get(c).add(item + "【单码已使用】");
                                } else if(loginCode.getChargeType() == ChargeType.CHARGE || appUser == null) {
                                    result.get(c).add(item + "【单码已使用且已经以卡充卡到软件用户】");
                                } else {
                                    if(app.getBillType() == BillType.TIME) {
                                        long second = DateUtils.differentMillisecond(DateUtils.getNowDate(), appUser.getExpireTime()) / 1000;
                                        if(second < template.getReplaceThreshold() || second <= 0) {
                                            result.get(c).add(item + "【单码已使用且不满足可换卡条件或单码已无剩余时间】");
                                        } else {
                                            // 符合换卡条件
                                            doReplace(vo, item, template, loginCode, batchNo, result, a, e);
                                        }
                                    }  else if(app.getBillType() == BillType.POINT) {
                                        if(appUser.getPoint().compareTo(BigDecimal.valueOf(template.getReplaceThreshold())) < 0 || appUser.getPoint().compareTo(BigDecimal.ZERO) < 0) {
                                            result.get(c).add(item + "【单码已使用且不满足可换卡条件或单码已无剩余点数】");
                                        } else {
                                            // 符合换卡条件
                                            doReplace(vo, item, template, loginCode, batchNo, result, a, e);
                                        }
                                    } else {
                                        throw new ServiceException("软件计费方式设置有误");
                                    }
                                    SysAppUser updateUser = new SysAppUser();
                                    updateUser.setAppUserId(appUser.getAppUserId());
                                    updateUser.setStatus(UserStatus.DISABLE.getCode());
                                    sysAppUserService.updateSysAppUser(updateUser);
                                }
                            } else {
                                // 符合换卡条件
                                doReplace(vo, item, template, loginCode, batchNo, result, a, e);
                            }
                        }
                    }
                }
            }
        }
        StringBuilder resultStr = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : result.entrySet()) {
            resultStr.append("--------------------------【")
                    .append(entry.getKey()).append("】").append(entry.getValue().size())
                    .append("个--------------------------").append("\n\n")
                    .append(String.join("\n", entry.getValue()))
                    .append("\n\n");
        }
        return AjaxResult.success(resultStr.toString());
    }

    private void doReplace(BatchReplaceVo vo, String item, SysLoginCodeTemplate template, SysLoginCode loginCode, String batchNo, LinkedHashMap<String, List<String>> result, String a, String e) {
        try {
            SysLoginCode newLoginCode = sysLoginCodeTemplateService.genSysLoginCodeReplace(template, loginCode, vo, batchNo);
            SysLoginCode update = new SysLoginCode();
            update.setCardId(loginCode.getCardId());
            String remark = "此卡已被【" + newLoginCode.getCardNo() + "】替换";
            update.setRemark(StringUtils.isNotBlank(loginCode.getRemark()) ? loginCode.getRemark() + "\n" + remark : remark);
            update.setDelFlag("2"); // 删除
            sysLoginCodeService.updateSysLoginCode(update);
            result.get(a).add(item + " => " + newLoginCode.getCardNo());
        } catch (Exception ex) {
            result.get(e).add(item + "【" + ex.getMessage() + "】");
        }
    }

}
