package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.UserFavorites;
import com.ruoyi.xkt.mapper.UserFavoritesMapper;
import com.ruoyi.xkt.service.IUserFavoritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户收藏商品Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class UserFavoritesServiceImpl implements IUserFavoritesService {
    @Autowired
    private UserFavoritesMapper userFavoritesMapper;

    /**
     * 查询用户收藏商品
     *
     * @param userFavoId 用户收藏商品主键
     * @return 用户收藏商品
     */
    @Override
    @Transactional(readOnly = true)
    public UserFavorites selectUserFavoritesByUserFavoId(Long userFavoId) {
        return userFavoritesMapper.selectUserFavoritesByUserFavoId(userFavoId);
    }

    /**
     * 查询用户收藏商品列表
     *
     * @param userFavorites 用户收藏商品
     * @return 用户收藏商品
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserFavorites> selectUserFavoritesList(UserFavorites userFavorites) {
        return userFavoritesMapper.selectUserFavoritesList(userFavorites);
    }

    /**
     * 新增用户收藏商品
     *
     * @param userFavorites 用户收藏商品
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUserFavorites(UserFavorites userFavorites) {
        userFavorites.setCreateTime(DateUtils.getNowDate());
        return userFavoritesMapper.insertUserFavorites(userFavorites);
    }

    /**
     * 修改用户收藏商品
     *
     * @param userFavorites 用户收藏商品
     * @return 结果
     */
    @Override
    @Transactional
    public int updateUserFavorites(UserFavorites userFavorites) {
        userFavorites.setUpdateTime(DateUtils.getNowDate());
        return userFavoritesMapper.updateUserFavorites(userFavorites);
    }

    /**
     * 批量删除用户收藏商品
     *
     * @param userFavoIds 需要删除的用户收藏商品主键
     * @return 结果
     */
    @Override
    public int deleteUserFavoritesByUserFavoIds(Long[] userFavoIds) {
        return userFavoritesMapper.deleteUserFavoritesByUserFavoIds(userFavoIds);
    }

    /**
     * 删除用户收藏商品信息
     *
     * @param userFavoId 用户收藏商品主键
     * @return 结果
     */
    @Override
    public int deleteUserFavoritesByUserFavoId(Long userFavoId) {
        return userFavoritesMapper.deleteUserFavoritesByUserFavoId(userFavoId);
    }
}
