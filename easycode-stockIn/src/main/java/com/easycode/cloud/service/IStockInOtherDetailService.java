package com.easycode.cloud.service;

import com.easycode.cloud.domain.StockInOtherDetail;
import com.easycode.cloud.domain.vo.StockInDetailPrintVo;
import com.weifu.cloud.domain.vo.PrintInfoVo;

import java.util.List;

/**
 * 其它入库明细Service接口
 *
 * @author bcp
 * @date 2023-03-24
 */
public interface IStockInOtherDetailService
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
     * 查询其它入库明细-打印信息
     *
     * @param printInfoVo 其它入库明细
     * @return 其它入库明细集合
     */
    public List<PrintInfoVo> getPrintInfoByIds(PrintInfoVo printInfoVo);

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
     * 批量删除其它入库明细
     *
     * @param ids 需要删除的其它入库明细主键集合
     * @return 结果
     */
    public int deleteStockInOtherDetailByIds(Long[] ids);

    /**
     * 删除其它入库明细信息
     *
     * @param id 其它入库明细主键
     * @return 结果
     */
    public int deleteStockInOtherDetailById(Long id);

    /**
     * 根据任务号查询其他入库明细信息
     * @param taskNo
     * @return
     */

    public StockInDetailPrintVo queryStockInOtherDetailByTaskNo(String taskNo);
}
