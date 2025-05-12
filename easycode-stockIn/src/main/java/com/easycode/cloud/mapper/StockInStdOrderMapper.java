package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.dto.StoorDto;
import com.easycode.cloud.domain.vo.StockInOrederVo;
import com.weifu.cloud.domain.StockInStdOrder;
import com.weifu.cloud.domain.StockInStdOrderDetail;
import com.weifu.cloud.domain.vo.PrintInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 标准入库单Mapper接口
 *
 * @author weifu
 * @date 2022-12-07
 */
@Repository
public interface StockInStdOrderMapper
{
    /**
     * 查询标准入库单
     *
     * @param stockInStdOrder 标准入库单
     * @return 标准入库单
     */
    public StockInStdOrder selectStockInStdOrder(StockInStdOrder stockInStdOrder);



    /**
     * 查询标准入库单列表
     *
     * @param stockInStdOrder 标准入库单
     * @return 标准入库单集合
     */
    public List<StockInStdOrder> selectStockInStdOrderList(StockInStdOrder stockInStdOrder);

    /**
     * 新增标准入库单
     *
     * @param stockInStdOrder 标准入库单
     * @return 结果
     */
    public int insertStockInStdOrder(StockInStdOrder stockInStdOrder);

    /**
     * 修改标准入库单
     *
     * @param stockInStdOrder 标准入库单
     * @return 结果
     */
    public int updateStockInStdOrder(StockInStdOrder stockInStdOrder);

    /**
     * 删除标准入库单
     *
     * @param id 标准入库单主键
     * @return 结果
     */
    public int deleteStockInStdOrderById(Long id);

    /**
     * 批量删除标准入库单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStockInStdOrderByIds(Long[] ids);

    /**
     * 根据送货单id 关闭标准入库单
     * @param ids 送货单id集合
     * @return 结果
     */
    List<PrintInfoVo> selectInfoByOrderIds(Long[] ids);

    /**
     * 根据送货单详情id查询打印信息
     */
    List<PrintInfoVo> selectInfoByOrderDetailIds(Long[] ids);

    StockInStdOrder selectStockInStdOrderByNo(String stockInOrderNo);

    /**
     * 根据asnNo查询
     * @param asnNo
     * @return
     */
    StockInStdOrder selectStockInStdOrderByAsnNo(String asnNo);

    List<StockInStdOrder> selectStockInStdOrderListByDeliveryOrderIds(Long[] ids);

    List<PrintInfoVo> getPrintInfoByIds(Long[] ids);

    List<StockInStdOrder> timing();

    // 获取用户飞书id
    StockInOrederVo getUserLdap(Long id);


    /**
     * 获取区域,仓位
     * @param materialNo
     * @return
     */
    StoorDto selectMaterialAttrShelves(@Param("materialNo") String materialNo, @Param("tenantId") Long tenantId);

    /**
     * 查询标准入库明细
     * @param id 入库主单id
     * @return
     */
    List<StockInStdOrderDetail> queryWmsStockinStdOrderDetailById(Long id);
}
