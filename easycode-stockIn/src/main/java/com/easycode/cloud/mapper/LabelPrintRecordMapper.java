package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.LabelPrintRecord;
import com.easycode.cloud.domain.vo.LabelPrintRecordVo;

import java.util.List;

/**
 * 标签打印记录Mapper接口
 * 
 * @author weifu
 * @date 2024-05-14
 */
public interface LabelPrintRecordMapper
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
     * 修改标签打印记录
     * 
     * @param labelPrintRecord 标签打印记录
     * @return 结果
     */
    public int updateLabelPrintRecord(LabelPrintRecord labelPrintRecord);

    /**
     * 删除标签打印记录
     * 
     * @param id 标签打印记录主键
     * @return 结果
     */
    public int deleteLabelPrintRecordById(Long id);

    /**
     * 批量删除标签打印记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLabelPrintRecordByIds(Long[] ids);



    /**
     * 查询生成订单号最大流水
     * @param productionOrderNo
     * @return
     */
    int getProductionMax(String productionOrderNo);
}
