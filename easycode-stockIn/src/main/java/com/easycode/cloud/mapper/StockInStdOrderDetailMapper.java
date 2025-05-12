package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.dto.StoorDto;
import com.easycode.cloud.domain.vo.StockInStdOrderDetailVo;
import com.weifu.cloud.domain.StockInStdOrderDetail;
import com.weifu.cloud.domain.dto.StockInOrderCommonDto;
import com.weifu.cloud.domain.dto.StockInStdOrderDetailDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 标准入库单明细Mapper接口
 *
 * @author weifu
 * @date 2022-12-08
 */
@Repository
public interface StockInStdOrderDetailMapper
{

    /**
     * 查询标准入库单明细列表
     *
     * @param stockInStdOrderDetailDto 标准入库单明细
     * @return 标准入库单明细集合
     */
    List<StockInStdOrderDetail> selectStockInStdOrderDetailList(StockInStdOrderDetailDto stockInStdOrderDetailDto);

    /**
     * 根据物料list、单据状态list查询入库单明细数据，将数据以物料代码分组后返回
     *
     * @param stock 标准入库单明细
     * @return 根据物料代码分组后的数据集合
     */
    List<StockInOrderCommonDto> queryOnWayNumGroupByMaterialNo(StockInOrderCommonDto stock);

    /**
     * 新增标准入库单明细
     *
     * @param stockInStdOrderDetail 标准入库单明细
     * @return 结果
     */
    public int insertStockInStdOrderDetail(StockInStdOrderDetail stockInStdOrderDetail);

    /**
     * 修改标准入库单明细
     *
     * @param stockInStdOrderDetail 标准入库单明细
     * @return 结果
     */
    public int updateStockInStdOrderDetail(StockInStdOrderDetail stockInStdOrderDetail);

    /**
     * 删除标准入库单明细
     *
     * @param id 标准入库单明细主键
     * @return 结果
     */
    public int deleteStockInStdOrderDetailById(Long id);

    /**
     * 批量删除标准入库单明细
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStockInStdOrderDetailByIds(Long[] ids);

    /**
     * 判断是否需要合并 标准入库单明细
     * @param stockInStdOrderDetail
     * @return
     */
    public StockInStdOrderDetail selectStockInStdOrderDetailByMerge(StockInStdOrderDetail stockInStdOrderDetail);


    /**
     * 查询入标准入口单明细列表(联查主表)
     * @param stockInStdOrderDetail 标准入库单明细
     * @return 标准入库单明细vo集合
     */
    List<StockInStdOrderDetailVo> queryStdOrderAndDetailList(StockInStdOrderDetail stockInStdOrderDetail);

    /**
     * 根据单据id删除明细
     * @param id
     */
    void deleteStockInStdOrderDetailByOrderId(Long id);

    /**
     * 根据单据id集合查询明细
     * @param materialNo
     * @param orderNos
     * @return
     */
    List<StockInStdOrderDetailDto> stdOrderToDeliveryList(@Param("materialNo") String materialNo, @Param("orderNos") List<String> orderNos);
    /**
     * 根据单据id集合查询明细
     * @param orderNos
     * @return
     */
    List<StockInStdOrderDetailDto> stdOrderToDeliveryList(@Param("orderNos") List<String> orderNos);

    /**
     * 根据物料代码和租户id查询库存信息
     * @param materialNo
     * @param tenantId
     * @return
     */
    StoorDto selectStockInStdOrderDetailByOrderId(@Param("materialNo") String materialNo, @Param("tenantId") Long tenantId);
}
