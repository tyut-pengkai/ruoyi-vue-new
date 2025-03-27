package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.UserFavorites;

import java.util.List;

/**
 * 用户收藏商品Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface UserFavoritesMapper extends BaseMapper<UserFavorites> {
    /**
     * 查询用户收藏商品
     *
     * @param userFavoId 用户收藏商品主键
     * @return 用户收藏商品
     */
    public UserFavorites selectUserFavoritesByUserFavoId(Long userFavoId);

    /**
     * 查询用户收藏商品列表
     *
     * @param userFavorites 用户收藏商品
     * @return 用户收藏商品集合
     */
    public List<UserFavorites> selectUserFavoritesList(UserFavorites userFavorites);

    /**
     * 新增用户收藏商品
     *
     * @param userFavorites 用户收藏商品
     * @return 结果
     */
    public int insertUserFavorites(UserFavorites userFavorites);

    /**
     * 修改用户收藏商品
     *
     * @param userFavorites 用户收藏商品
     * @return 结果
     */
    public int updateUserFavorites(UserFavorites userFavorites);

    /**
     * 删除用户收藏商品
     *
     * @param userFavoId 用户收藏商品主键
     * @return 结果
     */
    public int deleteUserFavoritesByUserFavoId(Long userFavoId);

    /**
     * 批量删除用户收藏商品
     *
     * @param userFavoIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserFavoritesByUserFavoIds(Long[] userFavoIds);
}
