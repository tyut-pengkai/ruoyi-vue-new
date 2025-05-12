package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.InspectOrderDetails;
import com.easycode.cloud.domain.dto.InspectOrderDto;
import com.easycode.cloud.domain.vo.InspectOrderDetailsVo;
import com.weifu.cloud.domain.dto.StockInOrderCommonDto;
import com.weifu.cloud.domain.vo.InspectInfoVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 送检单明细Mapper接口
 *
 * @author weifu
 * @date 2023-03-29
 */
@Repository
public interface InspectOrderDetailsMapper
{
    /**
     * 查询送检单明细
     *
     * @param id 送检单明细主键
     * @return 送检单明细
     */
    public InspectOrderDetails selectInspectOrderDetailsById(Long id);

    /**
     * 查询送检单明细列表
     *
     * @param inspectOrderDetails 送检单明细
     * @return 送检单明细集合
     */
    public List<InspectOrderDetailsVo> selectInspectOrderDetailsList(InspectOrderDetails inspectOrderDetails);

    /**
     * 查询送检单明细列表
     *
     * @param ids 送检单明细
     * @return 送检单明细集合
     */
    public List<InspectInfoVo> getPrintInfoByIds(Long[] ids);

    /**
     * 查询送检单明细列表
     *
     * @param stock 包含物料代码list 送检单状态list及检验结果
     * @return 物料待检数量集合
     */
    public List<StockInOrderCommonDto> queryWaitInspectNumGroupByMaterialNo(StockInOrderCommonDto stock);

    /**
     * 新增送检单明细
     *
     * @param inspectOrderDetails 送检单明细
     * @return 结果
     */
    public int insertInspectOrderDetails(InspectOrderDetails inspectOrderDetails);

    /**
     * 修改送检单明细
     *
     * @param inspectOrderDetails 送检单明细
     * @return 结果
     */
    public int updateInspectOrderDetails(InspectOrderDetails inspectOrderDetails);

    /**
     * 删除送检单明细
     *
     * @param id 送检单明细主键
     * @return 结果
     */
    public int deleteInspectOrderDetailsById(Long id);

    /**
     * 批量删除送检单明细
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteInspectOrderDetailsByIds(Long[] ids);

    /**
     * 批量录入检验结果
     *
     * @param inspectOrderDto 送检单
     * @return 结果
     */
    public int updateInspectOrderDetailsBatch(InspectOrderDto inspectOrderDto);

    /**
     * 查询送检单明细列表（by送检单号集合）
     *
     * @param inspectOrderDto 送检单
     * @return 送检单明细集合
     */
    public List<InspectOrderDetailsVo> selectInspectOrderDetailsListByOrderNos(InspectOrderDto inspectOrderDto);

    /**
     * 查询送检单明细列表（by送检单号集合）
     *
     * @param orderNo 送检单
     * @return 送检单明细集合
     */
    public List<InspectOrderDetailsVo> selectInspectOrderDetailsListByOrderNo(String orderNo);
}

