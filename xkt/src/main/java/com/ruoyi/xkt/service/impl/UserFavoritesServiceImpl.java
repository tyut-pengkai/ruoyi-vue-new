package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.dto.storeProdColorPrice.StoreProdMinPriceDTO;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdMainPicDTO;
import com.ruoyi.xkt.dto.userFavorite.*;
import com.ruoyi.xkt.enums.*;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.INoticeService;
import com.ruoyi.xkt.service.IUserFavoritesService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ruoyi.common.constant.Constants.ORDER_NUM_1;

/**
 * 用户收藏商品Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */

@Service
@RequiredArgsConstructor
public class UserFavoritesServiceImpl implements IUserFavoritesService {

    final UserFavoritesMapper userFavMapper;
    final StoreProductFileMapper prodFileMapper;
    final StoreProductColorSizeMapper prodColorSizeMapper;
    final ShoppingCartMapper shopCartMapper;
    final StoreProductColorMapper prodColorMapper;
    final ShoppingCartDetailMapper shopCartDetailMapper;
    final StoreMapper storeMapper;
    final INoticeService noticeService;
    final StoreProductMapper storeProdMapper;


    /**
     * 新增用户收藏商品
     *
     * @param favoriteDTO 新增收藏入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer create(UserFavoriteDTO favoriteDTO) {
        // 获取当前登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        // 筛选已经加入收藏的商品
        List<UserFavorites> existList = this.userFavMapper.selectList(new LambdaQueryWrapper<UserFavorites>()
                .eq(UserFavorites::getUserId, loginUser.getUserId())
                .in(UserFavorites::getStoreProdId, favoriteDTO.getBatchList().stream().map(UserFavoriteDTO.UFBatchVO::getStoreProdId).collect(Collectors.toList()))
                .eq(UserFavorites::getDelFlag, Constants.UNDELETED));
        // 已经加入购物车的商品
        final List<Long> existStoreProdIdList = CollectionUtils.isNotEmpty(existList)
                ? existList.stream().map(UserFavorites::getStoreProdId).collect(Collectors.toList()) : new ArrayList<>();
        List<UserFavorites> unFavList = favoriteDTO.getBatchList().stream()
                .filter(x -> CollectionUtils.isEmpty(existStoreProdIdList) || !existStoreProdIdList.contains(x.getStoreProdId()))
                .map(x -> BeanUtil.toBean(x, UserFavorites.class).setUserId(loginUser.getUserId()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(unFavList)) {
            return 0;
        }
        int count = this.userFavMapper.insert(unFavList).size();
        Map<Long, Store> storeMap = this.storeMapper.selectList(new LambdaQueryWrapper<Store>()
                        .in(Store::getId, unFavList.stream().map(UserFavorites::getStoreId).collect(Collectors.toList()))
                        .eq(Store::getDelFlag, Constants.UNDELETED)).stream()
                .collect(Collectors.toMap(Store::getId, Function.identity()));
        Map<Long, StoreProduct> storeProdMap = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                        .in(StoreProduct::getId, unFavList.stream().map(UserFavorites::getStoreProdId).collect(Collectors.toList()))
                        .eq(StoreProduct::getDelFlag, Constants.UNDELETED)).stream()
                .collect(Collectors.toMap(StoreProduct::getId, Function.identity()));
        // 新增用户收藏商品通知
        unFavList.forEach(userFav -> {
            StoreProduct storeProd = storeProdMap.get(userFav.getStoreProdId());
            Store store = storeMap.get(userFav.getStoreId());
            // 新增用户收藏商品消息通知
            this.noticeService.createSingleNotice(SecurityUtils.getUserId(), "收藏商品成功!", NoticeType.NOTICE.getValue(), NoticeOwnerType.SYSTEM.getValue(),
                    userFav.getStoreId(), UserNoticeType.FAVORITE_PRODUCT.getValue(),
                    "恭喜您，收藏" + (ObjectUtils.isNotEmpty(store) ? store.getStoreName() : "") + " 家商品: " +
                            (ObjectUtils.isNotEmpty(storeProd) ? storeProd.getProdArtNum() : "") + "成功!");
        });
        return count;
    }

    /**
     * 获取用户收藏列表
     *
     * @param pageDTO 查询入参
     * @return Page<UserFavoritePageResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserFavoritePageResDTO> page(UserFavoritePageDTO pageDTO) {
        // 获取当前登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        pageDTO.setUserId(loginUser.getUserId());
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<UserFavoritePageResDTO> favoriteList = this.userFavMapper.selectUserFavPage(pageDTO);
        if (CollectionUtils.isEmpty(favoriteList)) {
            return Page.empty(pageDTO.getPageNum(), pageDTO.getPageSize());
        }
        // 档口商品最低价格
        List<StoreProdMinPriceDTO> prodMinPriceList = this.prodColorSizeMapper.selectStoreProdMinPriceList(favoriteList.stream()
                .map(UserFavoritePageResDTO::getStoreProdId).map(String::valueOf).collect(Collectors.toList()));
        Map<Long, BigDecimal> prodMinPriceMap = CollectionUtils.isEmpty(prodMinPriceList) ? new HashMap<>()
                : prodMinPriceList.stream().collect(Collectors.toMap(StoreProdMinPriceDTO::getStoreProdId, StoreProdMinPriceDTO::getPrice));
        // 找到第一张商品主图
        List<StoreProdMainPicDTO> mainPicList = this.prodFileMapper.selectMainPicByStoreProdIdList(favoriteList.stream()
                .map(UserFavoritePageResDTO::getStoreProdId).collect(Collectors.toList()), FileType.MAIN_PIC.getValue(), ORDER_NUM_1);
        Map<Long, String> mainPicMap = CollectionUtils.isEmpty(mainPicList) ? new HashMap<>() : mainPicList.stream()
                .collect(Collectors.toMap(StoreProdMainPicDTO::getStoreProdId, StoreProdMainPicDTO::getFileUrl));
        // 商品的标准尺码
        List<StoreProductColorSize> standardSizeList = this.prodColorSizeMapper.selectList(new LambdaQueryWrapper<StoreProductColorSize>()
                .in(StoreProductColorSize::getStoreProdId, favoriteList.stream().map(UserFavoritePageResDTO::getStoreProdId).collect(Collectors.toList()))
                .eq(StoreProductColorSize::getDelFlag, Constants.UNDELETED)
                .eq(StoreProductColorSize::getStandard, ProductSizeStatus.STANDARD.getValue()));
        // 当前商品所有标准尺码
        Map<Long, String> prodStandardSizeMap = standardSizeList.stream()
                .collect(Collectors.groupingBy(StoreProductColorSize::getStoreProdId, Collectors
                        .collectingAndThen(Collectors.mapping(StoreProductColorSize::getSize, Collectors.toSet()),
                                sizes -> sizes.stream().sorted().map(String::valueOf).collect(Collectors.joining(",")))));
        favoriteList.forEach(x -> x.setStandardSize(prodStandardSizeMap.getOrDefault(x.getStoreProdId(), ""))
                .setMainPicUrl(mainPicMap.getOrDefault(x.getStoreProdId(), null))
                .setPrice(prodMinPriceMap.getOrDefault(x.getStoreProdId(), null)));
        return Page.convert(new PageInfo<>(favoriteList));
    }

    /**
     * 批量加入进货车
     *
     * @param batchDTO 批量操作入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer batchAddToShoppingCart(UserFavBatchAddToShopCartDTO batchDTO) {
        // 获取当前登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        // 判断哪些已经加入过进货车中
        List<ShoppingCart> existList = this.shopCartMapper.selectList(new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getUserId, loginUser.getUserId()).eq(ShoppingCart::getDelFlag, Constants.UNDELETED)
                .in(ShoppingCart::getStoreProdId, batchDTO.getBatchList().stream()
                        .map(UserFavBatchAddToShopCartDTO.BatchDTO::getStoreProdId).collect(Collectors.toList())));
        Map<Long, ShoppingCart> existMap = existList.stream().collect(Collectors.toMap(ShoppingCart::getStoreProdId, x -> x));
        // 有哪些还未加入到进货车
        List<UserFavBatchAddToShopCartDTO.BatchDTO> notAddList = batchDTO.getBatchList().stream().filter(x -> !existMap.containsKey(x.getStoreProdId()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(notAddList)) {
            return 0;
        }
        // 找到当前商品的颜色
        List<StoreProductColor> prodColorList = this.prodColorMapper.selectList(new LambdaQueryWrapper<StoreProductColor>()
                .in(StoreProductColor::getStoreProdId, notAddList.stream().map(UserFavBatchAddToShopCartDTO.BatchDTO::getStoreProdId).collect(Collectors.toList()))
                .in(StoreProductColor::getProdStatus, Collections.singletonList(EProductStatus.ON_SALE.getValue()))
                .eq(StoreProductColor::getDelFlag, Constants.UNDELETED));
        // 按照storeProdId分组，若商品有多个颜色则任取其一
        Map<Long, StoreProductColor> prodColorMap = prodColorList.stream().collect(Collectors.toMap(StoreProductColor::getStoreProdId, Function.identity(), (x, y) -> x));
        // 找到当前商品的标准码
        List<StoreProductColorSize> colorSizeList = this.prodColorSizeMapper.selectList(new LambdaQueryWrapper<StoreProductColorSize>()
                .in(StoreProductColorSize::getStoreProdId, notAddList.stream().map(UserFavBatchAddToShopCartDTO.BatchDTO::getStoreProdId).collect(Collectors.toList()))
                .eq(StoreProductColorSize::getDelFlag, Constants.UNDELETED)
                .eq(StoreProductColorSize::getStandard, ProductSizeStatus.STANDARD.getValue()));
        // key:storeProdId+storeColorId 并取最小的标准尺码
        Map<String, StoreProductColorSize> minSizeMap = colorSizeList.stream().collect(Collectors
                .groupingBy(x -> x.getStoreProdId().toString() + x.getStoreColorId().toString(), Collectors
                        .collectingAndThen(Collectors.toList(), x -> x.stream()
                                .min(Comparator.comparingInt(StoreProductColorSize::getSize))
                                .orElseThrow(() -> new ServiceException("当前商品没有标准尺码", HttpStatus.ERROR)))));
        notAddList.forEach(x -> {
            StoreProductColor storeProdColor = Optional.ofNullable(prodColorMap.get(x.getStoreProdId())).orElseThrow(() -> new ServiceException("当前商品没有颜色", HttpStatus.ERROR));
            StoreProductColorSize standardSize = minSizeMap.getOrDefault(x.getStoreProdId().toString() + storeProdColor.getStoreColorId().toString(), null);
            ShoppingCart shoppingCart = BeanUtil.toBean(x, ShoppingCart.class).setUserId(loginUser.getUserId());
            this.shopCartMapper.insert(shoppingCart);
            this.shopCartDetailMapper.insert(new ShoppingCartDetail() {{
                setShoppingCartId(shoppingCart.getId());
                setStoreColorId(storeProdColor.getStoreColorId());
                setColorName(storeProdColor.getColorName());
                setStoreProdColorId(storeProdColor.getId());
                setSize(ObjectUtils.isNotEmpty(standardSize) ? standardSize.getSize() : 36);
                setQuantity(1);
            }});
        });
        return notAddList.size();
    }

    /**
     * 批量取消收藏
     *
     * @param batchDTO 批量操作入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer batchDelete(UserFavBatchDeleteDTO batchDTO) {
        Long userId = SecurityUtils.getUserIdSafe();
        if (ObjectUtils.isEmpty(userId)) {
            throw new ServiceException("用户未登录，请先登录!", HttpStatus.ERROR);
        }
        // 获取当前登录用户
        List<UserFavorites> favoriteList = this.userFavMapper.selectList(new LambdaQueryWrapper<UserFavorites>()
                .eq(UserFavorites::getUserId, userId).in(UserFavorites::getStoreProdId, batchDTO.getStoreProdIdList())
                .eq(UserFavorites::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isEmpty(favoriteList)) {
            return 0;
        }
        favoriteList.forEach(x -> x.setDelFlag(Constants.DELETED));
        int count = this.userFavMapper.updateById(favoriteList).size();
        Map<Long, Store> storeMap = this.storeMapper.selectList(new LambdaQueryWrapper<Store>()
                        .in(Store::getId, favoriteList.stream().map(UserFavorites::getStoreId).collect(Collectors.toList()))
                        .eq(Store::getDelFlag, Constants.UNDELETED)).stream()
                .collect(Collectors.toMap(Store::getId, Function.identity()));
        Map<Long, StoreProduct> storeProdMap = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                        .in(StoreProduct::getId, favoriteList.stream().map(UserFavorites::getStoreProdId).collect(Collectors.toList()))
                        .eq(StoreProduct::getDelFlag, Constants.UNDELETED)).stream()
                .collect(Collectors.toMap(StoreProduct::getId, Function.identity()));
        // 新增用户取消收藏商品通知
        favoriteList.forEach(userFav -> {
            StoreProduct storeProd = storeProdMap.get(userFav.getStoreProdId());
            Store store = storeMap.get(userFav.getStoreId());
            // 新增用户取消收藏商品消息通知
            this.noticeService.createSingleNotice(SecurityUtils.getUserId(), "取消收藏商品!", NoticeType.NOTICE.getValue(), NoticeOwnerType.SYSTEM.getValue(),
                    userFav.getStoreId(), UserNoticeType.FAVORITE_PRODUCT.getValue(),
                    "您已取消收藏" + (ObjectUtils.isNotEmpty(store) ? store.getStoreName() : "") + " 家商品: " +
                            (ObjectUtils.isNotEmpty(storeProd) ? storeProd.getProdArtNum() : "") + "!");
        });
        return count;
    }

    /**
     * 获取用户收藏商品各个状态数量
     *
     * @return UserFavStatusCountResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public UserFavStatusCountResDTO getStatusNum() {
        Long userId = SecurityUtils.getUserIdSafe();
        if (ObjectUtils.isEmpty(userId)) {
            throw new ServiceException("用户未登录，请先登录!", HttpStatus.ERROR);
        }
        final Date now = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        final Date sixMonthAgo = java.sql.Date.valueOf(LocalDate.now().minusMonths(6));
        return this.userFavMapper.getStatusNum(userId, sixMonthAgo, now);
    }


}
