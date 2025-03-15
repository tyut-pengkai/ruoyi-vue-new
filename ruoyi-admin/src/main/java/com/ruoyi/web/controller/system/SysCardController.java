package com.ruoyi.web.controller.system;

import com.ruoyi.agent.domain.SysAgent;
import com.ruoyi.agent.service.ISysAgentUserService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.PermissionService;
import com.ruoyi.system.domain.SysCard;
import com.ruoyi.system.domain.SysCardTemplate;
import com.ruoyi.system.domain.vo.BatchNoVo;
import com.ruoyi.system.domain.vo.BatchReplaceVo;
import com.ruoyi.system.service.ISysAppService;
import com.ruoyi.system.service.ISysAppUserService;
import com.ruoyi.system.service.ISysCardService;
import com.ruoyi.system.service.ISysCardTemplateService;
import com.ruoyi.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 卡密Controller
 *
 * @author zwgu
 * @date 2021-12-03
 */
@RestController
@RequestMapping("/system/card")
public class SysCardController extends BaseController {
    @Autowired
    private ISysCardService sysCardService;
    @Resource
    private ISysCardTemplateService sysCardTemplateService;
    @Resource
    private ISysAppService sysAppService;
    @Resource
    private ISysAppUserService sysAppUserService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private ISysAgentUserService sysAgentService;

    /**
     * 查询卡密列表
     */
    @PreAuthorize("@ss.hasPermi('system:card:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysCard sysCard) {
        startPage();
        List<SysCard> list = sysCardService.selectSysCardList(sysCard);
        return getDataTable(list);
    }

    /**
     * 导出卡密列表
     */
    @PreAuthorize("@ss.hasPermi('system:card:export')")
    @Log(title = "卡密", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysCard sysCard) {
        List<SysCard> list = sysCardService.selectSysCardList(sysCard);
        ExcelUtil<SysCard> util = new ExcelUtil<SysCard>(SysCard.class);
        return util.exportExcel(list, "卡密数据");
    }

    @Log(title = "卡密", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:card:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysCard> util = new ExcelUtil<>(SysCard.class);
        List<SysCard> cardList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = sysCardService.importCard(cardList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<SysCard> util = new ExcelUtil<>(SysCard.class);
        util.importTemplateExcel(response, "卡密数据");
    }

    /**
     * 获取卡密详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:card:query')")
    @GetMapping(value = "/{cardId}")
    public AjaxResult getInfo(@PathVariable("cardId") Long cardId) {
        return AjaxResult.success(sysCardService.selectSysCardByCardId(cardId));
    }

    /**
     * 新增卡密
     */
    @PreAuthorize("@ss.hasPermi('system:card:add')")
    @Log(title = "卡密", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysCard sysCard) {
        if (sysCard.getTemplateId() == null) {
            sysCard.setCreateBy(getUsername());
            sysCard.setIsAgent(UserConstants.NO);
            return toAjax(sysCardService.insertSysCard(sysCard));
        } else {
            if (sysCard.getGenQuantity() == null || sysCard.getGenQuantity() < 0) {
                return AjaxResult.error("制卡数量有误，批量制卡失败");
            }
            if (sysCard.getGenQuantity() > 10000) {
                return AjaxResult.error("制卡数量过多，一次性最多制卡10000张");
            }
            SysCardTemplate sysCardTemplate = sysCardTemplateService.selectSysCardTemplateByTemplateId(sysCard.getTemplateId());
            if (sysCardTemplate == null) {
                return AjaxResult.error("卡类不存在，批量制卡失败");
            }
//            new Thread(() -> sysCardTemplateService.genSysCardBatch(sysCardTemplate, sysCard.getGenQuantity(), sysCard.getOnSale(), UserConstants.NO, sysCard.getRemark())).start();
            List<SysCard> sysCardList = sysCardTemplateService.genSysCardBatch(sysCardTemplate, sysCard.getGenQuantity(), sysCard.getOnSale(), UserConstants.NO, sysCard.getRemark());
            List<Map<String, String>> resultList = new ArrayList<>();
            for (SysCard item : sysCardList) {
                Map<String, String> map = new HashMap<>();
                map.put("cardNo", item.getCardNo());
                map.put("cardPass", item.getCardPass());
                map.put("expireTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, item.getExpireTime()));
                resultList.add(map);
            }
            return AjaxResult.success("生成完毕", resultList).put("cardName", sysCardTemplate.getCardName());
        }
    }

    /**
     * 修改卡密
     */
    @PreAuthorize("@ss.hasPermi('system:card:edit')")
    @Log(title = "卡密", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysCard sysCard) {
        sysCard.setUpdateBy(getUsername());
        return toAjax(sysCardService.updateSysCard(sysCard));
    }

    /**
     * 删除卡密
     */
    @PreAuthorize("@ss.hasPermi('system:card:remove')")
    @Log(title = "卡密", businessType = BusinessType.DELETE)
    @DeleteMapping("/{cardIds}")
    public AjaxResult remove(@PathVariable Long[] cardIds) {
        return toAjax(sysCardService.deleteSysCardByCardIds(cardIds));
    }

    /**
     * 获取批次号列表
     *
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:card:list')")
    @GetMapping("/selectBatchNoList")
    public AjaxResult selectBatchNoList() {
        List<BatchNoVo> batchNoList = sysCardService.selectBatchNoList(null);
        return AjaxResult.success(batchNoList);
    }

    /**
     * 批量换卡
     */
    @PreAuthorize("@ss.hasAnyPermi('system:card:replace,agent:agentCard:replace')")
    @Log(title = "卡密", businessType = BusinessType.REPLACE)
    @PostMapping("/batchReplace")
    public AjaxResult batchReplace(@RequestBody BatchReplaceVo vo) {
        String[] split = vo.getContent().split("\n");
        Set<String> set = new HashSet<>(Arrays.asList(split));
        if (set.isEmpty()) {
            return AjaxResult.success("请输入要操作的内容，每行一个");
        }
        List<String> list = set.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        LinkedHashMap<String, List<String>> result = new LinkedHashMap<>();
        String a = "成功替换的卡密";
        String b = "非所选软件的卡密";
        String c = "不符合换卡条件的卡密";
        String d = "不存在的卡密";
        String e = "生成新卡密异常";
        String f = "非您代理的卡密";
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
            SysCard card = sysCardService.selectSysCardByCardNo(item);
            if (card == null) {
                result.get(d).add(item + "【卡密不存在】");
            } else {
                if(!permissionService.hasAnyRoles("sadmin,admin")) {
                    // 检查是否自己代理的卡
                    SysAgent agent = sysAgentService.selectSysAgentByUserId(getUserId());
                    if(!Objects.equals(card.getAgentId(), agent.getAgentId())) {
                        result.get(f).add(item + "【非您代理的卡密】");
                        continue;
                    }
                }
                if (app != null && !Objects.equals(card.getAppId(), app.getAppId())) {
                    result.get(b).add(item + "【所属软件：" + app.getAppName() + "】【" + card.getCardName() + "】");
                } else {
                    app = card.getApp();
                    // 检查换卡开关
                    Long templateId = card.getTemplateId();
                    SysCardTemplate template = sysCardTemplateService.selectSysCardTemplateByTemplateId(templateId);
                    if(templateId == null) {
                        result.get(c).add(item + "【此卡类不存在】");
                    } else if(!template.getEnableReplace().equals(UserConstants.YES)) {
                        result.get(c).add(item + "【此卡类未开启换卡】");
                    } else {
                        // 换卡条件：未过期， 未冻结
                        if(UserStatus.DELETED.getCode().equals(card.getDelFlag())) {
                            result.get(c).add(item + "【卡密已被删除】");
                        } else if(UserStatus.DISABLE.getCode().equals(card.getStatus())) {
                            result.get(c).add(item + "【卡密已停用】");
                        } else if(card.getExpireTime().before(DateUtils.getNowDate())) {
                            result.get(c).add(item + "【卡密已过期】");
                        } else {
                            // 检查阈值
                            if(card.getIsCharged().equals(UserConstants.YES)) {
                                result.get(c).add(item + "【卡密已使用】");
//                                SysAppUser appUser = null;
//                                if(card.getChargeTo() != null) {
//                                    appUser = sysAppUserService.selectSysAppUserByAppUserId(card.getChargeTo());
//                                }
//                                if(template.getReplaceThreshold() == -1) {
//                                    result.get(c).add(item + "【卡密已使用】");
//                                } else if(card.getChargeType() == ChargeType.CHARGE) {
//                                    result.get(c).add(item + "【卡密已使用且已经以卡充卡到软件用户】");
//                                } else if(card.getChargeTo() == null || appUser == null) {
//                                    result.get(c).add(item + "【v1.8.2及之前版本使用的卡密，无法关联到软件用户，不支持换卡】");
//                                } else {
//                                    if(app.getBillType() == BillType.TIME) {
//                                        long second = DateUtils.differentMillisecond(DateUtils.getNowDate(), appUser.getExpireTime()) / 1000;
//                                        if(second < template.getReplaceThreshold() || second <= 0) {
//                                            result.get(c).add(item + "【卡密已使用且不满足可换卡条件或单码已无剩余时间】");
//                                        } else {
//                                            // 符合换卡条件
//                                            doReplace(vo, item, template, card, batchNo, result, a, e);
//                                        }
//                                    }  else if(app.getBillType() == BillType.POINT) {
//                                        if(appUser.getPoint().compareTo(BigDecimal.valueOf(template.getReplaceThreshold())) < 0 || appUser.getPoint().compareTo(BigDecimal.ZERO) < 0) {
//                                            result.get(c).add(item + "【卡密已使用且不满足可换卡条件或单码已无剩余点数】");
//                                        } else {
//                                            // 符合换卡条件
//                                            doReplace(vo, item, template, card, batchNo, result, a, e);
//                                        }
//                                    } else {
//                                        throw new ServiceException("软件计费方式设置有误");
//                                    }
    //                                SysAppUser updateUser = new SysAppUser();
    //                                updateUser.setAppUserId(appUser.getAppUserId());
    //                                updateUser.setStatus(UserStatus.DISABLE.getCode());
    //                                sysAppUserService.updateSysAppUser(updateUser);
//                                }
                            } else {
                                // 符合换卡条件
                                doReplace(vo, item, template, card, batchNo, result, a, e);
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

    private void doReplace(BatchReplaceVo vo, String item, SysCardTemplate template, SysCard card, String batchNo, LinkedHashMap<String, List<String>> result, String a, String e) {
        try {
            SysCard newCard = sysCardTemplateService.genCardReplace(template, card, vo, batchNo);
            SysCard update = new SysCard();
            update.setCardId(card.getCardId());
            String remark = "此卡已被【" + newCard.getCardNo() + "】替换";
            update.setRemark(StringUtils.isNotBlank(card.getRemark()) ? card.getRemark() + "\n" + remark : remark);
            update.setDelFlag("2"); // 删除
            sysCardService.updateSysCard(update);
            result.get(a).add(item + " => " + newCard.getCardNo() + " " + newCard.getCardPass());
        } catch (Exception ex) {
            result.get(e).add(item + "【" + ex.getMessage() + "】");
        }
    }
}