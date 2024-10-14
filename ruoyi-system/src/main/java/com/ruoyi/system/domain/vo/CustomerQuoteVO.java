package com.ruoyi.system.domain.vo;

import java.io.Serializable;
import java.util.List;

import com.ruoyi.system.domain.TCustomerQuote;
import com.ruoyi.system.domain.TNumberCutCost;
import com.ruoyi.system.domain.TProcessCost;
import com.ruoyi.system.domain.TRawMaterialCost;

public class CustomerQuoteVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long customerId;
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
	
}
