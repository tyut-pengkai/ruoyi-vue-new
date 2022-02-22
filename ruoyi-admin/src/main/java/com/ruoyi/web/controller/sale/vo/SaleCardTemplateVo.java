package com.ruoyi.web.controller.sale.vo;

import java.math.BigDecimal;

public class SaleCardTemplateVo {

    private Long templateId;
    private String cardName;
    private BigDecimal price;
    private Integer cardCount;

    public SaleCardTemplateVo(Long templateId, String cardName, BigDecimal price, Integer cardCount) {
        this.templateId = templateId;
        this.cardName = cardName;
        this.price = price;
        this.cardCount = cardCount;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCardCount() {
        return cardCount;
    }

    public void setCardCount(Integer cardCount) {
        this.cardCount = cardCount;
    }
}
