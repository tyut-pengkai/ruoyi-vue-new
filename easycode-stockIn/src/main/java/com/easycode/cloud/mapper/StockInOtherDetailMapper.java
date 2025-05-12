package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.StockInOtherDetail;
import com.easycode.cloud.domain.dto.StockinOtherDetailDto;
import com.easycode.cloud.domain.vo.StockInDetailPrintVo;
import com.weifu.cloud.domain.vo.PrintInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 其它入库明细Mapper接口
 *
 * @author bcp
 * @date 2023-03-24
 */
@Repository
public interface StockInOtherDetailMapper
{
    /**
     * 查询其它入库明细
     *
     * @param id 其它入库明细主键
     * @return 其它入库明细
     */
    public StockInOtherDetail selectStockInOtherDetailById(Long id);

    /**
     * 查询其它入库明细列表
     *
     * @param stockInOtherDetail 其它入库明细
     * @return 其它入库明细集合
     */
    public List<StockInOtherDetail> selectStockInOtherDetailList(StockInOtherDetail stockInOtherDetail);

    /**
     * 查询其它入库明细列表
     *
     * @param ids 其它入库明细
     * @return 其它入库明细集合
     */
    public List<PrintInfoVo> getPrintInfoByIds(Long[] ids);

    /**
     * 新增其它入库明细
     *
     * @param stockInOtherDetail 其它入库明细
     * @return 结果
     */
    public int insertStockInOtherDetail(StockInOtherDetail stockInOtherDetail);

    /**
     * 修改其它入库明细
     *
     * @param stockInOtherDetail 其它入库明细
     * @return 结果
     */
    public int updateStockInOtherDetail(StockInOtherDetail stockInOtherDetail);

    /**
     * 删除其它入库明细
     *
     * @param id 其它入库明细主键
     * @return 结果
     */
    public int deleteStockInOtherDetailById(Long id);

    /**
     * 批量删除其它入库明细
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStockInOtherDetailByIds(Long[] ids);

    /**
     * 查询未完成的任务数量
     * @param orderNo 退货单据号
     * @param taskType 退货任务类型
     * @return 数量
     */
    public Integer getBeContinuedNumber(@Param("orderNo") String orderNo, @Param("taskType") String taskType);

    StockinOtherDetailDto stockInOtherOrderDetailList(Long id);

    StockinOtherDetailDto stockInOtherOrderDetailListByTaskNo(String taskNo);

    StockInDetailPrintVo queryStockInOtherDetailByTaskNo(String taskNo);
}
