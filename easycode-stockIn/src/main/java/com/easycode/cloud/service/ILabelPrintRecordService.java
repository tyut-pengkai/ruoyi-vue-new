package com.easycode.cloud.service;

import com.easycode.cloud.domain.LabelPrintRecord;
import com.easycode.cloud.domain.vo.LabelPrintRecordVo;

import java.util.List;

/**
 * 标签打印记录Service接口
 * 
 * @author weifu
 * @date 2024-05-14
 */
public interface ILabelPrintRecordService
{
    /**
     * 查询标签打印记录
     * 
     * @param id 标签打印记录主键
     * @return 标签打印记录
     */
    public LabelPrintRecord selectLabelPrintRecordById(Long id);

    /**
     * 查询标签打印记录列表
     * 
     * @param labelPrintRecordVo 标签打印记录
     * @return 标签打印记录集合
     */
    public List<LabelPrintRecord> selectLabelPrintRecordList(LabelPrintRecordVo labelPrintRecordVo);

    /**
     * 新增标签打印记录
     * 
     * @param labelPrintRecord 标签打印记录
     * @return 结果
     */
    public int insertLabelPrintRecord(LabelPrintRecord labelPrintRecord);

    /**
     * 批量添加
     * @param list
     * @return
     */
    public int addBatch(List<LabelPrintRecord> list);

    /**
     * 修改标签打印记录
     * 
     * @param labelPrintRecord 标签打印记录
     * @return 结果
     */
    public int updateLabelPrintRecord(LabelPrintRecord labelPrintRecord);

    /**
     * 批量删除标签打印记录
     * 
     * @param ids 需要删除的标签打印记录主键集合
     * @return 结果
     */
    public int deleteLabelPrintRecordByIds(Long[] ids);

    /**
     * 删除标签打印记录信息
     * 
     * @param id 标签打印记录主键
     * @return 结果
     */
    public int deleteLabelPrintRecordById(Long id);

    /**
     * 查询生成订单号最大流水
     * @param productionOrderNo
     * @return
     */
    int getProductionMax(String productionOrderNo);

}
