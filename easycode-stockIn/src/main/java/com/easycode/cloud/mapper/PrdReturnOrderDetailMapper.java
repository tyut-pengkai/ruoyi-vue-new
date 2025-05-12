package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.PrdReturnOrderDetail;
import com.weifu.cloud.domain.vo.PrintInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 生产订单发货退货单明细Mapper接口
 *
 * @author bcp
 * @date 2023-03-11
 */
@Repository
public interface PrdReturnOrderDetailMapper
{
    /**
     * 查询生产订单发货退货单明细
     *
     * @param id 生产订单发货退货单明细主键
     * @return 生产订单发货退货单明细
     */
    public PrdReturnOrderDetail selectPrdReturnOrderDetailById(Long id);

    /**
     * 查询生产订单发货退货单明细列表
     *
     * @param prdReturnOrderDetail 生产订单发货退货单明细
     * @return 生产订单发货退货单明细集合
     */
    public List<PrdReturnOrderDetail> selectPrdReturnOrderDetailList(PrdReturnOrderDetail prdReturnOrderDetail);

    /**
     * 查询生产订单发货退货单明细列表
     *
     * @param ids 生产订单发货退货单明细
     * @return 生产订单发货退货单明细集合
     */
    public List<PrintInfoVo> getPrintInfoByIds(Long[] ids);

    /**
     * 新增生产订单发货退货单明细
     *
     * @param prdReturnOrderDetail 生产订单发货退货单明细
     * @return 结果
     */
    public int insertPrdReturnOrderDetail(PrdReturnOrderDetail prdReturnOrderDetail);

    /**
     * 修改生产订单发货退货单明细
     *
     * @param prdReturnOrderDetail 生产订单发货退货单明细
     * @return 结果
     */
    public int updatePrdReturnOrderDetail(PrdReturnOrderDetail prdReturnOrderDetail);

    /**
     * 删除生产订单发货退货单明细
     *
     * @param id 生产订单发货退货单明细主键
     * @return 结果
     */
    public int deletePrdReturnOrderDetailById(Long id);

    /**
     * 批量删除生产订单发货退货单明细
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePrdReturnOrderDetailByIds(Long[] ids);

    /**
     * 查询未完成的任务数量
     * @param orderNo 退货单据号
     * @param taskType 退货任务类型
     * @return 数量
     */
    public int getBeContinuedNumber(@Param("orderNo") String orderNo, @Param("taskType") String taskType);
}
