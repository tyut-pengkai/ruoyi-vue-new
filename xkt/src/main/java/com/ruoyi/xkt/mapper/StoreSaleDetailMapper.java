package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreSaleDetail;
import com.ruoyi.xkt.dto.dailyStoreTag.DailyStoreTagDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 档口销售明细Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreSaleDetailMapper extends BaseMapper<StoreSaleDetail> {

    /**
     * 查询档口销售明细
     *
     * @param id 档口销售明细主键
     * @return 档口销售明细
     */
    StoreSaleDetail selectStoreSaleDetailByStoreSaleDetailId(Long id);

    /**
     * 查询档口销售明细列表
     *
     * @param storeSaleDetail 档口销售明细
     * @return 档口销售明细集合
     */
    List<StoreSaleDetail> selectStoreSaleDetailList(StoreSaleDetail storeSaleDetail);

    /**
     * 新增档口销售明细
     *
     * @param storeSaleDetail 档口销售明细
     * @return 结果
     */
    int insertStoreSaleDetail(StoreSaleDetail storeSaleDetail);

    /**
     * 修改档口销售明细
     *
     * @param storeSaleDetail 档口销售明细
     * @return 结果
     */
    int updateStoreSaleDetail(StoreSaleDetail storeSaleDetail);

    /**
     * 删除档口销售明细
     *
     * @param id 档口销售明细主键
     * @return 结果
     */
    int deleteStoreSaleDetailByStoreSaleDetailId(Long id);

    /**
     * 批量删除档口销售明细
     *
     * @param storeSaleDetailIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteStoreSaleDetailByStoreSaleDetailIds(Long[] storeSaleDetailIds);

}
