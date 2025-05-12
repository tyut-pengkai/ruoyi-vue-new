package com.easycode.cloud.mapper;

import com.weifu.cloud.domain.StockInFinOrderDetail;
import com.weifu.cloud.domain.vo.StockInFinOrderDetailVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 成品入库明细Mapper接口
 *
 * @author weifu
 * @date 2022-12-08
 */
@Repository
public interface StockInFinOrderDetailMapper {

    /**
     * 查询成品入库明细
     *
     * @param id 成品入库明细主键
     * @return 成品入库明细
     */
    StockInFinOrderDetail selectStockInFinOrderDetailById(Long id);

    /**
     * 查询成品入库明细列表
     *
     * @param stockInFinOrderDetail 成品入库明细
     * @return 成品入库明细集合
     */
    List<StockInFinOrderDetail> selectStockInFinOrderDetailList(StockInFinOrderDetailVo stockInFinOrderDetail);

    /**
     * 新增成品入库明细
     *
     * @param stockInFinOrderDetail 成品入库明细
     * @return 结果
     */
    int insertStockInFinOrderDetail(StockInFinOrderDetail stockInFinOrderDetail);

    /**
     * 修改成品入库明细
     *
     * @param stockInFinOrderDetail 成品入库明细
     * @return 结果
     */
    int updateStockInFinOrderDetail(StockInFinOrderDetail stockInFinOrderDetail);

    /**
     * 删除成品入库明细
     *
     * @param id 成品入库明细主键
     * @return 结果
     */
    int deleteStockInFinOrderDetailById(Long id);

    /**
     * 批量删除成品入库明细
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteStockInFinOrderDetailByIds(Long[] ids);

    List<StockInFinOrderDetail> selectStockInFinOrderDetailByIds(Long[] finOrderIds);

    /**
     * 获取成品收货单据最大批次
     * @param finOrderId 成品收货单id
     * @return 最大批次
     */
    String getMaxLot(Long finOrderId);
}
