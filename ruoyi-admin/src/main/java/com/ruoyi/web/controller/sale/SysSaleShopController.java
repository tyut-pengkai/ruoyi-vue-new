package com.ruoyi.web.controller.sale;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.SaleOrderStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.sale.domain.SysSaleOrder;
import com.ruoyi.sale.domain.SysSaleOrderItem;
import com.ruoyi.sale.domain.SysSaleOrderItemGoods;
import com.ruoyi.sale.domain.vo.SysCardVo;
import com.ruoyi.sale.domain.vo.SysSaleOrderItemVo;
import com.ruoyi.sale.service.ISysSaleOrderItemGoodsService;
import com.ruoyi.sale.service.ISysSaleOrderService;
import com.ruoyi.system.domain.SysCard;
import com.ruoyi.system.domain.SysCardTemplate;
import com.ruoyi.system.mapper.SysAppMapper;
import com.ruoyi.system.mapper.SysCardMapper;
import com.ruoyi.system.mapper.SysCardTemplateMapper;
import com.ruoyi.system.mapper.SysLoginCodeMapper;
import com.ruoyi.system.service.ISysCardTemplateService;
import com.ruoyi.web.controller.sale.vo.SaleAppVo;
import com.ruoyi.web.controller.sale.vo.SaleCardTemplateVo;
import com.ruoyi.web.controller.sale.vo.SaleOrderVo;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
    private ISysCardTemplateService sysCardTemplateService;
    @Autowired
    private SysCardMapper sysCardMapper;
    @Autowired
    private SysLoginCodeMapper sysLoginCodeMapper;
    @Autowired
    private ISysSaleOrderService sysSaleOrderService;
    @Autowired
    private ISysSaleOrderItemGoodsService sysSaleOrderItemGoodsService;

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
        String orderNo = genOrderNo(saleOrderVo);
        SysSaleOrder sso = new SysSaleOrder();
        SysSaleOrderItem ssoi = new SysSaleOrderItem();
        sso.setOrderNo(orderNo);
        sso.setUserId(null);
        // 3.计算金额
        SysCardTemplate sct = sysCardTemplateMapper.selectSysCardTemplateByTemplateId(saleOrderVo.getTemplateId());
        BigDecimal totalFee = sct.getPrice().multiply(BigDecimal.valueOf(saleOrderVo.getBuyNum()));
        sso.setTotalFee(totalFee);
        // 4.计算优惠
        sso.setDiscountRule(null);
        sso.setDiscountFee(null);
        // 5.计算要支付的金额
        sso.setActualFee(totalFee);
        sso.setPayMode(saleOrderVo.getPayMode());
        sso.setStatus(SaleOrderStatus.WAIT_PAY);
        sso.setContact(saleOrderVo.getContact());
        sso.setQueryPass(saleOrderVo.getQueryPass());
        sso.setCreateBy(null);
        sso.setPaymentTime(null);
        sso.setDeliveryTime(null);
        sso.setFinishTime(null);
        sso.setCloseTime(null);
        sso.setRemark(null);
        sso.setExpireTime(new Date(DateUtils.getNowDate().getTime() + 1000 * 60 * 5));
        sso.setUpdateBy(null);
        sso.setUpdateTime(null);
        // 6.订单详情
        ssoi.setTemplateType("1"); // 1卡类 2登录码类
        ssoi.setTemplateId(saleOrderVo.getTemplateId());
        ssoi.setNum(saleOrderVo.getBuyNum());
        ssoi.setTitle(sct.getCardName());
        ssoi.setPrice(sct.getPrice());
        ssoi.setTotalFee(totalFee);
        ssoi.setDiscountRule(null);
        ssoi.setDiscountFee(null);
        ssoi.setActualFee(totalFee);
        List<SysSaleOrderItem> itemList = new ArrayList<>();
        itemList.add(ssoi);
        sso.setSysSaleOrderItemList(itemList);
        sysSaleOrderService.insertSysSaleOrder(sso);
        return AjaxResult.success().put("orderNo", orderNo);
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
        return sysCardMapper.selectSysCardList(card)
                .stream().filter(c -> c.getExpireTime().after(DateUtils.getNowDate())).collect(Collectors.toList());
    }

    private synchronized String genOrderNo(SaleOrderVo saleOrderVo) {
        String appId = String.format("%04d", saleOrderVo.getAppId());
        appId = appId.substring(appId.length() - 4, appId.length());
        String tplId = String.format("%04d", saleOrderVo.getTemplateId());
        tplId = tplId.substring(tplId.length() - 4, tplId.length());
        return DateUtils.dateTimeNow("yyMMddHHmmssSSS") + appId + tplId + RandomStringUtils.randomNumeric(2);
    }

    @GetMapping("/notify")
    public AjaxResult notify(HttpServletRequest request, HttpServletResponse response, String orderNo) {
        if (orderNo == null) {
            throw new ServiceException("订单不存在", 400);
        }
        SysSaleOrder sso = sysSaleOrderService.selectSysSaleOrderByOrderNo(orderNo);
        if (sso == null) {
            throw new ServiceException("订单不存在", 400);
        }
        sso.setStatus(SaleOrderStatus.PAID);
        sysSaleOrderService.updateSysSaleOrder(sso);
        // 发货
        deliveryGoods(sso);
        System.out.println("支付成功回调。。。。");
        return AjaxResult.success();
    }

    @Transactional(rollbackFor = Exception.class)
    protected void deliveryGoods(SysSaleOrder sso) {
        List<SysSaleOrderItem> itemList = sso.getSysSaleOrderItemList();
        for (SysSaleOrderItem item : itemList) {
            if (item.getNum() == null || item.getNum() < 0) {
                throw new ServiceException("购卡数量有误，购买失败", 400);
            }
            SysCardTemplate cardTemplate = sysCardTemplateMapper.selectSysCardTemplateByTemplateId(item.getTemplateId());
            if (cardTemplate == null) {
                throw new ServiceException("商品不存在，购买失败", 400);
            }
            List<SysCard> resultCardList = new ArrayList<>();
            // 获取库存数量
            List<SysCard> saleableCard = getSaleableCard(item.getTemplateId());
            if (saleableCard.size() >= item.getNum()) { //库存足够
                resultCardList.addAll(saleableCard.subList(0, item.getNum()));
            } else { // 库存不足
                if (!UserConstants.YES.equals(cardTemplate.getEnableAutoGen())) { // 非自动制卡
                    throw new ServiceException("库存不足，请稍后再试", 400);
                }
                resultCardList.addAll(saleableCard);
                List<SysCard> cardList = sysCardTemplateService.genSysCardBatch(cardTemplate, item.getNum() - saleableCard.size(), UserConstants.NO, "系统制卡");
                resultCardList.addAll(cardList);
            }
            List<SysSaleOrderItemGoods> goodsList = new ArrayList<>();
            for (SysCard card : resultCardList) {
                SysSaleOrderItemGoods goods = new SysSaleOrderItemGoods();
                goods.setItemId(item.getItemId());
                goods.setCardId(card.getCardId());
                goodsList.add(goods);
                // 更新充值卡状态 下架/已出售/
                card.setOnSale(UserConstants.NO);
                card.setIsSold(UserConstants.YES);
                sysCardMapper.updateSysCard(card);
            }
            sysSaleOrderItemGoodsService.insertSysSaleOrderItemGoodsBatch(goodsList);
        }
        sso.setStatus(SaleOrderStatus.TRADE_SUCCESS);
        sysSaleOrderService.updateSysSaleOrder(sso);
    }

    @GetMapping("/getCardList")
    public AjaxResult getCardList(@RequestParam("orderNo") String orderNo, @RequestParam("queryPass") String queryPass) {
        SysSaleOrder sso = sysSaleOrderService.selectSysSaleOrderByOrderNo(orderNo);
        if (orderNo == null) {
            throw new ServiceException("订单不存在", 400);
        }
        if (sso == null) {
            throw new ServiceException("订单不存在", 400);
        }
        if (sso.getStatus() == null || sso.getStatus() == SaleOrderStatus.WAIT_PAY) {
            throw new ServiceException("订单未支付", 400);
        }
        if (!sso.getQueryPass().equals(queryPass)) {
            throw new ServiceException("查询密码有误", 400);
        }

        List<SysSaleOrderItem> itemList = sso.getSysSaleOrderItemList();
        List<SysSaleOrderItemVo> itemVoList = new ArrayList<>();
        for (SysSaleOrderItem item : itemList) {
            List<SysSaleOrderItemGoods> goodsList = sysSaleOrderItemGoodsService.selectSysSaleOrderItemGoodsByItemId(item.getItemId());
            if (goodsList != null && goodsList.size() > 0) {
                if ("1".equals(item.getTemplateType())) { // 充值卡
                    for (SysSaleOrderItemGoods goods : goodsList) {
                        if (item.getGoodsList() == null) {
                            item.setGoodsList(new ArrayList<>());
                        }
                        item.getGoodsList().add(new SysCardVo(sysCardMapper.selectSysCardByCardId(goods.getCardId())));
                    }
                } else if ("2".equals(item.getTemplateType())) { // 登录码

                }
            }
            itemVoList.add(new SysSaleOrderItemVo(item));
        }

        return AjaxResult.success().put("itemList", itemVoList);
    }
}
