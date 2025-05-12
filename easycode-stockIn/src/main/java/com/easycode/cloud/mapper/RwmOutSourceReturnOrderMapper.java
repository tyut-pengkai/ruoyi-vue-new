package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.RwmOutSourceReturnOrder;
import com.easycode.cloud.domain.vo.RwmOutSourceReturnOrderVo;
import com.weifu.cloud.domain.vo.PrintInfoVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 原材料委外发料退退货单Mapper接口
 *
 * @author fsc
 * @date 2023-03-11
 */
@Repository
public interface RwmOutSourceReturnOrderMapper
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
     * 查询原材料委外发料退退货单-物料标签信息
     *
     * @param ids 原材料委外发料退退货单
     * @return 原材料委外发料退退货单集合
     */
    public List<PrintInfoVo> getPrintInfoByIds(Long[] ids);

    /**
     * 根据详情id查询原材料委外发料退退货单-物料标签信息
     */
    List<PrintInfoVo> getPrintInfoByDetailIds(Long[] ids);

    /**
     * 新增原材料委外发料退退货单
     *
     * @param rwmOutSourceReturnOrder 原材料委外发料退退货单
     * @return 结果
     */
    public int insertRwmOutSourceReturnOrder(RwmOutSourceReturnOrder rwmOutSourceReturnOrder);

    /**
     * 修改原材料委外发料退退货单
     *
     * @param rwmOutSourceReturnOrder 原材料委外发料退退货单
     * @return 结果
     */
    public int updateRwmOutSourceReturnOrder(RwmOutSourceReturnOrder rwmOutSourceReturnOrder);

    /**
     * 删除原材料委外发料退退货单
     *
     * @param id 原材料委外发料退退货单主键
     * @return 结果
     */
    public int deleteRwmOutSourceReturnOrderById(Long id);

    /**
     * 批量删除原材料委外发料退退货单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRwmOutSourceReturnOrderByIds(Long[] ids);
}
