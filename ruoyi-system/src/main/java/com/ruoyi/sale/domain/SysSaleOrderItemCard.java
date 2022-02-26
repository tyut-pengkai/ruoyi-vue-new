package com.ruoyi.sale.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.system.domain.SysCard;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 订单卡密对象 sys_sale_order_item_card
 *
 * @author zwgu
 * @date 2022-02-26
 */
public class SysSaleOrderItemCard extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 订单详情ID
     */
    @Excel(name = "订单详情ID")
    private Long itemId;

    /**
     * 卡密ID
     */
    @Excel(name = "卡密ID")
    private Long cardId;

    /**
     * 卡密信息
     */
    private List<SysCard> sysCardList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public List<SysCard> getSysCardList() {
        return sysCardList;
    }

    public void setSysCardList(List<SysCard> sysCardList) {
        this.sysCardList = sysCardList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("itemId", getItemId())
                .append("cardId", getCardId())
                .append("sysCardList", getSysCardList())
                .toString();
    }
}
