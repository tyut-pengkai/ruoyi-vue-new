package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.WmsPurchaseOrderDetailRaw;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 采购单临时-明细Mapper接口
 * 
 * @author weifu
 * @date 2023-02-20
 */
@Repository
public interface WmsPurchaseOrderDetailRawMapper 
{
    /**
     * 查询采购单临时-明细
     * 
     * @param id 采购单临时-明细主键
     * @return 采购单临时-明细
     */
    public WmsPurchaseOrderDetailRaw selectWmsPurchaseOrderDetailRawById(Long id);

    /**
     * 查询采购单临时-明细列表
     * 
     * @param wmsPurchaseOrderDetailRaw 采购单临时-明细
     * @return 采购单临时-明细集合
     */
    public List<WmsPurchaseOrderDetailRaw> selectWmsPurchaseOrderDetailRawList(WmsPurchaseOrderDetailRaw wmsPurchaseOrderDetailRaw);

    /**
     * 新增采购单临时-明细
     * 
     * @param wmsPurchaseOrderDetailRaw 采购单临时-明细
     * @return 结果
     */
    public int insertWmsPurchaseOrderDetailRaw(WmsPurchaseOrderDetailRaw wmsPurchaseOrderDetailRaw);

    /**
     * 修改采购单临时-明细
     * 
     * @param wmsPurchaseOrderDetailRaw 采购单临时-明细
     * @return 结果
     */
    public int updateWmsPurchaseOrderDetailRaw(WmsPurchaseOrderDetailRaw wmsPurchaseOrderDetailRaw);

    /**
     * 删除采购单临时-明细
     * 
     * @param id 采购单临时-明细主键
     * @return 结果
     */
    public int deleteWmsPurchaseOrderDetailRawById(Long id);

    /**
     * 批量删除采购单临时-明细
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWmsPurchaseOrderDetailRawByIds(Long[] ids);

}
