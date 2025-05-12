package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.InspectOrder;
import com.easycode.cloud.domain.dto.InspectOrderDto;
import com.easycode.cloud.domain.vo.InspectOrderVo;
import com.easycode.cloud.domain.vo.StockInDetailPrintVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 送检单Mapper接口
 *
 * @author weifu
 * @date 2023-03-29
 */
@Repository
public interface InspectOrderMapper
{
    /**
     * 查询送检单
     *
     * @param id 送检单主键
     * @return 送检单
     */
    public InspectOrder selectInspectOrderById(Long id);
    /**
     * 查询送检单
     *
     * @param orderNo 送检单单号
     * @return 送检单
     */
    public InspectOrder selectInspectOrderByNo(String orderNo);

    /**
     * 查询送检单列表
     *
     * @param inspectOrder 送检单
     * @return 送检单集合
     */
    public List<InspectOrderVo> selectInspectOrderList(InspectOrderVo inspectOrder);

    /**
     * 新增送检单
     *
     * @param inspectOrder 送检单
     * @return 结果
     */
    public int insertInspectOrder(InspectOrder inspectOrder);

    /**
     * 修改送检单
     *
     * @param inspectOrder 送检单
     * @return 结果
     */
    public int updateInspectOrder(InspectOrder inspectOrder);

    /**
     * 修改送检单
     *
     * @param inspectOrder 送检单
     * @return 结果
     */
    public int updateInspectOrderByNo(InspectOrder inspectOrder);

    /**
     * 删除送检单
     *
     * @param id 送检单主键
     * @return 结果
     */
    public int deleteInspectOrderById(Long id);

    /**
     * 批量删除送检单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteInspectOrderByIds(Long[] ids);

    /**
     * 查询送检单列表(关联送检单明细)
     *
     * @param inspectOrder 送检单
     * @return 送检单集合
     */
    public List<InspectOrderVo> selectInspectOrderJoinDetailsList(InspectOrderVo inspectOrder);

    /**
     * 批量录入检验结果
     *
     * @param inspectOrderDto 送检单
     * @return 结果
     */
    public int updateInspectOrderBatch(InspectOrderDto inspectOrderDto);

    StockInDetailPrintVo selectInspectOrderByTaskNo(String taskNo);
}

