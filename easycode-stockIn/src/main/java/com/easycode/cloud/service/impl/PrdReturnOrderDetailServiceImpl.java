package com.easycode.cloud.service.impl;

import com.weifu.cloud.common.core.utils.DateUtils;
import com.easycode.cloud.domain.PrdReturnOrderDetail;
import com.weifu.cloud.domain.vo.PrintInfoVo;
import com.easycode.cloud.mapper.PrdReturnOrderDetailMapper;
import com.easycode.cloud.service.IPrdReturnOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 生产订单发货退货单明细Service业务层处理
 *
 * @author bcp
 * @date 2023-03-11
 */
@Service
public class PrdReturnOrderDetailServiceImpl implements IPrdReturnOrderDetailService
{
    @Autowired
    private PrdReturnOrderDetailMapper prdReturnOrderDetailMapper;

    /**
     * 查询生产订单发货退货单明细
     *
     * @param id 生产订单发货退货单明细主键
     * @return 生产订单发货退货单明细
     */
    @Override
    public PrdReturnOrderDetail selectPrdReturnOrderDetailById(Long id)
    {
        return prdReturnOrderDetailMapper.selectPrdReturnOrderDetailById(id);
    }

    /**
     * 查询生产订单发货退货单明细列表
     *
     * @param prdReturnOrderDetail 生产订单发货退货单明细
     * @return 生产订单发货退货单明细
     */
    @Override
    public List<PrdReturnOrderDetail> selectPrdReturnOrderDetailList(PrdReturnOrderDetail prdReturnOrderDetail)
    {
        return prdReturnOrderDetailMapper.selectPrdReturnOrderDetailList(prdReturnOrderDetail);
    }

    /**
     * 查询生产订单发货退货单明细列表
     *
     * @param printInfo 生产订单发货退货单明细
     * @return 生产订单发货退货单明细
     */
    @Override
    public List<PrintInfoVo> getPrintInfoByIds(PrintInfoVo printInfo)
    {
        Long[] ids = printInfo.getIds();
        List<PrintInfoVo> printInfoVos = prdReturnOrderDetailMapper.getPrintInfoByIds(ids);
        for (PrintInfoVo printInfoVo : printInfoVos){
            String materialNo = printInfoVo.getMaterialNo();
            String lotNo = printInfoVo.getLotNo();
            BigDecimal deliverQty = printInfoVo.getDeliverQty();
            String qrCode = String.format("O%s%%D%s%%M%s%%Q%s%%B%s%%V%s%%L%s%%X1/1", "", "", materialNo,
                    deliverQty,lotNo, "", "");
            printInfoVo.setQrCode(qrCode);
            printInfoVo.setBatchNo(lotNo);
            printInfoVo.setPrintDate(DateUtils.getTime());
        }
        return printInfoVos;
    }

    /**
     * 查询生产订单发货退货单明细列表
     *
     * @param prdReturnOrderDetail 生产订单发货退货单明细
     * @return 生产订单发货退货单明细
     */
    @Override
    public List<PrdReturnOrderDetail> getPrintInfoByProductNo(PrdReturnOrderDetail prdReturnOrderDetail)
    {
        return prdReturnOrderDetailMapper.selectPrdReturnOrderDetailList(prdReturnOrderDetail);
    }

    /**
     * 新增生产订单发货退货单明细
     *
     * @param prdReturnOrderDetail 生产订单发货退货单明细
     * @return 结果
     */
    @Override
    public int insertPrdReturnOrderDetail(PrdReturnOrderDetail prdReturnOrderDetail)
    {
        prdReturnOrderDetail.setCreateTime(DateUtils.getNowDate());
        return prdReturnOrderDetailMapper.insertPrdReturnOrderDetail(prdReturnOrderDetail);
    }

    /**
     * 修改生产订单发货退货单明细
     *
     * @param prdReturnOrderDetail 生产订单发货退货单明细
     * @return 结果
     */
    @Override
    public int updatePrdReturnOrderDetail(PrdReturnOrderDetail prdReturnOrderDetail)
    {
        prdReturnOrderDetail.setUpdateTime(DateUtils.getNowDate());
        return prdReturnOrderDetailMapper.updatePrdReturnOrderDetail(prdReturnOrderDetail);
    }

    /**
     * 批量删除生产订单发货退货单明细
     *
     * @param ids 需要删除的生产订单发货退货单明细主键
     * @return 结果
     */
    @Override
    public int deletePrdReturnOrderDetailByIds(Long[] ids)
    {
        return prdReturnOrderDetailMapper.deletePrdReturnOrderDetailByIds(ids);
    }

    /**
     * 删除生产订单发货退货单明细信息
     *
     * @param id 生产订单发货退货单明细主键
     * @return 结果
     */
    @Override
    public int deletePrdReturnOrderDetailById(Long id)
    {
        return prdReturnOrderDetailMapper.deletePrdReturnOrderDetailById(id);
    }
}
