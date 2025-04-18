package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.UserSubscriptions;
import com.ruoyi.xkt.dto.dailyStoreTag.DailyStoreTagDTO;
import com.ruoyi.xkt.dto.userSubscriptions.UserSubscPageResDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户关注u档口Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface UserSubscriptionsMapper extends BaseMapper<UserSubscriptions> {

    /**
     * 获取用户关注列表
     *
     * @param userId    用户ID
     * @param storeName 档口名称
     * @return
     */
    List<UserSubscPageResDTO> selectUserSubscPage(@Param("userId") Long userId, @Param("storeName") String storeName);

    /**
     * 获取档口关注前10
     * @return
     */
    List<DailyStoreTagDTO> selectTop10List();

}
