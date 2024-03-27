package com.ruoyi.sale.service;

import com.ruoyi.sale.domain.SysSaleOrder;
import com.ruoyi.system.domain.SysCard;
import com.ruoyi.system.domain.SysLoginCode;

import java.util.List;
import java.util.Map;

public interface ISysSaleShopService {

    public void deliveryGoods(SysSaleOrder sso);

    /**
     * 获取可售卖的卡，满足条件：卡上架，卡未过期，卡未使用，卡未售出，卡状态正常
     *
     * @param templateId
     * @return
     */
    public List<SysCard> getSaleableCard(Long templateId);

    public int getSaleableCardSize(Long templateId);

    public Map<Long, Long> getSaleableCardSizeAll();

    /**
     * 获取可售卖的卡，满足条件：卡上架，卡未过期，卡未使用，卡未售出，卡状态正常
     *
     * @param templateId
     * @return
     */
    public List<SysLoginCode> getSaleableLoginCode(Long templateId);

    public int getSaleableLoginCodeSize(Long templateId);

    public Map<Long, Long> getSaleableLoginCodeSizeAll();
}
