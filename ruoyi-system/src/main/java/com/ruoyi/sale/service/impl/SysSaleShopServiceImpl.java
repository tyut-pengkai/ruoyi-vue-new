package com.ruoyi.sale.service.impl;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.BalanceChangeType;
import com.ruoyi.common.enums.OrderType;
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
import com.ruoyi.system.domain.SysLoginCode;
import com.ruoyi.system.domain.SysLoginCodeTemplate;
import com.ruoyi.system.domain.vo.BalanceChangeVo;
import com.ruoyi.system.mapper.SysCardMapper;
import com.ruoyi.system.mapper.SysCardTemplateMapper;
import com.ruoyi.system.mapper.SysLoginCodeMapper;
import com.ruoyi.system.mapper.SysLoginCodeTemplateMapper;
import com.ruoyi.system.service.ISysCardTemplateService;
import com.ruoyi.system.service.ISysLoginCodeTemplateService;
import com.ruoyi.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class SysSaleShopServiceImpl implements ISysSaleShopService {

    @Resource
    private SysCardMapper sysCardMapper;
    @Resource
    private SysLoginCodeMapper sysLoginCodeMapper;
    @Resource
    private SysCardTemplateMapper sysCardTemplateMapper;
    @Resource
    private SysLoginCodeTemplateMapper sysLoginCodeTemplateMapper;
    @Resource
    private ISysSaleOrderService sysSaleOrderService;
    @Resource
    private ISysCardTemplateService sysCardTemplateService;
    @Resource
    private ISysLoginCodeTemplateService sysLoginCodeTemplateService;
    @Resource
    private ISysSaleOrderItemGoodsService sysSaleOrderItemGoodsService;
    @Resource
    private ISysUserService userService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deliveryGoods(SysSaleOrder sso) {
        synchronized (sso.getOrderNo().intern()) {
            if (sso.getStatus() != SaleOrderStatus.PAID) {
                throw new ServiceException("该订单尚未支付", 400);
            }
            if (OrderType.SALE.equals(sso.getOrderType())) {
                List<SysSaleOrderItem> itemList = sso.getSysSaleOrderItemList();
                List<SysSaleOrderItemGoods> goodsList = new ArrayList<>();
                for (SysSaleOrderItem item : itemList) {
                    if (item.getNum() == null || item.getNum() < 0) {
                        throw new ServiceException("购卡数量有误，购买失败", 400);
                    }
                    if ("1".equals(item.getTemplateType())) {
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
                            List<SysCard> cardList = sysCardTemplateService.genSysCardBatch(cardTemplate, item.getNum() - saleableCard.size(), UserConstants.NO, UserConstants.NO, "销售系统自动制卡");
                            resultCardList.addAll(cardList);
                        }
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
                    } else if ("2".equals(item.getTemplateType())) {
                        SysLoginCodeTemplate cardTemplate = sysLoginCodeTemplateMapper.selectSysLoginCodeTemplateByTemplateId(item.getTemplateId());
                        if (cardTemplate == null) {
                            throw new ServiceException("商品不存在，购买失败", 400);
                        }
                        List<SysLoginCode> resultCardList = new ArrayList<>();
                        // 获取库存数量
                        List<SysLoginCode> saleableCard = getSaleableLoginCode(item.getTemplateId());
                        if (saleableCard.size() >= item.getNum()) { //库存足够
                            resultCardList.addAll(saleableCard.subList(0, item.getNum()));
                        } else { // 库存不足
                            if (!UserConstants.YES.equals(cardTemplate.getEnableAutoGen())) { // 非自动制卡
                                throw new ServiceException("库存不足，请稍后再试", 400);
                            }
                            resultCardList.addAll(saleableCard);
                            List<SysLoginCode> cardList = sysLoginCodeTemplateService.genSysLoginCodeBatch(cardTemplate, item.getNum() - saleableCard.size(), UserConstants.NO, UserConstants.NO, "系统制卡");
                            resultCardList.addAll(cardList);
                        }
                        for (SysLoginCode card : resultCardList) {
                            SysSaleOrderItemGoods goods = new SysSaleOrderItemGoods();
                            goods.setItemId(item.getItemId());
                            goods.setCardId(card.getCardId());
                            goodsList.add(goods);
                            // 更新充值卡状态 下架/已出售/
                            card.setOnSale(UserConstants.NO);
                            card.setIsSold(UserConstants.YES);
                            sysLoginCodeMapper.updateSysLoginCode(card);
                        }
                    }
                    sysSaleOrderItemGoodsService.insertSysSaleOrderItemGoodsBatch(goodsList);
                }
            } else if (OrderType.CHARGE.equals(sso.getOrderType())) {
                BalanceChangeVo change = new BalanceChangeVo();
                change.setUserId(sso.getUserId());
                SysUser user = userService.selectUserById(sso.getUserId());
                change.setUpdateBy(user.getUserName());
                change.setType(BalanceChangeType.RECHARGE);
                change.setDescription("余额充值：账号：" + user.getUserName() + "，" + sso.getActualFee() + "元");
                change.setAvailablePayBalance(sso.getActualFee());
                change.setSaleOrderId(sso.getOrderId());
                userService.updateUserBalance(change);
            } else {
                throw new ServiceException("订单类型有误", 400);
            }
            sso.setStatus(SaleOrderStatus.TRADE_SUCCESS);
            Date nowDate = DateUtils.getNowDate();
            sso.setDeliveryTime(nowDate);
            sso.setManualDelivery('0');
            sso.setFinishTime(nowDate);
            sysSaleOrderService.updateSysSaleOrder(sso);
            log.info("***********************************");
            log.info("* 订单号: {}", sso.getOrderNo());
            log.info("* 订单类型: {}", sso.getOrderType().getInfo());
            log.info("* 实付金额: {}", sso.getActualFee());
            log.info("* 购买产品: {}", sso.getSysSaleOrderItemList().get(0).getTitle());
            log.info("* 产品单价: {}", sso.getSysSaleOrderItemList().get(0).getPrice());
            log.info("* 购买数量: {}", sso.getSysSaleOrderItemList().get(0).getNum());
            log.info("***********************************");
        }
    }

    /**
     * 获取可售卖的卡，满足条件：卡上架，卡未过期，卡未使用，卡未售出，卡状态正常，非代理制卡
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
        card.setIsAgent(UserConstants.NO);
        card.setStatus(UserConstants.NORMAL);
        HashMap<String, Object> params = new HashMap<>();
        params.put("beginExpireTime", DateUtils.getTime());
        params.put("endExpireTime", "9999-12-31 23:59:59");
        card.setParams(params);
        return sysCardMapper.selectSysCardList(card);
                //.stream().filter(c -> c.getExpireTime().after(DateUtils.getNowDate())).collect(Collectors.toList());
    }

    @Override
    public int getSaleableCardSize(Long templateId) {
        SysCard card = new SysCard();
        card.setTemplateId(templateId);
        card.setOnSale(UserConstants.YES);
        card.setIsCharged(UserConstants.NO);
        card.setIsSold(UserConstants.NO);
        card.setIsAgent(UserConstants.NO);
        card.setStatus(UserConstants.NORMAL);
        HashMap<String, Object> params = new HashMap<>();
        params.put("beginExpireTime", DateUtils.getTime());
        params.put("endExpireTime", "9999-12-31 23:59:59");
        card.setParams(params);
        return sysCardMapper.countSysCard(card);
    }

    @Override
    public Map<Long, Long> getSaleableCardSizeAll() {
        SysCard card = new SysCard();
        card.setOnSale(UserConstants.YES);
        card.setIsCharged(UserConstants.NO);
        card.setIsSold(UserConstants.NO);
        card.setIsAgent(UserConstants.NO);
        card.setStatus(UserConstants.NORMAL);
        HashMap<String, Object> params = new HashMap<>();
        params.put("beginExpireTime", DateUtils.getTime());
        params.put("endExpireTime", "9999-12-31 23:59:59");
        card.setParams(params);
        List<Map<String, Long>> mapList = sysCardMapper.countSysCardAll(card);
        return mapListToMap(mapList, "templateId");
    }

    /**
     * 获取可售卖的卡，满足条件：卡上架，卡未过期，卡未使用，卡未售出，卡状态正常，非代理制卡
     *
     * @param templateId
     * @return
     */
    @Override
    public List<SysLoginCode> getSaleableLoginCode(Long templateId) {
        SysLoginCode card = new SysLoginCode();
        card.setTemplateId(templateId);
        card.setOnSale(UserConstants.YES);
        card.setIsCharged(UserConstants.NO);
        card.setIsSold(UserConstants.NO);
        card.setIsAgent(UserConstants.NO);
        card.setStatus(UserConstants.NORMAL);
        HashMap<String, Object> params = new HashMap<>();
        params.put("beginExpireTime", DateUtils.getTime());
        params.put("endExpireTime", "9999-12-31 23:59:59");
        card.setParams(params);
        return sysLoginCodeMapper.selectSysLoginCodeList(card);
//                .stream().filter(c -> c.getExpireTime().after(DateUtils.getNowDate())).collect(Collectors.toList());
    }

    @Override
    public int getSaleableLoginCodeSize(Long templateId) {
        SysLoginCode card = new SysLoginCode();
        card.setTemplateId(templateId);
        card.setOnSale(UserConstants.YES);
        card.setIsCharged(UserConstants.NO);
        card.setIsSold(UserConstants.NO);
        card.setIsAgent(UserConstants.NO);
        card.setStatus(UserConstants.NORMAL);
        HashMap<String, Object> params = new HashMap<>();
        params.put("beginExpireTime", DateUtils.getTime());
        params.put("endExpireTime", "9999-12-31 23:59:59");
        card.setParams(params);
        return sysLoginCodeMapper.countSysLoginCode(card);
    }

    @Override
    public Map<Long, Long> getSaleableLoginCodeSizeAll() {
        SysLoginCode card = new SysLoginCode();
        card.setOnSale(UserConstants.YES);
        card.setIsCharged(UserConstants.NO);
        card.setIsSold(UserConstants.NO);
        card.setIsAgent(UserConstants.NO);
        card.setStatus(UserConstants.NORMAL);
        HashMap<String, Object> params = new HashMap<>();
        params.put("beginExpireTime", DateUtils.getTime());
        params.put("endExpireTime", "9999-12-31 23:59:59");
        card.setParams(params);
        List<Map<String, Long>> mapList = sysLoginCodeMapper.countSysLoginCodeAll(card);
        return mapListToMap(mapList, "templateId");
    }

    private Map<Long, Long> mapListToMap(List<Map<String, Long>> mapList, String keyName) {
        Map<Long, Long> result = new HashMap<>();
        for (Map<String, Long> kv : mapList) {
            Long key = null;
            Long value = null;
            for (Map.Entry<String, Long> entry : kv.entrySet()) {
                if (entry.getKey().equals(keyName)) {
                    key = entry.getValue();
                } else {
                    value = entry.getValue();
                }
            }
            result.put(key, value);
        }
        return result;
    }

}
