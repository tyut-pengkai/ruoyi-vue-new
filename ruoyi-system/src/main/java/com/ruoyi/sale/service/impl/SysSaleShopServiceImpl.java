package com.ruoyi.sale.service.impl;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.enums.SaleOrderStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.sale.domain.SysSaleOrder;
import com.ruoyi.sale.domain.SysSaleOrderItem;
import com.ruoyi.sale.domain.SysSaleOrderItemGoods;
import com.ruoyi.sale.service.ISysSaleOrderItemGoodsService;
import com.ruoyi.sale.service.ISysSaleOrderService;
import com.ruoyi.sale.service.ISysSaleShopService;
import com.ruoyi.system.domain.SysCard;
import com.ruoyi.system.domain.SysCardTemplate;
import com.ruoyi.system.mapper.SysCardMapper;
import com.ruoyi.system.mapper.SysCardTemplateMapper;
import com.ruoyi.system.service.ISysCardTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysSaleShopServiceImpl implements ISysSaleShopService {

    @Resource
    private SysCardMapper sysCardMapper;
    @Resource
    private SysCardTemplateMapper sysCardTemplateMapper;
    @Resource
    private ISysSaleOrderService sysSaleOrderService;
    @Resource
    private ISysCardTemplateService sysCardTemplateService;
    @Resource
    private ISysSaleOrderItemGoodsService sysSaleOrderItemGoodsService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deliveryGoods(SysSaleOrder sso) {
        if (sso.getStatus() != SaleOrderStatus.PAID) {
            throw new ServiceException("该订单非待发货订单", 400);
        }
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
        Date nowDate = DateUtils.getNowDate();
        sso.setDeliveryTime(nowDate);
        sso.setFinishTime(nowDate);
        sysSaleOrderService.updateSysSaleOrder(sso);
    }

    /**
     * 获取可售卖的卡，满足条件：卡上架，卡未过期，卡未使用，卡未售出，卡状态正常
     *
     * @param templateId
     * @return
     */
    @Override
    public List<SysCard> getSaleableCard(Long templateId) {
        SysCard card = new SysCard();
        card.setTemplateId(templateId);
        card.setOnSale(UserConstants.YES);
        card.setIsCharged(UserConstants.NO);
        card.setIsSold(UserConstants.NO);
        card.setStatus(UserConstants.NORMAL);
        return sysCardMapper.selectSysCardList(card)
                .stream().filter(c -> c.getExpireTime().after(DateUtils.getNowDate())).collect(Collectors.toList());
    }

}
