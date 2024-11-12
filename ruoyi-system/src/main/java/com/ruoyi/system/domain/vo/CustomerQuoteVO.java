package com.ruoyi.system.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.ruoyi.system.domain.TCustomerQuote;
import com.ruoyi.system.domain.TNumberCutCost;
import com.ruoyi.system.domain.TProcessCost;
import com.ruoyi.system.domain.TRawMaterialCost;

public class CustomerQuoteVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long customerId;
	private BigDecimal firstPrice;
	private String firstBeginTime;
	private String firstEndTime;
	private BigDecimal secondPrice;
	private String secondBeginTime;
	private String secondEndTime;
	private TCustomerQuote quote;
	private TRawMaterialCost rawMaterial;
	private TNumberCutCost numberCut;
	private List<TProcessCost> process;
	public TCustomerQuote getQuote() {
		return quote;
	}
	public void setQuote(TCustomerQuote quote) {
		this.quote = quote;
	}
	public TRawMaterialCost getRawMaterial() {
		return rawMaterial;
	}
	public void setRawMaterial(TRawMaterialCost rawMaterial) {
		this.rawMaterial = rawMaterial;
	}
	public TNumberCutCost getNumberCut() {
		return numberCut;
	}
	public void setNumberCut(TNumberCutCost numberCut) {
		this.numberCut = numberCut;
	}
	public List<TProcessCost> getProcess() {
		return process;
	}
	public void setProcess(List<TProcessCost> process) {
		this.process = process;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public BigDecimal getFirstPrice() {
		return firstPrice;
	}
	public void setFirstPrice(BigDecimal firstPrice) {
		this.firstPrice = firstPrice;
	}
	public String getFirstBeginTime() {
		return firstBeginTime;
	}
	public void setFirstBeginTime(String firstBeginTime) {
		this.firstBeginTime = firstBeginTime;
	}
	public String getFirstEndTime() {
		return firstEndTime;
	}
	public void setFirstEndTime(String firstEndTime) {
		this.firstEndTime = firstEndTime;
	}
	public BigDecimal getSecondPrice() {
		return secondPrice;
	}
	public void setSecondPrice(BigDecimal secondPrice) {
		this.secondPrice = secondPrice;
	}
	public String getSecondBeginTime() {
		return secondBeginTime;
	}
	public void setSecondBeginTime(String secondBeginTime) {
		this.secondBeginTime = secondBeginTime;
	}
	public String getSecondEndTime() {
		return secondEndTime;
	}
	public void setSecondEndTime(String secondEndTime) {
		this.secondEndTime = secondEndTime;
	}
	
}
