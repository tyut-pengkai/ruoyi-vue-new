package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.UserFavorites;
import com.ruoyi.xkt.dto.userFavorite.UserFavoritePageDTO;
import com.ruoyi.xkt.dto.userFavorite.UserFavoritePageResDTO;

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
     * @param pageDTO 收藏列表入参
     * @return List<UserFavoritePageResDTO>
     */
    List<UserFavoritePageResDTO> selectUserFavPage(UserFavoritePageDTO pageDTO);
}
