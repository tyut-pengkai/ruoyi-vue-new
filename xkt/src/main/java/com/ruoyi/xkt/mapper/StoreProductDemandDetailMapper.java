package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProductDemandDetail;
import com.ruoyi.xkt.dto.storeProductDemand.StoreProdDemandPageDTO;
import com.ruoyi.xkt.dto.storeProductDemand.StoreProdDemandPageResDTO;
import com.ruoyi.xkt.dto.storeProductDemand.StoreProdDemandSimpleDTO;
import com.ruoyi.xkt.dto.storeProductDemand.StoreProdDemandStatusCountResDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 档口商品需求单明细Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreProductDemandDetailMapper extends BaseMapper<StoreProductDemandDetail> {

    /**
     * 分页查询档口商品需求单明细
     *
     * @param pageDTO 分页查询参数
     * @return 档口商品需求单明细分页列表
     */
    List<StoreProdDemandPageResDTO> selectDemandPage(StoreProdDemandPageDTO pageDTO);

    /**
     * 根据需求code查询需求单
     *
     * @param detailIdList 明细ID集合
     * @return 需求单
     */
    List<StoreProdDemandSimpleDTO> selectDemandCodeList(@Param("detailIdList") List<Long> detailIdList);

    /**
     * 查询各状态数量
     *
     * @param storeId     档口ID
     * @param sixMonthAgo 六个月前的时间
     * @param now         当前时间
     * @return StoreProdDemandStatusCountResDTO
     */
    StoreProdDemandStatusCountResDTO getStatusNum(@Param("storeId") Long storeId,
                                                  @Param("sixMonthAgo") Date sixMonthAgo,
                                                  @Param("now") Date now);
}
