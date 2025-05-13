package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.AdvertRound;
import com.ruoyi.xkt.dto.adminAdvertRound.AdminAdRoundPageDTO;
import com.ruoyi.xkt.dto.adminAdvertRound.AdminAdRoundPageResDTO;
import com.ruoyi.xkt.dto.advertRound.AdvertRoundStorePageDTO;
import com.ruoyi.xkt.dto.advertRound.AdvertRoundStorePageResDTO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 推广营销轮次Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface AdvertRoundMapper extends BaseMapper<AdvertRound> {

    /**
     * 判断当前档口是否超买广告位
     *
     * @param advertId         推广位ID
     * @param roundId          播放轮次ID
     * @param storeId          档口ID
     * @param launchStatusList 播放的轮次状态
     * @return true 已超  false 未超
     */
    boolean isStallOverBuy(@Param("advertId") Long advertId, @Param("roundId") Long roundId, @Param("storeId") Long storeId,
                           @Param("launchStatusList") List<Integer> launchStatusList);

    /**
     * 获取档口已订购的推广列表
     *
     * @param pageDTO 列表查询入参
     * @return List<AdvertRoundStorePageResDTO>
     */
    List<AdvertRoundStorePageResDTO> selectStoreAdvertPage(AdvertRoundStorePageDTO pageDTO);

    /**
     * 获取档口已订购的推广列表
     *
     * @param pageDTO 列表查询入参
     * @return List<AdvertRoundStorePageResDTO>
     */
    List<AdminAdRoundPageResDTO> selectAdminAdvertPage(AdminAdRoundPageDTO pageDTO);

    /**
     * 将参数直接null
     *
     * @param advertRoundId advertRoundId
     */
    Integer updateAttrNull(Long advertRoundId);

    /**
     * 获取最受欢迎的8个推广位
     *
     * @return List<Long>
     */
    List<Long> selectMostPopulars();

    /**
     * 获取时间范围类型推广位 最高出价
     *
     * @param advertId 推广位ID
     * @param roundId  播放轮次ID
     * @return BigDecimal
     */
    BigDecimal selectMaxPayPrice(@Param("advertId") Long advertId, @Param("roundId") Integer roundId);

}

