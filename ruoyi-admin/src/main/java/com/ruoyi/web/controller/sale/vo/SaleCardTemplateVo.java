package com.ruoyi.web.controller.sale.vo;

import java.math.BigDecimal;

public class SaleCardTemplateVo {

    private Long templateId;
    private String cardName;
    private BigDecimal price;
    private Long cardCount;
    private Integer minBuyNum;
    private Integer sort;

    public SaleCardTemplateVo(Long templateId, String cardName, BigDecimal price, Long cardCount, Integer minBuyNum, Integer sort) {
        this.templateId = templateId;
        this.cardName = cardName;
        this.price = price;
        this.cardCount = cardCount;
        this.minBuyNum = minBuyNum;
        this.sort = sort;
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

    public Long getCardCount() {
        return cardCount;
    }

    public void setCardCount(Long cardCount) {
        this.cardCount = cardCount;
    }

    public Integer getMinBuyNum() {
        return minBuyNum;
    }

    public void setMinBuyNum(Integer minBuyNum) {
        this.minBuyNum = minBuyNum;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
