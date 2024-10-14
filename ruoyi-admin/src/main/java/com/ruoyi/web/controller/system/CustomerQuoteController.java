package com.ruoyi.web.controller.system;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.ruoyi.system.domain.vo.CustomerQuoteVO;
import com.ruoyi.system.service.ITCustomerQuoteService;

@RestController
@RequestMapping("/system/quote")
public class CustomerQuoteController extends BaseController {
	
	@Autowired
    private ITCustomerQuoteService tCustomerQuoteService;

	/**
     * 查询客户报价单列表
     */
    @PreAuthorize("@ss.hasPermi('system:easyquote:add')")
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
}
