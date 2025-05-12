package com.easycode.cloud.service;

import com.easycode.cloud.domain.InspectOrderDetails;
import com.easycode.cloud.domain.dto.InspectOrderDto;
import com.easycode.cloud.domain.vo.InspectOrderDetailsListVo;
import com.easycode.cloud.domain.vo.InspectOrderDetailsVo;
import com.weifu.cloud.domain.dto.StockInOrderCommonDto;
import com.weifu.cloud.domain.vo.InspectInfoVo;

import java.util.List;

/**
 * 送检单明细Service接口
 *
 * @author weifu
 * @date 2023-03-29
 */
public interface IInspectOrderDetailsService
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
     * 查询送检单明细打印信息
     *
     * @param inspectInfoVo 送检单明细
     * @return 送检单明细集合
     */
    public List<InspectInfoVo> getPrintInfoByIds(InspectInfoVo inspectInfoVo);

    /**
     * 新增送检单明细
     *
     * @param inspectOrderDetails 送检单明细
     * @return 结果
     */
    public int insertInspectOrderDetails(InspectOrderDetails inspectOrderDetails);

    /**
     * 根据物料list、单据状态list及检验结果查询待检数量
     * @param stock 检验单信息
     * @Date 2024/05/13
     * @Author fsc
     * @return 物料待检数量集合
     */
    List<StockInOrderCommonDto> queryWaitInspectNumGroupByMaterialNo(StockInOrderCommonDto stock);

    /**
     * 修改送检单明细
     *
     * @param inspectOrderDetails 送检单明细
     * @return 结果
     */
    public int updateInspectOrderDetails(InspectOrderDetails inspectOrderDetails);
    /**
     * 批量修改送检单明细
     *
     * @param detailsList 送检单明细
     * @return 结果
     */
    public int updateDetailsByList(InspectOrderDetailsListVo detailsList) throws Exception;

    /**
     * 批量删除送检单明细
     *
     * @param ids 需要删除的送检单明细主键集合
     * @return 结果
     */
    public int deleteInspectOrderDetailsByIds(Long[] ids);

    /**
     * 删除送检单明细信息
     *
     * @param id 送检单明细主键
     * @return 结果
     */
    public int deleteInspectOrderDetailsById(Long id);

    /**
     * 批量释放送检单
     * @param inspectOrderDto 送检单dto
     * @return 未成功的送检单号
     */
    InspectOrderDto releaseBatchInspectOrderDetails(InspectOrderDto inspectOrderDto);


}
