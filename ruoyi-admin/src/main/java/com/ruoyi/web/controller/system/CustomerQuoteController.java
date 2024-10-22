package com.ruoyi.web.controller.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.TCustomerQuote;
import com.ruoyi.system.domain.TNumberCutCost;
import com.ruoyi.system.domain.TProcessCost;
import com.ruoyi.system.domain.TRawMaterialCost;
import com.ruoyi.system.domain.vo.CustomerQuoteVO;
import com.ruoyi.system.service.ITCustomerQuoteService;
import com.ruoyi.system.service.ITNumberCutCostService;
import com.ruoyi.system.service.ITProcessCostService;
import com.ruoyi.system.service.ITRawMaterialCostService;

@RestController
@RequestMapping("/system/quote")
public class CustomerQuoteController extends BaseController {
	
	@Autowired
    private ITCustomerQuoteService tCustomerQuoteService;
	@Autowired
	private ITRawMaterialCostService rawMaterialCostService;
	@Autowired
	private ITNumberCutCostService numberCutCostService;
	@Autowired
	private ITProcessCostService processCostService;

	/**
     * 查询客户报价单列表
     */
    @PreAuthorize("@ss.hasPermi('system:easyquote:list')")
    @GetMapping("/list")
    public TableDataInfo list(TCustomerQuote tCustomerQuote) {
        startPage();
        List<Map<String,Object>> list = tCustomerQuoteService.listCoustomerQuote(tCustomerQuote);
        return getDataTable(list);
    }
    
	@PreAuthorize("@ss.hasPermi('system:easyquote:add')")
    @Log(title = "客户报价单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody List<CustomerQuoteVO> quoteList) {
		return success(tCustomerQuoteService.createCustomerQuote(getUsername(), quoteList));
	}
	
	@PreAuthorize("@ss.hasPermi('system:easyquote:list')")
    @GetMapping("/detail/{quote_no}")
    public AjaxResult detail(@PathVariable(name = "quote_no") String quote_no) {
		Map<String, Object> map = new HashMap<>();
		TCustomerQuote cq = new TCustomerQuote();
		cq.setQuoteNo(quote_no);
		List<TCustomerQuote> quoteList = tCustomerQuoteService.selectTCustomerQuoteList(cq);
		map.put("quote", quoteList);
		TRawMaterialCost mc = new TRawMaterialCost();
		mc.setQuoteNo(quote_no);
		List<TRawMaterialCost> mcList = rawMaterialCostService.selectTRawMaterialCostList(mc);
		map.put("rawMaterialCost", mcList);
		TNumberCutCost cc = new TNumberCutCost();
		cc.setQuoteNo(quote_no);
		List<TNumberCutCost> ccList = numberCutCostService.selectTNumberCutCostList(cc);
		map.put("numberCutCost", ccList);
		TProcessCost pc = new TProcessCost();
		pc.setQuoteNo(quote_no);
		List<TProcessCost> pcList = processCostService.selectTProcessCostList(pc);
		map.put("processCost", pcList);
        return success(map);
    }
}
