package com.easycode.cloud.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.easycode.cloud.domain.vo.StockInDetailPrintVo;
import com.weifu.cloud.common.core.exception.ServiceException;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.easycode.cloud.domain.StockInOtherDetail;
import com.weifu.cloud.domain.vo.PrintInfoVo;
import com.weifu.cloud.domian.dto.WmsMaterialAttrParamsDto;
import com.weifu.cloud.domian.dto.WmsMaterialBasicDto;
import com.easycode.cloud.mapper.StockInOtherDetailMapper;
import com.weifu.cloud.service.IMainDataService;
import com.easycode.cloud.service.IStockInOtherDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 其它入库明细Service业务层处理
 *
 * @author bcp
 * @date 2023-03-24
 */
@Service
public class StockInOtherDetailServiceImpl implements IStockInOtherDetailService
{
    @Autowired
    private StockInOtherDetailMapper stockInOtherDetailMapper;

    @Autowired
    private IMainDataService iMainDataService;

    /**
     * 查询其它入库明细
     *
     * @param id 其它入库明细主键
     * @return 其它入库明细
     */
    @Override
    public StockInOtherDetail selectStockInOtherDetailById(Long id)
    {
        return stockInOtherDetailMapper.selectStockInOtherDetailById(id);
    }

    /**
     * 查询其它入库明细列表
     *
     * @param stockInOtherDetail 其它入库明细
     * @return 其它入库明细
     */
    @Override
    public List<StockInOtherDetail> selectStockInOtherDetailList(StockInOtherDetail stockInOtherDetail)
    {
        return stockInOtherDetailMapper.selectStockInOtherDetailList(stockInOtherDetail);
    }

    /**
     * 查询其它入库明细列表
     *
     * @param printInfo 其它入库明细
     * @return 其它入库明细
     */
    @Override
    public List<PrintInfoVo> getPrintInfoByIds(PrintInfoVo printInfo)
    {
        Long[] ids = printInfo.getIds();
        List<PrintInfoVo> printInfoVos = stockInOtherDetailMapper.getPrintInfoByIds(ids);

        // 获取物料号
        List<String> materialNoList = printInfoVos.stream().map(PrintInfoVo::getMaterialNo).collect(Collectors.toList());
        // 根据物料查询对应物料类型
        WmsMaterialBasicDto materialBasicDto = new WmsMaterialBasicDto();
        materialBasicDto.setMaterialNoList(materialNoList);
        AjaxResult ajaxResult = iMainDataService.getMaterialArrInfo(materialBasicDto);
        if (ajaxResult.isError()) {
            throw new ServiceException(ajaxResult.get("msg").toString());
        }
        List<WmsMaterialAttrParamsDto> list = JSONObject.parseArray(ajaxResult.get("data").toString(), WmsMaterialAttrParamsDto.class);
        if (ObjectUtils.isEmpty(list)) {
            throw new ServiceException("物料属性表中未查询到物料相关信息！");
        }
        if (list.size() > 0 && !Optional.ofNullable(list.get(0).getType()).isPresent()){
            throw new ServiceException("物料属性未维护物料类型！");
        }

        Map<String, String> materialInfoMap = list.stream().collect(Collectors.toMap(WmsMaterialAttrParamsDto::getMaterialNo, WmsMaterialAttrParamsDto::getType));

        for (PrintInfoVo printInfoVo : printInfoVos){
            String materialNo = printInfoVo.getMaterialNo();
            String lotNo = printInfoVo.getLotNo();
            Integer deliverQty = printInfoVo.getDeliverQty().intValue();
            String materialType = materialInfoMap.get(materialNo);
            printInfoVo.setMaterialType(materialType);
            String qrCode = String.format("O%s%%D%s%%M%s%%Q%s%%B%s%%V%s%%L%s%%X1/1", "", "", materialNo,
                    deliverQty, lotNo, "", "");
            printInfoVo.setQrCode(qrCode);
        }
        return printInfoVos;
    }

    /**
     * 新增其它入库明细
     *
     * @param stockInOtherDetail 其它入库明细
     * @return 结果
     */
    @Override
    public int insertStockInOtherDetail(StockInOtherDetail stockInOtherDetail)
    {
        stockInOtherDetail.setCreateTime(DateUtils.getNowDate());
        return stockInOtherDetailMapper.insertStockInOtherDetail(stockInOtherDetail);
    }

    /**
     * 修改其它入库明细
     *
     * @param stockInOtherDetail 其它入库明细
     * @return 结果
     */
    @Override
    public int updateStockInOtherDetail(StockInOtherDetail stockInOtherDetail)
    {
        stockInOtherDetail.setUpdateTime(DateUtils.getNowDate());
        return stockInOtherDetailMapper.updateStockInOtherDetail(stockInOtherDetail);
    }

    /**
     * 批量删除其它入库明细
     *
     * @param ids 需要删除的其它入库明细主键
     * @return 结果
     */
    @Override
    public int deleteStockInOtherDetailByIds(Long[] ids)
    {
        return stockInOtherDetailMapper.deleteStockInOtherDetailByIds(ids);
    }

    /**
     * 删除其它入库明细信息
     *
     * @param id 其它入库明细主键
     * @return 结果
     */
    @Override
    public int deleteStockInOtherDetailById(Long id)
    {
        return stockInOtherDetailMapper.deleteStockInOtherDetailById(id);
    }

    public StockInDetailPrintVo queryStockInOtherDetailByTaskNo(String taskNo)
    {
        return stockInOtherDetailMapper.queryStockInOtherDetailByTaskNo(taskNo);
    }


}
