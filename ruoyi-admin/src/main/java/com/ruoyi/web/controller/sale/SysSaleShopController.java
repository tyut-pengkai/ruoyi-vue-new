package com.ruoyi.web.controller.sale;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SysCard;
import com.ruoyi.system.domain.SysCardTemplate;
import com.ruoyi.system.mapper.SysAppMapper;
import com.ruoyi.system.mapper.SysCardMapper;
import com.ruoyi.system.mapper.SysCardTemplateMapper;
import com.ruoyi.web.controller.sale.vo.SaleAppVo;
import com.ruoyi.web.controller.sale.vo.SaleCardTemplateVo;
import com.ruoyi.web.controller.sale.vo.SaleOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 销售Controller
 *
 * @author zwgu
 * @date 2022-02-21
 */
@RestController
@RequestMapping("/sale/shop")
public class SysSaleShopController extends BaseController {
    @Autowired
    private SysAppMapper sysAppMapper;
    @Autowired
    private SysCardTemplateMapper sysCardTemplateMapper;
    @Autowired
    private SysCardMapper sysCardTMapper;

    /**
     * 查询软件列表
     */
    @GetMapping("/appList")
    public TableDataInfo appList() {
        List<SaleAppVo> saleAppVoList = new ArrayList<>();
        List<SysApp> appList = sysAppMapper.selectSysAppList(new SysApp());
        for (SysApp app : appList) {
            SysCardTemplate ct = new SysCardTemplate();
            ct.setAppId(app.getAppId());
            ct.setOnSale(UserConstants.YES);
            saleAppVoList.add(new SaleAppVo(app.getAppId(), app.getAppName(), sysCardTemplateMapper.selectSysCardTemplateList(ct).size()));
        }
        return getDataTable(saleAppVoList);
    }

    /**
     * 查询卡密模板列表
     */
    @GetMapping("/cardTemplateList")
    public TableDataInfo cardTemplateList(SysCardTemplate sysCardTemplate) {
        List<SaleCardTemplateVo> saleCardTemplateVoList = new ArrayList<>();
        List<SysCardTemplate> list = sysCardTemplateMapper.selectSysCardTemplateList(sysCardTemplate);
        for (SysCardTemplate ct : list) {
            Integer cardCount = null;
            if (UserConstants.YES.equals(ct.getEnableAutoGen())) {
                cardCount = 999999;
            } else {
                cardCount = getSaleableCard(ct.getTemplateId()).size();
            }
            saleCardTemplateVoList.add(new SaleCardTemplateVo(ct.getTemplateId(), ct.getCardName(), ct.getPrice(), cardCount));
        }
        return getDataTable(saleCardTemplateVoList);
    }

    @PostMapping("/checkStock")
    public AjaxResult checkStock(@RequestBody SaleOrderVo saleOrderVo) {
        // 检查库存
        SysCardTemplate tpl = sysCardTemplateMapper.selectSysCardTemplateByTemplateId(saleOrderVo.getTemplateId());
        if (!UserConstants.YES.equals(tpl.getOnSale())) {
            throw new ServiceException("商品已下架", 400);
        }
        if (!UserConstants.YES.equals(tpl.getEnableAutoGen())) { // 非自动制卡
            if (getSaleableCard(tpl.getTemplateId()).size() < saleOrderVo.getBuyNum()) {
                throw new ServiceException("库存不足，请稍后再试", 400);
            }
        }
        return AjaxResult.success();
    }

    @PostMapping("/createSaleOrder")
    public AjaxResult createSaleOrder(@RequestBody SaleOrderVo saleOrderVo) {
        System.out.println(JSON.toJSONString(saleOrderVo));
        // 1.检查库存
        checkStock(saleOrderVo);
        // 2.订单生成

        return AjaxResult.success().put("orderId", "234567890");
    }

    /**
     * 获取可售卖的卡，满足条件：卡上架，卡未过期，卡未使用，卡未售出，卡状态正常
     *
     * @param templateId
     * @return
     */
    private List<SysCard> getSaleableCard(Long templateId) {
        SysCard card = new SysCard();
        card.setTemplateId(templateId);
        card.setOnSale(UserConstants.YES);
        card.setIsCharged(UserConstants.NO);
        card.setIsSold(UserConstants.NO);
        card.setStatus(UserConstants.NORMAL);
        return sysCardTMapper.selectSysCardList(card)
                .stream().filter(c -> c.getExpireTime().after(DateUtils.getNowDate())).collect(Collectors.toList());
    }

}
