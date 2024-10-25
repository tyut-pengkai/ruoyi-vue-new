package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BjCustomer;
import com.ruoyi.system.domain.TCustomerQuote;
import com.ruoyi.system.domain.TNumberCutCost;
import com.ruoyi.system.domain.TProcessCost;
import com.ruoyi.system.domain.TRawMaterialCost;
import com.ruoyi.system.domain.vo.CustomerQuoteVO;
import com.ruoyi.system.mapper.BjCustomerMapper;
import com.ruoyi.system.mapper.TCustomerQuoteMapper;
import com.ruoyi.system.mapper.TNumberCutCostMapper;
import com.ruoyi.system.mapper.TProcessCostMapper;
import com.ruoyi.system.mapper.TRawMaterialCostMapper;
import com.ruoyi.system.service.ITCustomerQuoteService;

/**
 * 客户报价单Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-10-10
 */
@Service
public class TCustomerQuoteServiceImpl implements ITCustomerQuoteService 
{
	@Autowired
	private BjCustomerMapper bjCustomerMapper;
    @Autowired
    private TCustomerQuoteMapper tCustomerQuoteMapper;
    @Autowired
    private TRawMaterialCostMapper rawMaterialCostMapper;
    @Autowired
    private TNumberCutCostMapper numberCutCostMapper;
    @Autowired
    private TProcessCostMapper processCostMapper;

    /**
     * 查询客户报价单
     * 
     * @param id 客户报价单主键
     * @return 客户报价单
     */
    @Override
    public TCustomerQuote selectTCustomerQuoteById(Long id)
    {
        return tCustomerQuoteMapper.selectTCustomerQuoteById(id);
    }

    /**
     * 查询客户报价单列表
     * 
     * @param tCustomerQuote 客户报价单
     * @return 客户报价单
     */
    @Override
    public List<TCustomerQuote> selectTCustomerQuoteList(TCustomerQuote tCustomerQuote)
    {
    	List<TCustomerQuote> cqList = tCustomerQuoteMapper.selectTCustomerQuoteList(tCustomerQuote);
    	if(cqList != null && cqList.size() > 0) {
    		cqList.forEach(cq -> {
    			BjCustomer customer = bjCustomerMapper.selectBjCustomerById(cq.getCustomerId());
    			cq.setCustomerName(customer != null ? customer.getName() : "");
    		});
    	}
    	return cqList;
    }

    /**
     * 新增客户报价单
     * 
     * @param tCustomerQuote 客户报价单
     * @return 结果
     */
    @Override
    public int insertTCustomerQuote(TCustomerQuote tCustomerQuote)
    {
        tCustomerQuote.setCreateTime(DateUtils.getNowDate());
        return tCustomerQuoteMapper.insertTCustomerQuote(tCustomerQuote);
    }

    /**
     * 修改客户报价单
     * 
     * @param tCustomerQuote 客户报价单
     * @return 结果
     */
    @Override
    public int updateTCustomerQuote(TCustomerQuote tCustomerQuote)
    {
        return tCustomerQuoteMapper.updateTCustomerQuote(tCustomerQuote);
    }

    /**
     * 批量删除客户报价单
     * 
     * @param ids 需要删除的客户报价单主键
     * @return 结果
     */
    @Override
    public int deleteTCustomerQuoteByIds(Long[] ids)
    {
        return tCustomerQuoteMapper.deleteTCustomerQuoteByIds(ids);
    }

    /**
     * 删除客户报价单信息
     * 
     * @param id 客户报价单主键
     * @return 结果
     */
    @Override
    public int deleteTCustomerQuoteById(Long id)
    {
        return tCustomerQuoteMapper.deleteTCustomerQuoteById(id);
    }
    
    @Transactional
    public List<String> createCustomerQuote(String userName, List<CustomerQuoteVO> quoteList) {
    	List<String> quoteNos = new ArrayList<>();
    	if(quoteList != null && quoteList.size() > 0) {
    		String quoteNo = DateUtils.dateTimeNow() + StringUtils.randomString(5);
    		for(CustomerQuoteVO vo : quoteList) {
    			TCustomerQuote cq = vo.getQuote();
    			cq.setCreateBy(userName);
    			cq.setQuoteNo(quoteNo);
    			cq.setCustomerId(vo.getCustomerId());
    			if(cq.getTotalPrice() != null) {
    				cq.setNoTax(cq.getTotalPrice().divide(new BigDecimal("1.13"), 2));
    				if(cq.getNetWight() != null) {
    					cq.setPerPrice(cq.getNoTax().divide(cq.getNetWight(), 2));
    				}
    			}
    			insertTCustomerQuote(cq);
    			TRawMaterialCost rmc = vo.getRawMaterial();
    			rmc.setQuoteNo(quoteNo);
    			rmc.setCustomerId(vo.getCustomerId());
    			rawMaterialCostMapper.insertTRawMaterialCost(rmc);
    			TNumberCutCost ncc = vo.getNumberCut();
    			ncc.setQuoteNo(quoteNo);
    			ncc.setCustomerId(vo.getCustomerId());
    			numberCutCostMapper.insertTNumberCutCost(ncc);
    			List<TProcessCost> pcList = vo.getProcess();
    			if(pcList != null && pcList.size() > 0) {
    				pcList.forEach(pc -> {
    					pc.setQuoteNo(quoteNo);
    	    			pc.setCustomerId(vo.getCustomerId());
    	    			processCostMapper.insertTProcessCost(pc);
    				});
    			}
    			quoteNos.add(quoteNo);
    		}
    	}
    	return quoteNos;
    }
    
    public List<Map<String,Object>> listCoustomerQuote(TCustomerQuote tCustomerQuote) {
    	return tCustomerQuoteMapper.listCoustomerQuote(tCustomerQuote);
    }
}
