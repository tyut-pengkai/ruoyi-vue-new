package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.RwmOutSourceReturnOrderDetail;
import com.easycode.cloud.domain.vo.RwmOutSourceReturnOrderDetailVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 原材料委外发料退退货单明细Mapper接口
 *
 * @author fsc
 * @date 2023-03-11
 */
@Repository
public interface RwmOutSourceReturnOrderDetailMapper
{
    /**
     * 查询原材料委外发料退退货单明细
     *
     * @param id 原材料委外发料退退货单明细主键
     * @return 原材料委外发料退退货单明细
     */
    public RwmOutSourceReturnOrderDetail selectRwmOutSourceReturnOrderDetailById(Long id);

    /**
     * 查询原材料委外发料退退货单明细
     *
     * @param id 原材料委外发料退退货单明细主键
     * @return 原材料委外发料退退货单明细
     */
    public RwmOutSourceReturnOrderDetailVo getPrintInfoList(Long id);

    /**
     * 查询原材料委外发料退退货单明细列表
     *
     * @param costCenterReturnOrderDetail 原材料委外发料退退货单明细
     * @return 原材料委外发料退退货单明细集合
     */
    public List<RwmOutSourceReturnOrderDetail> selectRwmOutSourceReturnOrderDetailList(RwmOutSourceReturnOrderDetail costCenterReturnOrderDetail);

    /**
     * 新增原材料委外发料退退货单明细
     *
     * @param costCenterReturnOrderDetail 原材料委外发料退退货单明细
     * @return 结果
     */
    public int insertRwmOutSourceReturnOrderDetail(RwmOutSourceReturnOrderDetail costCenterReturnOrderDetail);

    /**
     * 修改原材料委外发料退退货单明细
     *
     * @param costCenterReturnOrderDetail 原材料委外发料退退货单明细
     * @return 结果
     */
    public int updateRwmOutSourceReturnOrderDetail(RwmOutSourceReturnOrderDetail costCenterReturnOrderDetail);

    /**
     * 删除原材料委外发料退退货单明细
     *
     * @param id 原材料委外发料退退货单明细主键
     * @return 结果
     */
    public int deleteRwmOutSourceReturnOrderDetailById(Long id);

    /**
     * 批量删除原材料委外发料退退货单明细
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRwmOutSourceReturnOrderDetailByIds(Long[] ids);

    /**
     * 查询未完成的任务数量
     * @param orderNo 退货单据号
     * @param taskType 退货任务类型
     * @return 数量
     */
    public int getBeContinuedNumber(@Param("orderNo") String orderNo, @Param("taskType") String taskType);
}
