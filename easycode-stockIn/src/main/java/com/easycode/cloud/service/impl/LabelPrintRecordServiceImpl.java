package com.easycode.cloud.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.easycode.cloud.domain.vo.LabelPrintRecordVo;
import com.weifu.cloud.common.core.exception.ServiceException;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.easycode.cloud.domain.LabelPrintRecord;
import com.weifu.cloud.domian.GoodsSourceDef;
import com.weifu.cloud.domian.dto.WmsMaterialBasicDto;
import com.easycode.cloud.mapper.LabelPrintRecordMapper;
import com.easycode.cloud.service.ILabelPrintRecordService;
import com.weifu.cloud.service.IMainDataService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 标签打印记录Service业务层处理
 * 
 * @author weifu
 * @date 2024-05-14
 */
@Service
public class LabelPrintRecordServiceImpl implements ILabelPrintRecordService
{
    @Autowired
    private LabelPrintRecordMapper labelPrintRecordMapper;

    @Autowired
    private IMainDataService mainDataService;

    /**
     * 查询标签打印记录
     * 
     * @param id 标签打印记录主键
     * @return 标签打印记录
     */
    @Override
    public LabelPrintRecord selectLabelPrintRecordById(Long id)
    {
        return labelPrintRecordMapper.selectLabelPrintRecordById(id);
    }

    /**
     * 查询标签打印记录列表
     * 
     * @param labelPrintRecordVo 标签打印记录
     * @return 标签打印记录
     */
    @Override
    public List<LabelPrintRecord> selectLabelPrintRecordList(LabelPrintRecordVo labelPrintRecordVo)
    {
        return labelPrintRecordMapper.selectLabelPrintRecordList(labelPrintRecordVo);
    }

    /**
     * 新增标签打印记录
     * 
     * @param labelPrintRecord 标签打印记录
     * @return 结果
     */
    @Override
    public int insertLabelPrintRecord(LabelPrintRecord labelPrintRecord)
    {
        labelPrintRecord.setCreateTime(DateUtils.getNowDate());
        labelPrintRecord.setPrintTime(DateUtils.getNowDate());
        //添加物料描述
        GoodsSourceDef goodsSourceDef = new GoodsSourceDef();
        goodsSourceDef.setMaterialNo(labelPrintRecord.getMaterialNo());
        String username = SecurityUtils.getUsername();
        labelPrintRecord.setCreateBy(username);
        labelPrintRecord.setOperator(username);
        labelPrintRecord.setPlantCode(SecurityUtils.getComCode());
        return labelPrintRecordMapper.insertLabelPrintRecord(labelPrintRecord);
    }

    /**
     * 批量添加
     * @param list
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addBatch(List<LabelPrintRecord> list) {
        list.forEach(labelPrintRecord ->{
            labelPrintRecord.setCreateTime(DateUtils.getNowDate());
            labelPrintRecord.setPrintTime(DateUtils.getNowDate());

            // 获取物料相关基础信息
            WmsMaterialBasicDto dto = new WmsMaterialBasicDto();
            dto.setMaterialNo(labelPrintRecord.getMaterialNo());
            AjaxResult materialBasicMap = mainDataService.getMaterialBasicMap(dto);
            if (materialBasicMap.isError()){
                throw new ServiceException("查询物料主数据失败");
            }

            JSONObject data = JSON.parseObject(JSON.parseObject(materialBasicMap.get("data").toString())
                            .getString(labelPrintRecord.getMaterialNo()).toString());
            if (ObjectUtils.isEmpty(data)) {
                throw new ServiceException(String.format("物料号:%s不存在！",
                        dto.getMaterialNo()));
            }
            labelPrintRecord.setOldMaterialNo(data.getString("oldMaterialNo"));
            labelPrintRecord.setMaterialDesc(data.getString("materialName"));

            String username = SecurityUtils.getUsername();
            labelPrintRecord.setCreateBy(username);
            labelPrintRecord.setOperator(username);
            labelPrintRecord.setPlantCode(SecurityUtils.getComCode());
            labelPrintRecordMapper.insertLabelPrintRecord(labelPrintRecord);
        });
        return 1;
    }

    /**
     * 修改标签打印记录
     * 
     * @param labelPrintRecord 标签打印记录
     * @return 结果
     */
    @Override
    public int updateLabelPrintRecord(LabelPrintRecord labelPrintRecord)
    {
        labelPrintRecord.setUpdateTime(DateUtils.getNowDate());
        return labelPrintRecordMapper.updateLabelPrintRecord(labelPrintRecord);
    }

    /**
     * 批量删除标签打印记录
     * 
     * @param ids 需要删除的标签打印记录主键
     * @return 结果
     */
    @Override
    public int deleteLabelPrintRecordByIds(Long[] ids)
    {
        return labelPrintRecordMapper.deleteLabelPrintRecordByIds(ids);
    }

    /**
     * 删除标签打印记录信息
     * 
     * @param id 标签打印记录主键
     * @return 结果
     */
    @Override
    public int deleteLabelPrintRecordById(Long id)
    {
        return labelPrintRecordMapper.deleteLabelPrintRecordById(id);
    }


    /**
     * 查询生成订单号最大流水
     * @param productionOrderNo
     * @return
     */
    @Override
    public int getProductionMax(String productionOrderNo) {
        return labelPrintRecordMapper.getProductionMax(productionOrderNo);
    }

}
