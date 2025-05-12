package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.SaleReturnOrder;
import com.easycode.cloud.domain.vo.SaleReturnOrderVo;
import com.weifu.cloud.domain.vo.PrintInfoVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 销售发货退货单Mapper接口
 *
 * @author fsc
 * @date 2023-03-11
 */
@Repository
public interface SaleReturnOrderMapper
{
    /**
     * 查询销售发货退货单
     *
     * @param id 销售发货退货单主键
     * @return 销售发货退货单
     */
    public SaleReturnOrder selectSaleReturnOrderById(Long id);

    /**
     * 查询销售发货退货单列表
     *
     * @param saleReturnOrderVo 销售发货退货单
     * @return 销售发货退货单集合
     */
    public List<SaleReturnOrder> selectSaleReturnOrderList(SaleReturnOrderVo saleReturnOrderVo);

    /**
     * 查询销售发货退货单-物料标签信息
     *
     * @param ids 销售发货退货单
     * @return 销售发货退货单集合
     */
    public List<PrintInfoVo> getPrintInfoByIds(Long[] ids);

    /**
     * 根据详情id查询销售发货退货单-物料标签信息
     */
    List<PrintInfoVo> getPrintInfoByDetailIds(Long[] ids);

    /**
     * 新增销售发货退货单
     *
     * @param saleReturnOrder 销售发货退货单
     * @return 结果
     */
    public int insertSaleReturnOrder(SaleReturnOrder saleReturnOrder);

    /**
     * 批量新增销售发货退货单
     *
     * @param saleReturnOrderList 销售发货退货单集合
     * @return 结果
     */
    int insertBatchSaleReturnOrder(List<SaleReturnOrder> saleReturnOrderList);

    /**
     * 修改销售发货退货单
     *
     * @param saleReturnOrder 销售发货退货单
     * @return 结果
     */
    public int updateSaleReturnOrder(SaleReturnOrder saleReturnOrder);

    /**
     * 删除销售发货退货单
     *
     * @param id 销售发货退货单主键
     * @return 结果
     */
    public int deleteSaleReturnOrderById(Long id);

    /**
     * 删除销售发货退货单
     *
     * @param saleCode 销售发货退货单号
     * @return 结果
     */
    public int deleteBySaleCode(String saleCode);

    /**
     * 批量删除销售发货退货单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSaleReturnOrderByIds(Long[] ids);

    SaleReturnOrder selectSaleReturnOrderByOrderNo(String stockinOrderNo);
}
