package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SysCard;
import com.ruoyi.system.domain.SysCardTemplate;
import com.ruoyi.system.service.ISysCardService;
import com.ruoyi.system.service.ISysCardTemplateService;
import com.ruoyi.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        ExcelUtil<SysCard> util = new ExcelUtil<SysCard>(SysCard.class);
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
}
