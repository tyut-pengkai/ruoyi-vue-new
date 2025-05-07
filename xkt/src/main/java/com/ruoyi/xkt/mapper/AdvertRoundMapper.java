package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.AdvertRound;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 推广营销轮次Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Repository
public interface AdvertRoundMapper extends BaseMapper<AdvertRound> {

    /**
     * 判断当前档口是否超买广告位
     *
     * @param advertId 推广位ID
     * @param roundId  播放轮次ID
     * @param storeId  档口ID
     * @param position 位置
     * @return true 已超  false 未超
     */
    boolean isStallOverBuy(@Param("advertId") Long advertId, @Param("roundId") Long roundId, @Param("storeId") Long storeId, @Param("position") String position);

}
