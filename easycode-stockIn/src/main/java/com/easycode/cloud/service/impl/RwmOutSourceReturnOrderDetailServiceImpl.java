package com.easycode.cloud.service.impl;

import com.easycode.cloud.domain.vo.RwmOutSourceReturnOrderDetailVo;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.easycode.cloud.domain.RwmOutSourceReturnOrderDetail;
import com.easycode.cloud.mapper.RwmOutSourceReturnOrderDetailMapper;
import com.easycode.cloud.service.IRwmOutSourceReturnOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 原材料委外发料退退货单明细Service业务层处理
 *
 * @author fsc
 * @date 2023-03-11
 */
@Service
public class RwmOutSourceReturnOrderDetailServiceImpl implements IRwmOutSourceReturnOrderDetailService
{
    @Autowired
    private RwmOutSourceReturnOrderDetailMapper rwmOutSourceReturnOrderDetailMapper;

    /**
     * 查询原材料委外发料退退货单明细
     *
     * @param id 原材料委外发料退退货单明细主键
     * @return 原材料委外发料退退货单明细
     */
    @Override
    public RwmOutSourceReturnOrderDetail selectRwmOutSourceReturnOrderDetailById(Long id)
    {
        return rwmOutSourceReturnOrderDetailMapper.selectRwmOutSourceReturnOrderDetailById(id);
    }

    /**
     * 查询原材料委外发料退退货单明细
     *
     * @param id 原材料委外发料退退货单明细主键
     * @return 原材料委外发料退退货单明细
     */
    @Override
    public RwmOutSourceReturnOrderDetailVo getPrintInfoList(Long id)
    {
        return rwmOutSourceReturnOrderDetailMapper.getPrintInfoList(id);
    }

    /**
     * 查询原材料委外发料退退货单明细列表
     *
     * @param rwmOutSourceReturnOrderDetail 原材料委外发料退退货单明细
     * @return 原材料委外发料退退货单明细
     */
    @Override
    public List<RwmOutSourceReturnOrderDetail> selectRwmOutSourceReturnOrderDetailList(RwmOutSourceReturnOrderDetail rwmOutSourceReturnOrderDetail)
    {
        return rwmOutSourceReturnOrderDetailMapper.selectRwmOutSourceReturnOrderDetailList(rwmOutSourceReturnOrderDetail);
    }

    /**
     * 新增原材料委外发料退退货单明细
     *
     * @param rwmOutSourceReturnOrderDetail 原材料委外发料退退货单明细
     * @return 结果
     */
    @Override
    public int insertRwmOutSourceReturnOrderDetail(RwmOutSourceReturnOrderDetail rwmOutSourceReturnOrderDetail)
    {
        rwmOutSourceReturnOrderDetail.setCreateTime(DateUtils.getNowDate());
        return rwmOutSourceReturnOrderDetailMapper.insertRwmOutSourceReturnOrderDetail(rwmOutSourceReturnOrderDetail);
    }

    /**
     * 修改原材料委外发料退退货单明细
     *
     * @param rwmOutSourceReturnOrderDetail 原材料委外发料退退货单明细
     * @return 结果
     */
    @Override
    public int updateRwmOutSourceReturnOrderDetail(RwmOutSourceReturnOrderDetail rwmOutSourceReturnOrderDetail)
    {
        rwmOutSourceReturnOrderDetail.setUpdateTime(DateUtils.getNowDate());
        return rwmOutSourceReturnOrderDetailMapper.updateRwmOutSourceReturnOrderDetail(rwmOutSourceReturnOrderDetail);
    }

    /**
     * 批量删除原材料委外发料退退货单明细
     *
     * @param ids 需要删除的原材料委外发料退退货单明细主键
     * @return 结果
     */
    @Override
    public int deleteRwmOutSourceReturnOrderDetailByIds(Long[] ids)
    {
        return rwmOutSourceReturnOrderDetailMapper.deleteRwmOutSourceReturnOrderDetailByIds(ids);
    }

    /**
     * 删除原材料委外发料退退货单明细信息
     *
     * @param id 原材料委外发料退退货单明细主键
     * @return 结果
     */
    @Override
    public int deleteRwmOutSourceReturnOrderDetailById(Long id)
    {
        return rwmOutSourceReturnOrderDetailMapper.deleteRwmOutSourceReturnOrderDetailById(id);
    }
}
