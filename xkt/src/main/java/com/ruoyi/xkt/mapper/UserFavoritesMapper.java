package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.UserFavorites;
import com.ruoyi.xkt.dto.dailyStoreTag.DailyStoreTagDTO;
import com.ruoyi.xkt.dto.userFavorite.UserFavoritePageDTO;
import com.ruoyi.xkt.dto.userFavorite.UserFavoritePageResDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 用户收藏商品Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface UserFavoritesMapper extends BaseMapper<UserFavorites> {

    /**
     * 查询用户收藏列表
     *
     * @param pageDTO 收藏列表入参
     * @return List<UserFavoritePageResDTO>
     */
    List<UserFavoritePageResDTO> selectUserFavPage(UserFavoritePageDTO pageDTO);

    /**
     * 获取用户收藏量前10的商品
     *
     * @param oneMonthAgo 一月前
     * @param yesterday   昨天
     * @return List<DailyStoreTagDTO>
     */
    List<DailyStoreTagDTO> searchTop10Prod(@Param("oneMonthAgo") Date oneMonthAgo, @Param("yesterday") Date yesterday);

}
