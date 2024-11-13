package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BjCustomer;
import com.ruoyi.system.domain.TCustomerQuote;
import com.ruoyi.system.domain.TMaterial;
import com.ruoyi.system.domain.TNumberCutCost;
import com.ruoyi.system.domain.TProcessCost;
import com.ruoyi.system.domain.TRawMaterialCost;
import com.ruoyi.system.domain.vo.CustomerQuoteVO;
import com.ruoyi.system.mapper.BjCustomerMapper;
import com.ruoyi.system.mapper.TCustomerQuoteMapper;
import com.ruoyi.system.mapper.TMaterialMapper;
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
    @Autowired
    private TMaterialMapper materialMapper;

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
    public AjaxResult createCustomerQuote(String userName, List<CustomerQuoteVO> quoteList) {
    	List<String> quoteNos = new ArrayList<>();
    	if(quoteList != null && quoteList.size() > 0) {
    		String quoteNo = DateUtils.dateTimeNow() + StringUtils.randomString(5);
    		List<CustomerQuoteVO> subCustomer = new ArrayList<>();
    		// 判断提交过来的是否有子件
    		for(CustomerQuoteVO vo : quoteList) {
    			TCustomerQuote cq = vo.getQuote();
    			cq.setQuoteNo(quoteNo);
    			cq.setCustomerId(vo.getCustomerId());
    			Long materialId = cq.getMaterialsId();
    			if(materialId == null) {
    				return AjaxResult.error("请求物料ID错误");
    			}
    			// 根据物料ID查询
    			TMaterial m = new TMaterial();
    			m.setParentId(materialId);
    			List<TMaterial> mList = materialMapper.selectTMaterialList(m);
    			if(mList != null && mList.size() > 0) {
    			} else {
    				// 没有根据物料ID查询到父数据，则是有子件
    				subCustomer.add(vo);
    			}
    		}
    		if(subCustomer.size() == 0) {
    			// 没有子件，返回错误
    			return AjaxResult.error("报价中未包含子件");
    		}
    		
    		for(CustomerQuoteVO vo : quoteList) {
    			TCustomerQuote cq = vo.getQuote();
    			cq.setCreateBy(userName);
    			cq.setQuoteNo(quoteNo);
    			cq.setCustomerId(vo.getCustomerId());
    			cq.setFirstPrice(vo.getFirstPrice());
    			cq.setFirstBeginTime(vo.getFirstBeginTime());
    			cq.setFirstEndTime(vo.getFirstEndTime());
    			cq.setSecondPrice(vo.getSecondPrice());
    			cq.setSecondBeginTime(vo.getSecondBeginTime());
    			cq.setSecondEndTime(vo.getSecondEndTime());
    			insertTCustomerQuote(cq);
    			TRawMaterialCost rmc = vo.getRawMaterial();
    			if(rmc == null) {
    				rmc = new TRawMaterialCost();
    			}
    			rmc.setQuoteNo(quoteNo);
    			rmc.setQuoteId(cq.getId());
    			rmc.setCustomerId(vo.getCustomerId());
    			rawMaterialCostMapper.insertTRawMaterialCost(rmc);
    			TNumberCutCost ncc = vo.getNumberCut();
    			if(ncc == null) {
    				ncc = new TNumberCutCost();
    			}
    			ncc.setQuoteNo(quoteNo);
    			ncc.setQuoteId(cq.getId());
    			ncc.setCustomerId(vo.getCustomerId());
    			numberCutCostMapper.insertTNumberCutCost(ncc);
    			List<TProcessCost> pcList = vo.getProcess();
    			if(pcList != null && pcList.size() > 0) {
    				pcList.forEach(pc -> {
    					pc.setQuoteNo(quoteNo);
    					pc.setQuoteId(cq.getId());
    	    			pc.setCustomerId(vo.getCustomerId());
    	    			if(cq.getParentMaterialsId() != -1) {
    	    				processCostMapper.insertTProcessCost(pc);
    	    			}
    				});
    			} else {
    				for(int i=1;i<4;i++) {
    					TProcessCost pc = new TProcessCost();
    					pc.setQuoteId(cq.getId());
    	        		pc.setQuoteNo(quoteNo);
    	        		pc.setCustomerId(vo.getCustomerId());
    	        		pc.setTypes(i);
    	        		processCostMapper.insertTProcessCost(pc);
    				}
    			}
    			quoteNos.add(quoteNo);
    		}
    		// 根据子件统计数据数据给上一层套件
    		summary(subCustomer);
    		
    	}
    	return AjaxResult.success(quoteNos);
    }
    
    public List<Map<String,Object>> listCoustomerQuote(TCustomerQuote tCustomerQuote) {
    	return tCustomerQuoteMapper.listCoustomerQuote(tCustomerQuote);
    }
    
    private void summary(List<CustomerQuoteVO> subCustomer) {
    	for(CustomerQuoteVO vo : subCustomer) {
    		TCustomerQuote cq = vo.getQuote();
    		TCustomerQuote quote = new TCustomerQuote();
    		quote.setQuoteNo(cq.getQuoteNo());
    		quote.setCustomerId(cq.getCustomerId());
    		quote.setMaterialsId(cq.getMaterialsId());
    		List<TCustomerQuote> cList = tCustomerQuoteMapper.selectTCustomerQuoteList(quote);
    		if(cList != null && cList.size() > 0) {
    			for(TCustomerQuote c : cList) {
    				summaryCustomerQuote(c);
    			}
    		}
    	}
    }
    
    private void summaryCustomerQuote(TCustomerQuote quote) {
    	if(quote.getParentMaterialsId() != -1) {
    		// 通过报价编码和父物料ID，查询父套件的主信息
    		TCustomerQuote c_quote = new TCustomerQuote();
    		c_quote.setQuoteNo(quote.getQuoteNo());
    		c_quote.setMaterialsId(quote.getParentMaterialsId());
    		List<TCustomerQuote> cList = tCustomerQuoteMapper.selectTCustomerQuoteList(c_quote);
    		
    		TCustomerQuote q = null;
    		if(cList != null && cList.size() > 0) {
    			q = cList.get(0);
    			// 更新父套件的净重
				if(q.getNetWight() == null) {
					q.setNetWight(BigDecimal.ZERO);
				}
				q.setNetWight(q.getNetWight().add(quote.getNetWight()));
				q.setFirstPrice(quote.getFirstPrice());
    			q.setFirstBeginTime(quote.getFirstBeginTime());
    			q.setFirstEndTime(quote.getFirstEndTime());
    			q.setSecondPrice(quote.getSecondPrice());
    			q.setSecondBeginTime(quote.getSecondBeginTime());
    			q.setSecondEndTime(quote.getSecondEndTime());
				tCustomerQuoteMapper.updateTCustomerQuote(q);
    		} else {
    			// 没有父套件的记录，则需要记录父套件
    			q = new TCustomerQuote();
    			q.setQuoteNo(quote.getQuoteNo());
    			q.setCustomerId(quote.getCustomerId());
    			q.setMaterialsId(quote.getParentMaterialsId());
    			TMaterial m = materialMapper.selectTMaterialById(quote.getParentMaterialsId());
    			if(m != null) {
    				q.setParentMaterialsId(m.getParentId());
    				q.setMaterialsNo(m.getMaterialCode());
    				q.setName(m.getMaterialName());
    				q.setNetWight(quote.getNetWight());
    			}
    			q.setFirstPrice(quote.getFirstPrice());
    			q.setFirstBeginTime(quote.getFirstBeginTime());
    			q.setFirstEndTime(quote.getFirstEndTime());
    			q.setSecondPrice(quote.getSecondPrice());
    			q.setSecondBeginTime(quote.getSecondBeginTime());
    			q.setSecondEndTime(quote.getSecondEndTime());
    			tCustomerQuoteMapper.insertTCustomerQuote(q);
    		}
    		summaryRawMaterial(q, quote);
    		summaryNumberCutCost(q, quote);
    		summaryProcessCost(q, quote);
    		// 计算父套件的裸价、利润、未税
    		calculate(q);
    		if(q.getParentMaterialsId() != -1) {
    			summaryCustomerQuote(q);
    		}
    	}
    }
    
    private void calculate(TCustomerQuote quote) {
    	BigDecimal ycl = BigDecimal.ZERO;
    	BigDecimal sg = BigDecimal.ZERO;
    	BigDecimal djjg = BigDecimal.ZERO;
    	BigDecimal bmcl = BigDecimal.ZERO;
    	BigDecimal pt = BigDecimal.ZERO;
    	// 查询原材料
    	List<TRawMaterialCost> rmcList = getRawMaterialCostList(quote.getId(), quote.getQuoteNo());
    	if(rmcList != null && rmcList.size() > 0) {
    		TRawMaterialCost rmc = rmcList.get(0);
    		ycl = rmc.getTotalSteel();
    	}
    	// 数割
    	List<TNumberCutCost> nccList = getNumberCutCost(quote.getId(), quote.getQuoteNo());
    	if(nccList != null && nccList.size() > 0) {
    		TNumberCutCost ncc = nccList.get(0);
    		sg = ncc.getTotalCut();
    	}
    	// 单件加工
    	List<TProcessCost> pclist1 = getPorcessCost(quote.getId(), quote.getQuoteNo(), 1);
    	if(pclist1 != null && pclist1.size() > 0) {
    		TProcessCost pc = pclist1.get(0);
    		djjg = pc.getTotalPrice();
    	}
    	// 表面处理
    	List<TProcessCost> pclist2 = getPorcessCost(quote.getId(), quote.getQuoteNo(), 2);
    	if(pclist2 != null && pclist2.size() > 0) {
    		TProcessCost pc = pclist2.get(0);
    		bmcl = pc.getTotalPrice();
    	}
    	// 喷涂
    	List<TProcessCost> pclist3 = getPorcessCost(quote.getId(), quote.getQuoteNo(), 3);
    	if(pclist3 != null && pclist3.size() > 0) {
    		TProcessCost pc = pclist3.get(0);
    		pt = pc.getTotalPrice();
    	}
    	BigDecimal tempPrice = sg.add(djjg).add(bmcl).add(pt);
    	BigDecimal price = tempPrice.add(ycl);
    	BigDecimal lr1 = tempPrice.multiply(new BigDecimal("0.05"));
    	BigDecimal lr2 = ycl.multiply(new BigDecimal("0.05"));
    	BigDecimal lr = lr1.add(lr2);
    	BigDecimal bz1 = price.multiply(new BigDecimal("0.07"));
    	BigDecimal bz2 = quote.getNetWight().multiply(new BigDecimal("0.17"));
    	BigDecimal bz3 = quote.getNetWight().multiply(new BigDecimal("0.03"));
    	BigDecimal bz = bz1.add(bz2).add(bz3);
    	BigDecimal totalPrice = price.add(lr).add(bz);
    	quote.setNakedPrice(price);
    	quote.setProfit(lr);
    	quote.setTransCost(bz);
    	quote.setTotalPrice(totalPrice);
    	quote.setNoTax(totalPrice.divide(new BigDecimal("1.13"), 2));
    	if(quote.getNetWight().compareTo(BigDecimal.ZERO) > 0) {
    		quote.setPerPrice(quote.getNoTax().divide(quote.getNetWight(), 2));
    	}
    	tCustomerQuoteMapper.updateTCustomerQuote(quote);
    }
    
    private void summaryRawMaterial(TCustomerQuote parent_quote,TCustomerQuote son_quote) {
    	List<TRawMaterialCost> rmcParentList = getRawMaterialCostList(parent_quote.getId(), parent_quote.getQuoteNo());
    	List<TRawMaterialCost> rmcSonList = getRawMaterialCostList(son_quote.getId(), son_quote.getQuoteNo());
    	BigDecimal tempWight = BigDecimal.ZERO;
		BigDecimal tempTotalPrice = BigDecimal.ZERO;
		for(TRawMaterialCost son_rmc : rmcSonList) {
			tempWight = tempWight.add(son_rmc.getSteelWight());
			tempTotalPrice = tempTotalPrice.add(son_rmc.getTotalSteel());
		}
    	if(rmcParentList != null && rmcParentList.size() > 0) {
    		// 有父套件的原材料费用
    		TRawMaterialCost parent_rmc = rmcParentList.get(0);
    		parent_rmc.setSteelWight(tempWight);
    		parent_rmc.setTotalSteel(tempTotalPrice);
    		if(parent_rmc.getSteelWight().compareTo(BigDecimal.ZERO) > 0) {
    			parent_rmc.setSteelPerPrice(parent_quote.getNetWight().divide(parent_rmc.getSteelWight(), 2));
    		} else {
    			parent_rmc.setSteelPerPrice(BigDecimal.ZERO);
    		}
    		rawMaterialCostMapper.updateTRawMaterialCost(parent_rmc);
    	} else {
    		// 没有父套件的原材料费用
    		TRawMaterialCost parent_rmc = new TRawMaterialCost();
    		parent_rmc.setQuoteId(parent_quote.getId());
    		parent_rmc.setQuoteNo(parent_quote.getQuoteNo());
    		parent_rmc.setCustomerId(parent_quote.getCustomerId());
    		parent_rmc.setSteelWight(tempWight);
    		parent_rmc.setTotalSteel(tempTotalPrice);
    		if(parent_rmc.getSteelWight().compareTo(BigDecimal.ZERO) > 0) {
    			parent_rmc.setSteelPerPrice(parent_quote.getNetWight().divide(parent_rmc.getSteelWight(), 2));
    		} else {
    			parent_rmc.setSteelPerPrice(BigDecimal.ZERO);
    		}
    		rawMaterialCostMapper.insertTRawMaterialCost(parent_rmc);
    	}
    }
    
    private void summaryNumberCutCost(TCustomerQuote parent_quote,TCustomerQuote son_quote) {
    	List<TNumberCutCost> p_number_cut = getNumberCutCost(parent_quote.getId(), parent_quote.getQuoteNo());
    	List<TNumberCutCost> s_number_cut = getNumberCutCost(son_quote.getId(), son_quote.getQuoteNo());
    	BigDecimal tempTotalPrice = BigDecimal.ZERO;
    	for(TNumberCutCost ncc : s_number_cut) {
    		tempTotalPrice = tempTotalPrice.add(ncc.getTotalCut());
    	}
    	if(p_number_cut != null && p_number_cut.size() > 0) {
    		TNumberCutCost ncc = p_number_cut.get(0);
    		ncc.setTotalCut(tempTotalPrice);
    		numberCutCostMapper.updateTNumberCutCost(ncc);
    	} else {
    		TNumberCutCost ncc = new TNumberCutCost();
    		ncc.setQuoteId(parent_quote.getId());
    		ncc.setQuoteNo(parent_quote.getQuoteNo());
    		ncc.setCustomerId(parent_quote.getCustomerId());
    		ncc.setTotalCut(tempTotalPrice);
    		numberCutCostMapper.insertTNumberCutCost(ncc);
    	}
    }
    
    private void summaryProcessCost(TCustomerQuote parent_quote,TCustomerQuote son_quote) {
    	for(int i=1;i<4;i++) {
    		List<TProcessCost> p_list = getPorcessCost(parent_quote.getId(), parent_quote.getQuoteNo(), i);
        	List<TProcessCost> s_list = getPorcessCost(son_quote.getId(), son_quote.getQuoteNo(), i);
        	
        	BigDecimal temp_price = BigDecimal.ZERO;
        	for(TProcessCost p : s_list) {
        		if(p.getTotalPrice() != null) {
        			temp_price = temp_price.add(p.getTotalPrice());
        		}
        	}
        	if(p_list != null && p_list.size() > 0) {
        		TProcessCost pc = p_list.get(0);
        		pc.setTotalPrice(temp_price);
        		processCostMapper.updateTProcessCost(pc);
        	} else {
        		TProcessCost pc = new TProcessCost();
        		pc.setQuoteId(parent_quote.getId());
        		pc.setQuoteNo(parent_quote.getQuoteNo());
        		pc.setCustomerId(parent_quote.getCustomerId());
        		pc.setTypes(i);
        		pc.setTotalPrice(temp_price);
        		processCostMapper.insertTProcessCost(pc);
        	}
    	}
    }
    
    private List<TRawMaterialCost> getRawMaterialCostList(Long quote_id, String quote_no){
    	TRawMaterialCost rmc = new TRawMaterialCost();
    	rmc.setQuoteId(quote_id);
    	rmc.setQuoteNo(quote_no);
    	return rawMaterialCostMapper.selectTRawMaterialCostList(rmc);
    }
    
    private List<TNumberCutCost> getNumberCutCost(Long quote_id, String quote_no){
    	TNumberCutCost rmc = new TNumberCutCost();
    	rmc.setQuoteId(quote_id);
    	rmc.setQuoteNo(quote_no);
    	return numberCutCostMapper.selectTNumberCutCostList(rmc);
    }
    
    private List<TProcessCost> getPorcessCost(Long quote_id, String quote_no, Integer types) {
    	TProcessCost pc = new TProcessCost();
    	pc.setQuoteId(quote_id);
    	pc.setQuoteNo(quote_no);
    	pc.setTypes(types);
    	return processCostMapper.selectTProcessCostList(pc);
    }
}
