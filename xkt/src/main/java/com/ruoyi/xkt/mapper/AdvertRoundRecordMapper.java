package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.AdvertRoundRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 推广营销轮次历史记录Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Repository
public interface AdvertRoundRecordMapper extends BaseMapper<AdvertRoundRecord> {

    /**
     * 获取档口在某个广告位的推广记录
     *
     * @param advertId    广告ID
     * @param storeId     档口ID
     * @param voucherDate 单据日期
     * @param roundIdSet  未购买的轮次ID
     * @return
     */
    List<AdvertRoundRecord> selectListByRoundId(@Param("advertId") Long advertId, @Param("storeId") Long storeId,
                                                @Param("voucherDate") Date voucherDate, @Param("roundIdSet") Set<Integer> roundIdSet);

    /**
     * 获取档口在某个广告位具体位置的推广记录
     *
     * @param advertId     广告ID
     * @param storeId      档口ID
     * @param voucherDate  单据日期
     * @param positionList 位置列表
     * @return
     */
    List<AdvertRoundRecord> selectListByPosition(@Param("advertId") Long advertId, @Param("storeId") Long storeId,
                                                 @Param("voucherDate") Date voucherDate, @Param("positionList") List<String> positionList);

}
