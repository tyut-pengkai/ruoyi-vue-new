package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.Store;
import com.ruoyi.xkt.dto.store.StoreAppViewRankResDTO;
import com.ruoyi.xkt.dto.store.StorePageDTO;
import com.ruoyi.xkt.dto.store.StorePageResDTO;
import com.ruoyi.xkt.dto.store.StoreSimpleResDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 档口Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Repository
public interface StoreMapper extends BaseMapper<Store> {

    /**
     * 筛选档口列表
     *
     * @param pageDTO 筛选参数
     * @return 筛选结果
     */
    List<StorePageResDTO> selectStorePage(StorePageDTO pageDTO);

    /**
     * PC 商城首页 获取档口基本信息
     *
     * @param storeId 档口ID
     * @return StoreBasicResDTO
     */
    StoreSimpleResDTO getSimpleInfo(@Param("storeId") Long storeId);

    /**
     * 获取档口访问榜  过去3个月到现在
     *
     * @param threeMonthAgo 三个月前
     * @param yesterday     昨天
     * @return
     */
    List<StoreAppViewRankResDTO.SAVRViewCountDTO> selectTop10AppViewCount(@Param("threeMonthAgo") Date threeMonthAgo, @Param("yesterday") Date yesterday);

    /**
     * 将serviceAmount置为null
     *
     * @param storeId 档口ID
     */
    void updateServiceAmountNull(@Param("storeId") Long storeId);

    /**
     * 将memberAmount置为null
     *
     * @param storeId 档口ID
     */
    void updateMemberAmountNull(@Param("storeId") Long storeId);

}
