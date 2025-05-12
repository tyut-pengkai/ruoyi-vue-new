package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.WmsPurchaseOrderRaw;
import org.apache.ibatis.annotations.MapKey;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 采购单临时-主Mapper接口
 * 
 * @author weifu
 * @date 2023-02-20
 */
@Repository
public interface WmsPurchaseOrderRawMapper 
{
    /**
     * 查询采购单临时-主
     * 
     * @param id 采购单临时-主主键
     * @return 采购单临时-主
     */
    public WmsPurchaseOrderRaw selectWmsPurchaseOrderRawById(Long id);

    /**
     * 查询采购单临时-主列表
     * 
     * @param wmsPurchaseOrderRaw 采购单临时-主
     * @return 采购单临时-主集合
     */
    public List<WmsPurchaseOrderRaw> selectWmsPurchaseOrderRawList(WmsPurchaseOrderRaw wmsPurchaseOrderRaw);

    /**
     * 新增采购单临时-主
     * 
     * @param wmsPurchaseOrderRaw 采购单临时-主
     * @return 结果
     */
    public int insertWmsPurchaseOrderRaw(WmsPurchaseOrderRaw wmsPurchaseOrderRaw);

    /**
     * 修改采购单临时-主
     * 
     * @param wmsPurchaseOrderRaw 采购单临时-主
     * @return 结果
     */
    public int updateWmsPurchaseOrderRaw(WmsPurchaseOrderRaw wmsPurchaseOrderRaw);

    /**
     * 删除采购单临时-主
     * 
     * @param id 采购单临时-主主键
     * @return 结果
     */
    public int deleteWmsPurchaseOrderRawById(Long id);

    /**
     * 批量删除采购单临时-主
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWmsPurchaseOrderRawByIds(Long[] ids);

    /**
     * 获取所有公司合集
     * @MapKey
     * @return
     */
    @MapKey("companyCode")
   Map<String, Map<String, Object>> getCompanyMaps();

    /**
     * 获取供应商信息集合
     * @return
     */
    @MapKey("supplierCode")
    Map<String,Map<String,Object>> getSupplyMaps();

    /**
     * 获取工厂map
     * @return
     */
    @MapKey("factoryCode")
    Map<String,Map<String,Object>> getFactoryMaps();


    /**
     * 获取物料map
     * @return
     */
    @MapKey("materialNo")
    Map<String,Map<String,Object>> getMaterialMaps();

    /**
     * 根据订单号删除
     * @param vbeln
     */
    void deleteWmsPurchaseOrderRawByOrderNo(String vbeln);
}
