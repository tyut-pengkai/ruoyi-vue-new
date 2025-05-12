package com.easycode.cloud.service;

import com.easycode.cloud.domain.RwmOutSourceReturnOrderDetail;
import com.easycode.cloud.domain.vo.RwmOutSourceReturnOrderDetailVo;

import java.util.List;

/**
 * 原材料委外发料退退货单明细Service接口
 *
 * @author fsc
 * @date 2023-03-11
 */
public interface IRwmOutSourceReturnOrderDetailService
{
    /**
     * 查询原材料委外发料退退货单明细
     *
     * @param id 原材料委外发料退退货单明细主键
     * @return 原材料委外发料退退货单明细
     */
    public RwmOutSourceReturnOrderDetail selectRwmOutSourceReturnOrderDetailById(Long id);

    /**
     * 打印信息
     *
     * @param id 原材料委外发料退退货单明细主键
     * @return 原材料委外发料退退货单明细
     */
    public RwmOutSourceReturnOrderDetailVo getPrintInfoList(Long id);

    /**
     * 查询原材料委外发料退退货单明细列表
     *
     * @param rwmOutSourceReturnOrderDetail 原材料委外发料退退货单明细
     * @return 原材料委外发料退退货单明细集合
     */
    public List<RwmOutSourceReturnOrderDetail> selectRwmOutSourceReturnOrderDetailList(RwmOutSourceReturnOrderDetail rwmOutSourceReturnOrderDetail);

    /**
     * 新增原材料委外发料退退货单明细
     *
     * @param rwmOutSourceReturnOrderDetail 原材料委外发料退退货单明细
     * @return 结果
     */
    public int insertRwmOutSourceReturnOrderDetail(RwmOutSourceReturnOrderDetail rwmOutSourceReturnOrderDetail);

    /**
     * 修改原材料委外发料退退货单明细
     *
     * @param rwmOutSourceReturnOrderDetail 原材料委外发料退退货单明细
     * @return 结果
     */
    public int updateRwmOutSourceReturnOrderDetail(RwmOutSourceReturnOrderDetail rwmOutSourceReturnOrderDetail);

    /**
     * 批量删除原材料委外发料退退货单明细
     *
     * @param ids 需要删除的原材料委外发料退退货单明细主键集合
     * @return 结果
     */
    public int deleteRwmOutSourceReturnOrderDetailByIds(Long[] ids);

    /**
     * 删除原材料委外发料退退货单明细信息
     *
     * @param id 原材料委外发料退退货单明细主键
     * @return 结果
     */
    public int deleteRwmOutSourceReturnOrderDetailById(Long id);
}
