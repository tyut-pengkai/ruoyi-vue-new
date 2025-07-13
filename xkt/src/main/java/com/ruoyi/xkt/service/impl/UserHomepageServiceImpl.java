package com.ruoyi.xkt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.domain.ShoppingCart;
import com.ruoyi.xkt.domain.StoreOrder;
import com.ruoyi.xkt.domain.UserFavorites;
import com.ruoyi.xkt.domain.UserSubscriptions;
import com.ruoyi.xkt.dto.storeProduct.StoreProdViewDTO;
import com.ruoyi.xkt.dto.userHomepage.UserHomepageOverviewDTO;
import com.ruoyi.xkt.mapper.ShoppingCartMapper;
import com.ruoyi.xkt.mapper.StoreOrderMapper;
import com.ruoyi.xkt.mapper.UserFavoritesMapper;
import com.ruoyi.xkt.mapper.UserSubscriptionsMapper;
import com.ruoyi.xkt.service.IUserHomepageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户首页 服务层实现
 *
 * @author ruoyi
 */
@Service
@RequiredArgsConstructor
public class UserHomepageServiceImpl implements IUserHomepageService {

    final RedisCache redisCache;
    final ShoppingCartMapper shopCartMapper;
    final UserSubscriptionsMapper userSubMapper;
    final UserFavoritesMapper userFavMapper;
    final StoreOrderMapper storeOrderMapper;

    /**
     * 获取用户首页统计概览
     *
     * @return UserHomepageOverviewDTO
     */
    @Override
    @Transactional(readOnly = true)
    public UserHomepageOverviewDTO overview() {
        final Long userId = SecurityUtils.getUserIdSafe();
        if (ObjectUtils.isEmpty(userId)) {
            throw new ServiceException("用户未登录，请先登录!", HttpStatus.ERROR);
        }
        // 获取所有的图搜热款数量
        List<StoreProdViewDTO> totalSearchHotList = redisCache.getCacheObject(CacheConstants.IMG_SEARCH_PRODUCT_HOT_TOTAL);
        // 进货车数量
        List<ShoppingCart> shopCartList = this.shopCartMapper.selectList(new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getDelFlag, Constants.UNDELETED).eq(ShoppingCart::getUserId, userId));
        // 关注档口数
        List<UserSubscriptions> userSubsList = this.userSubMapper.selectList(new LambdaQueryWrapper<UserSubscriptions>()
                .eq(UserSubscriptions::getDelFlag, Constants.UNDELETED).eq(UserSubscriptions::getUserId, userId));
        // 收藏商品数
        List<UserFavorites> userFavList = this.userFavMapper.selectList(new LambdaQueryWrapper<UserFavorites>()
                .eq(UserFavorites::getDelFlag, Constants.UNDELETED).eq(UserFavorites::getUserId, userId));
        // 用户待发订单
        List<StoreOrder> storeOrderList = this.storeOrderMapper.selectList(new LambdaQueryWrapper<StoreOrder>()
                .eq(StoreOrder::getDelFlag, Constants.UNDELETED).eq(StoreOrder::getOrderUserId, userId));
        return new UserHomepageOverviewDTO()
                .setSearchHotCount(CollectionUtils.isEmpty(totalSearchHotList) ? 0 : totalSearchHotList.size())
                .setShopCartCount(CollectionUtils.isEmpty(shopCartList) ? 0 : shopCartList.size())
                .setFocusCount(CollectionUtils.isEmpty(userSubsList) ? 0 : userSubsList.size())
                .setOrderCount(CollectionUtils.isEmpty(storeOrderList) ? 0 : storeOrderList.size())
                .setFavouriteCount(CollectionUtils.isEmpty(userFavList) ? 0 : userFavList.size());
    }


}
