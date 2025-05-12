package com.easycode.cloud.service;

import com.easycode.cloud.domain.RwmOutSourceReturnOrder;
import com.easycode.cloud.domain.vo.RwmOutSourceReturnOrderDetailVo;
import com.easycode.cloud.domain.vo.RwmOutSourceReturnOrderVo;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.domain.vo.PrintInfoVo;

import java.util.List;

/**
 * 原材料委外发料退退货单Service接口
 *
 * @author fsc
 * @date 2023-03-11
 */
public interface IRwmOutSourceReturnOrderService
{
    /**
     * 查询原材料委外发料退退货单
     *
     * @param id 原材料委外发料退退货单主键
     * @return 原材料委外发料退退货单
     */
    public RwmOutSourceReturnOrder selectRwmOutSourceReturnOrderById(Long id);

    /**
     * 查询原材料委外发料退退货单列表
     *
     * @param rwmOutSourceReturnOrderVo 原材料委外发料退退货单
     * @return 原材料委外发料退退货单集合
     */
    public List<RwmOutSourceReturnOrder> selectRwmOutSourceReturnOrderList(RwmOutSourceReturnOrderVo rwmOutSourceReturnOrderVo);

    /**
     * 查询原材料委外发料退退货单列表
     *
     * @param printInfoVo 原材料委外发料退退货单
     * @return 原材料委外发料退退货单集合
     */
    public List<PrintInfoVo> getPrintInfoByIds(PrintInfoVo printInfoVo);

    /**
     * 新增原材料委外发料退退货单
     *
     * @param rwmOutSourceReturnOrder 原材料委外发料退退货单
     * @return 结果
     */
    public int insertRwmOutSourceReturnOrder(RwmOutSourceReturnOrder rwmOutSourceReturnOrder);

    /**
     * 关闭原材料委外发料退退货单
     *
     * @param rwmOutSourceReturnOrder 原材料委外发料退退货单
     * @return 结果
     */
    public int closeRwmOutSourceOrder(RwmOutSourceReturnOrder rwmOutSourceReturnOrder);

    /**
     * 新增原材料委外发料退退货单及明细
     *
     * @param rwmOutSourceReturnOrderVo 原材料委外发料退退货单
     * @return 结果
     */
    public RwmOutSourceReturnOrder addRwmOutSourceReturn(RwmOutSourceReturnOrderVo rwmOutSourceReturnOrderVo);

    /**
     * 修改原材料委外发料退退货单
     *
     * @param rwmOutSourceReturnOrder 原材料委外发料退退货单
     * @return 结果
     */
    public int updateRwmOutSourceReturnOrder(RwmOutSourceReturnOrder rwmOutSourceReturnOrder);

    /**
     * 批量删除原材料委外发料退退货单
     *
     * @param ids 需要删除的原材料委外发料退退货单主键集合
     * @return 结果
     */
    public int deleteRwmOutSourceReturnOrderByIds(Long[] ids);

    /**
     * 删除原材料委外发料退退货单信息
     *
     * @param id 原材料委外发料退退货单主键
     * @return 结果
     */
    public int deleteRwmOutSourceReturnOrderById(Long id);

    /**
     * 激活原材料委外发料退退货单
     *
     * @param ids 原材料委外发料退退货单主键集合
     * @return 结果
     */
    int activeRwmOutSourceOrderReturnByIds(Long[] ids);


    /**
     * 查找是否存在导入数据
     *
     * @param stockList 原材料委外发料退
     * @return 结果
     */
    public AjaxResult importData(List<RwmOutSourceReturnOrderDetailVo> stockList);
}
