package com.easycode.cloud.service.impl;

import com.easycode.cloud.domain.dto.PurchaseOrderDetailPlanDto;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.core.utils.PageUtils;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.weifu.cloud.constant.CommonYesOrNo;
import com.weifu.cloud.domain.PurchaseOrderDetailPlan;
import com.weifu.cloud.domian.GoodsSourceDef;
import com.easycode.cloud.mapper.DeliveryOrderMapper;
import com.easycode.cloud.mapper.PurchaseOrderDetailPlanMapper;
import com.easycode.cloud.service.IPurchaseOrderDetailPlanService;
import com.weifu.cloud.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 采购协议计划行Service业务层处理
 * 
 * @author bcp
 * @date 2023-09-24
 */
@Service
public class PurchaseOrderDetailPlanServiceImpl implements IPurchaseOrderDetailPlanService 
{
    @Autowired
    private PurchaseOrderDetailPlanMapper purchaseOrderDetailPlanMapper;

    @Autowired
    private DeliveryOrderMapper deliveryOrderMapper;

    /**
     * 查询采购协议计划行
     * 
     * @param id 采购协议计划行主键
     * @return 采购协议计划行
     */
    @Override
    public PurchaseOrderDetailPlan selectPurchaseOrderDetailPlanById(Long id)
    {
        return purchaseOrderDetailPlanMapper.selectPurchaseOrderDetailPlanById(id);
    }

    /**
     * 查询采购协议计划行列表
     * 
     * @param purchaseOrderDetailPlan 采购协议计划行
     * @return 采购协议计划行
     */
    @Override
    public List<PurchaseOrderDetailPlan> selectPurchaseOrderDetailPlanList(PurchaseOrderDetailPlan purchaseOrderDetailPlan)
    {
        return purchaseOrderDetailPlanMapper.selectPurchaseOrderDetailPlanList(purchaseOrderDetailPlan);
    }

    /**
     * 新增采购协议计划行
     * 
     * @param purchaseOrderDetailPlan 采购协议计划行
     * @return 结果
     */
    @Override
    public int insertPurchaseOrderDetailPlan(PurchaseOrderDetailPlan purchaseOrderDetailPlan)
    {
        purchaseOrderDetailPlan.setCreateTime(DateUtils.getNowDate());
        return purchaseOrderDetailPlanMapper.insertPurchaseOrderDetailPlan(purchaseOrderDetailPlan);
    }

    /**
     * 修改采购协议计划行
     * 
     * @param purchaseOrderDetailPlan 采购协议计划行
     * @return 结果
     */
    @Override
    public int updatePurchaseOrderDetailPlan(PurchaseOrderDetailPlan purchaseOrderDetailPlan)
    {
        purchaseOrderDetailPlan.setUpdateTime(DateUtils.getNowDate());
        return purchaseOrderDetailPlanMapper.updatePurchaseOrderDetailPlan(purchaseOrderDetailPlan);
    }

    /**
     * 批量删除采购协议计划行
     * 
     * @param ids 需要删除的采购协议计划行主键
     * @return 结果
     */
    @Override
    public int deletePurchaseOrderDetailPlanByIds(Long[] ids)
    {
        return purchaseOrderDetailPlanMapper.deletePurchaseOrderDetailPlanByIds(ids);
    }

    /**
     * 删除采购协议计划行信息
     * 
     * @param id 采购协议计划行主键
     * @return 结果
     */
    @Override
    public int deletePurchaseOrderDetailPlanById(Long id)
    {
        return purchaseOrderDetailPlanMapper.deletePurchaseOrderDetailPlanById(id);
    }

    /**
     * 采购单处理页面 查询采购单计划行
     * @param purchaseOrderDetailPlanDto 采购协议计划行dto
     * @return 结果
     */
    @Override
    public List<PurchaseOrderDetailPlan> queryPurchaseOrderDetailPlan(PurchaseOrderDetailPlanDto purchaseOrderDetailPlanDto) {
        // 根据用户类型判断是否为外部供应商，
        String vendorCode = SecurityUtils.getComCode();

        LoginUser loginUser = SecurityUtils.getLoginUser();
        //判断是否为外部供应商
        String vendorFlag = loginUser.getSysUser().getVendorFlag();
        //仅外部供应商进行详情过滤
        if (CommonYesOrNo.YES.equals(vendorFlag)){
            //查询物料货源信息
            GoodsSourceDef goodsSourceDef = new GoodsSourceDef();
            goodsSourceDef.setStatus("0");
            goodsSourceDef.setFactoryCode(vendorCode);
            goodsSourceDef.setSupplierCode(purchaseOrderDetailPlanDto.getSupplierCode());
            goodsSourceDef.setMaterialNo(purchaseOrderDetailPlanDto.getMaterialNo());
            List<GoodsSourceDef> goodsSourceDefList = deliveryOrderMapper.selectGoodsSourceDefList(goodsSourceDef);
            //供应商-物料号 确定唯一物料货源信息
            Map<String, GoodsSourceDef> listMap = goodsSourceDefList.stream().
                    collect(Collectors.toMap(g -> g.getSupplierCode() + "-" + g.getMaterialNo(), e -> e, (a, b) -> a));
            GoodsSourceDef g = listMap.getOrDefault(purchaseOrderDetailPlanDto.getSupplierCode() + "-" + purchaseOrderDetailPlanDto.getMaterialNo(), null);
            if (null != g && g.getSupplierPeriod().compareTo(BigDecimal.ZERO) > 0) {
                //当前时间 年月日
                Date date = DateUtils.parseDate(DateUtils.getDate());
                //时间计算
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);//设置需要计算的时间
                calendar.add(Calendar.DATE, g.getSupplierPeriod().intValue());//+供应商时间
                Date newDate = calendar.getTime();//获取计算结果 当前时间+供应商时间
                //时间大小比较 当前时间+供应商时间 <= 送货时间 添加供应商时间
                purchaseOrderDetailPlanDto.setSupplierPeriod(newDate);
            }
        }
        PageUtils.startPage();
        return purchaseOrderDetailPlanMapper.queryPurchaseOrderDetailPlan(purchaseOrderDetailPlanDto);
    }
}
