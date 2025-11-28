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
import com.ruoyi.xkt.dto.storeProductFile.StoreProdMainPicDTO;
import com.ruoyi.xkt.dto.userShoppingCart.*;
import com.ruoyi.xkt.enums.EProductStatus;
import com.ruoyi.xkt.enums.FileType;
import com.ruoyi.xkt.enums.ProductSizeStatus;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IShoppingCartService;
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

import static com.ruoyi.common.constant.Constants.*;

/**
 * 用户进货车Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements IShoppingCartService {

    final ShoppingCartMapper shopCartMapper;
    final ShoppingCartDetailMapper shopCartDetailMapper;
    final StoreProductColorSizeMapper prodColorSizeMapper;
    final StoreProductColorMapper prodColorMapper;
    final StoreProductStockMapper prodStockMapper;
    final StoreProductFileMapper prodFileMapper;
    final StoreProductCategoryAttributeMapper prodCateAttrMapper;
    final StoreMapper storeMapper;
    final StoreProductMapper storeProdMapper;


    /**
     * 用户往进货车新增商品
     *
     * @param shoppingCartDTO 新增入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer create(ShoppingCartDTO shoppingCartDTO) {
        // 判断当前登录用户角色 只有电商卖家才可操作
        LoginUser loginUser = SecurityUtils.getLoginUser();
        ShoppingCart exist = this.shopCartMapper.selectOne(new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getUserId, loginUser.getUserId()).eq(ShoppingCart::getStoreProdId, shoppingCartDTO.getStoreProdId())
                .eq(ShoppingCart::getDelFlag, Constants.UNDELETED));
        if (ObjectUtils.isNotEmpty(exist)) {
            exist.setDelFlag(DELETED);
            this.shopCartMapper.updateById(exist);
            List<ShoppingCartDetail> detailList = this.shopCartDetailMapper.selectList(new LambdaQueryWrapper<ShoppingCartDetail>()
                    .eq(ShoppingCartDetail::getShoppingCartId, exist.getId()).eq(ShoppingCartDetail::getDelFlag, Constants.UNDELETED));
            if (CollectionUtils.isNotEmpty(detailList)) {
                detailList.forEach(x -> x.setDelFlag(DELETED));
                this.shopCartDetailMapper.updateById(detailList);
            }
        }
        ShoppingCart shoppingCart = BeanUtil.toBean(shoppingCartDTO, ShoppingCart.class).setUserId(loginUser.getUserId());
        int count = this.shopCartMapper.insert(shoppingCart);
        List<ShoppingCartDetail> cartDetailList = shoppingCartDTO.getDetailList().stream().map(x -> BeanUtil.toBean(x, ShoppingCartDetail.class)
                .setShoppingCartId(shoppingCart.getId())).collect(Collectors.toList());
        this.shopCartDetailMapper.insert(cartDetailList);
        return count;
    }

    /**
     * 用户进货车列表
     *
     * @param pageDTO 查询列表入参
     * @return Page<ShopCartListResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ShopCartPageResDTO> page(ShopCartPageDTO pageDTO) {
        // 获取当前登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        pageDTO.setUserId(loginUser.getUserId());
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<ShopCartPageResDTO> shoppingCartList = this.shopCartMapper.selectShopCartPage(pageDTO);
        if (CollectionUtils.isEmpty(shoppingCartList)) {
            return Page.empty(pageDTO.getPageNum(), pageDTO.getPageSize());
        }
        // 进货单明细列表
        List<ShopCartPageDetailResDTO> detailList = this.shopCartMapper.selectDetailList(shoppingCartList.stream()
                .map(ShopCartPageResDTO::getShoppingCartId).distinct().collect(Collectors.toList()));
        Map<Long, List<ShopCartPageDetailResDTO>> detailMap = detailList.stream().collect(Collectors.groupingBy(ShopCartPageDetailResDTO::getShoppingCartId));
        shoppingCartList.forEach(x -> x.setDetailList(detailMap.getOrDefault(x.getShoppingCartId(), new ArrayList<>())));
        // 查找排名第一个商品主图列表
        List<StoreProdMainPicDTO> mainPicList = this.prodFileMapper.selectMainPicByStoreProdIdList(shoppingCartList.stream()
                .map(ShopCartPageResDTO::getStoreProdId).collect(Collectors.toList()), FileType.MAIN_PIC.getValue(), ORDER_NUM_1);
        Map<Long, String> mainPicMap = CollectionUtils.isEmpty(mainPicList) ? new HashMap<>() : mainPicList.stream()
                .collect(Collectors.toMap(StoreProdMainPicDTO::getStoreProdId, StoreProdMainPicDTO::getFileUrl));
        // 获取商品价格尺码
        List<StoreProductColorSize> priceSizeList = this.prodColorSizeMapper.selectList(new LambdaQueryWrapper<StoreProductColorSize>()
                .in(StoreProductColorSize::getStoreProdId, shoppingCartList.stream().map(ShopCartPageResDTO::getStoreProdId).collect(Collectors.toList()))
                .in(StoreProductColorSize::getStoreColorId, detailList.stream().map(ShopCartPageDetailResDTO::getStoreColorId).collect(Collectors.toList()))
                .eq(StoreProductColorSize::getDelFlag, Constants.UNDELETED));
        // 商品价格尺码map
        Map<String, StoreProductColorSize> priceSizeMap = priceSizeList.stream().collect(Collectors
                .toMap(x -> x.getStoreProdId().toString() + x.getStoreColorId().toString() + x.getSize(), x -> x));
        // 当前商品所有标准尺码
        Map<Long, String> prodStandardSizeMap = priceSizeList.stream()
                .filter(x -> Objects.equals(x.getStandard(), ProductSizeStatus.STANDARD.getValue()))
                .collect(Collectors.groupingBy(StoreProductColorSize::getStoreProdId, Collectors
                        .collectingAndThen(Collectors.mapping(StoreProductColorSize::getSize, Collectors.toSet()),
                                sizes -> sizes.stream().sorted().map(String::valueOf).collect(Collectors.joining(",")))));
        shoppingCartList.forEach(x -> {
            x.setMainPicUrl(mainPicMap.getOrDefault(x.getStoreProdId(), null));
            x.setStandardSize(prodStandardSizeMap.getOrDefault(x.getStoreProdId(), ""));
            x.getDetailList()
                    .forEach(detail -> {
                        StoreProductColorSize prodColorSize = priceSizeMap.get(x.getStoreProdId().toString() + detail.getStoreColorId().toString() + detail.getSize());
                        detail.setAmount((ObjectUtils.isNotEmpty(prodColorSize)
                                        ? prodColorSize.getPrice() : BigDecimal.ZERO).multiply(new BigDecimal(detail.getQuantity())))
                                .setPrice(ObjectUtils.isNotEmpty(prodColorSize) ? prodColorSize.getPrice() : null)
                                .setStoreProdColorSizeId(ObjectUtils.isNotEmpty(prodColorSize) ? prodColorSize.getId() : null);
                    });
        });
        return Page.convert(new PageInfo<>(shoppingCartList));
    }

    /**
     * 根据购物车ID 获取购物车详情
     *
     * @param shoppingCartId 购物车ID
     * @return ShopCartDetailResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public ShopCartDetailResDTO getEditInfo(Long shoppingCartId) {
        // 获取当前登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        ShoppingCart shoppingCart = Optional.ofNullable(this.shopCartMapper.selectOne(new LambdaQueryWrapper<ShoppingCart>()
                        .eq(ShoppingCart::getId, shoppingCartId).eq(ShoppingCart::getDelFlag, Constants.UNDELETED)
                        .eq(ShoppingCart::getUserId, loginUser.getUserId())))
                .orElseThrow(() -> new ServiceException("用户购物车不存在!", HttpStatus.ERROR));
        // 获取进货车明细
        List<ShoppingCartDetail> detailList = this.shopCartDetailMapper.selectList(new LambdaQueryWrapper<ShoppingCartDetail>()
                .eq(ShoppingCartDetail::getShoppingCartId, shoppingCartId).eq(ShoppingCartDetail::getDelFlag, Constants.UNDELETED));
        // 获取标准尺码
        List<StoreProductColorSize> standardSizeList = this.prodColorSizeMapper.selectList(new LambdaQueryWrapper<StoreProductColorSize>()
                .eq(StoreProductColorSize::getStoreProdId, shoppingCart.getStoreProdId()).eq(StoreProductColorSize::getDelFlag, Constants.UNDELETED)
                .eq(StoreProductColorSize::getStandard, ProductSizeStatus.STANDARD.getValue()));
        // 标准尺码
        List<Integer> standardList = standardSizeList.stream().map(StoreProductColorSize::getSize).distinct().sorted(Comparator.comparing(x -> x)).collect(Collectors.toList());
        // key storeColorId:size value price
        Map<String, BigDecimal> standardSizePriceMap = standardSizeList.stream().collect(Collectors.toMap(x -> x.getStoreColorId() + ":" + x.getSize(), StoreProductColorSize::getPrice));
        // 获取商品颜色列表
        List<StoreProductColor> colorList = this.prodColorMapper.selectList(new LambdaQueryWrapper<StoreProductColor>()
                .eq(StoreProductColor::getStoreProdId, shoppingCart.getStoreProdId()).eq(StoreProductColor::getDelFlag, Constants.UNDELETED)
                .in(StoreProductColor::getProdStatus, Arrays.asList(EProductStatus.ON_SALE.getValue(), EProductStatus.TAIL_GOODS.getValue())));
        // 根据标准尺码去找对应尺码的库存数量
        List<StoreProductStock> prodStockList = this.prodStockMapper.selectList(new LambdaQueryWrapper<StoreProductStock>()
                .eq(StoreProductStock::getStoreProdId, shoppingCart.getStoreProdId())
                .in(StoreProductStock::getStoreProdColorId, colorList.stream().map(StoreProductColor::getId).distinct().collect(Collectors.toList()))
                .eq(StoreProductStock::getDelFlag, Constants.UNDELETED));
        // 获取档口颜色尺码的库存数量
        Map<String, List<ShopCartDetailResDTO.SCDStoreProdSizeStockDTO>> colorSizeStockMap = this.convertSizeStock(prodStockList, standardList, standardSizePriceMap);
        // 库存数量为0默认值
        List<ShopCartDetailResDTO.SCDStoreProdSizeStockDTO> defaultZeroStockList = standardList.stream().map(size ->
                new ShopCartDetailResDTO.SCDStoreProdSizeStockDTO().setSize(size).setStock(0)).collect(Collectors.toList());
        List<ShopCartDetailResDTO.SCDStoreProdColorDTO> colorSizeStockList = colorList.stream()
                .map(color -> BeanUtil.toBean(color, ShopCartDetailResDTO.SCDStoreProdColorDTO.class).setStoreProdColorId(color.getId())
                        // 设定尺码对应的库存及价格
                        .setSizeStockList(colorSizeStockMap
                                .getOrDefault(color.getStoreProdId().toString() + ":" + color.getStoreColorId().toString(), defaultZeroStockList)))
                .collect(Collectors.toList());
        return new ShopCartDetailResDTO() {{
            setProdArtNum(shoppingCart.getProdArtNum()).setStoreProdId(shoppingCart.getStoreProdId())
                    .setStandardSizeList(standardList).setColorList(colorSizeStockList)
                    .setDetailList(BeanUtil.copyToList(detailList, ShopCartDetailResDTO.SCDDetailDTO.class));
        }};
    }

    /**
     * 用户编辑进货车商品
     *
     * @param editDTO 编辑进货车商品入参
     * @return
     */
    @Override
    @Transactional
    public Integer update(ShoppingCartEditDTO editDTO) {
        // 获取当前用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        ShoppingCart shoppingCart = Optional.ofNullable(this.shopCartMapper.selectOne(new LambdaQueryWrapper<ShoppingCart>()
                        .eq(ShoppingCart::getId, editDTO.getShoppingCartId()).eq(ShoppingCart::getDelFlag, Constants.UNDELETED)
                        .eq(ShoppingCart::getUserId, loginUser.getUserId())))
                .orElseThrow(() -> new ServiceException("用户购物车不存在!", HttpStatus.ERROR));
        // 找到当前购物车明细
        List<ShoppingCartDetail> oldDetailList = this.shopCartDetailMapper.selectList(new LambdaQueryWrapper<ShoppingCartDetail>()
                .eq(ShoppingCartDetail::getShoppingCartId, editDTO.getShoppingCartId()).eq(ShoppingCartDetail::getDelFlag, Constants.UNDELETED));
        oldDetailList.forEach(x -> x.setDelFlag(Constants.DELETED));
        this.shopCartDetailMapper.updateById(oldDetailList);
        // 再新增购物车明细
        List<ShoppingCartDetail> cartDetailList = editDTO.getDetailList().stream().map(x -> BeanUtil.toBean(x, ShoppingCartDetail.class)
                .setShoppingCartId(shoppingCart.getId())).collect(Collectors.toList());
        this.shopCartDetailMapper.insert(cartDetailList);
        return 1;
    }

    /**
     * 用户删除进货车商品
     *
     * @param deleteDTO 进货车ID删除列表
     * @return
     */
    @Override
    @Transactional
    public Integer delete(ShopCartDeleteDTO deleteDTO) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        List<ShoppingCart> shopCartList = Optional.ofNullable(this.shopCartMapper.selectList(new LambdaQueryWrapper<ShoppingCart>()
                        .in(ShoppingCart::getId, deleteDTO.getShoppingCartIdList()).eq(ShoppingCart::getDelFlag, Constants.UNDELETED)
                        .eq(ShoppingCart::getUserId, loginUser.getUserId())))
                .orElseThrow(() -> new ServiceException("用户购物车不存在!", HttpStatus.ERROR));
        shopCartList.forEach(x -> x.setDelFlag(DELETED));
        int count = this.shopCartMapper.updateById(shopCartList).size();
        // 找到进货车明细
        List<ShoppingCartDetail> detailList = this.shopCartDetailMapper.selectList(new LambdaQueryWrapper<ShoppingCartDetail>()
                .in(ShoppingCartDetail::getShoppingCartId, deleteDTO.getShoppingCartIdList()).eq(ShoppingCartDetail::getDelFlag, Constants.UNDELETED));
        detailList.forEach(x -> x.setDelFlag(Constants.DELETED));
        this.shopCartDetailMapper.updateById(detailList);
        return count;
    }

    /**
     * 根据storeProdid获取进货车详情
     *
     * @param listDTO 商品ID列表
     * @return ShoppingCartDTO
     */
    @Override
    @Transactional(readOnly = true)
    public List<ShoppingCartDTO> getList(ShopCartListDTO listDTO) {
        Long userId = SecurityUtils.getUserIdSafe();
        if (ObjectUtils.isEmpty(userId)) {
            return Collections.emptyList();
        }
        List<ShoppingCart> shoppingCartList = this.shopCartMapper.selectList(new LambdaQueryWrapper<ShoppingCart>()
                .in(ShoppingCart::getStoreProdId, listDTO.getStoreProdIdList()).eq(ShoppingCart::getDelFlag, Constants.UNDELETED)
                .eq(ShoppingCart::getUserId, userId));
        if (CollectionUtils.isEmpty(shoppingCartList)) {
            return new ArrayList<>();
        }
        List<Store> storeList = this.storeMapper.selectList(new LambdaQueryWrapper<Store>().eq(Store::getDelFlag, Constants.UNDELETED)
                .in(Store::getId, shoppingCartList.stream().map(ShoppingCart::getStoreId).collect(Collectors.toList())));
        Map<Long, Store> storeMap = storeList.stream().collect(Collectors.toMap(Store::getId, Function.identity()));
        List<ShoppingCartDetail> detailList = this.shopCartDetailMapper.selectList(new LambdaQueryWrapper<ShoppingCartDetail>()
                .in(ShoppingCartDetail::getShoppingCartId, shoppingCartList.stream().map(ShoppingCart::getId).collect(Collectors.toList()))
                .eq(ShoppingCartDetail::getDelFlag, Constants.UNDELETED));
        Map<Long, List<ShoppingCartDetail>> detailMap = detailList.stream().collect(Collectors.groupingBy(ShoppingCartDetail::getShoppingCartId));
        // 商品第一张主图
        List<StoreProdMainPicDTO> mainPicList = this.prodFileMapper.selectMainPicByStoreProdIdList(listDTO.getStoreProdIdList(), FileType.MAIN_PIC.getValue(), ORDER_NUM_1);
        Map<Long, String> mainPicMap = CollectionUtils.isEmpty(mainPicList) ? new HashMap<>() : mainPicList.stream()
                .collect(Collectors.toMap(StoreProdMainPicDTO::getStoreProdId, StoreProdMainPicDTO::getFileUrl));
        // 获取商品价格尺码
        List<StoreProductColorSize> priceSizeList = this.prodColorSizeMapper.selectList(new LambdaQueryWrapper<StoreProductColorSize>()
                .in(StoreProductColorSize::getStoreProdId, shoppingCartList.stream().map(ShoppingCart::getStoreProdId).collect(Collectors.toList()))
                .in(StoreProductColorSize::getStoreColorId, detailList.stream().map(ShoppingCartDetail::getStoreColorId).collect(Collectors.toList()))
                .eq(StoreProductColorSize::getDelFlag, Constants.UNDELETED));
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .in(StoreProduct::getId, shoppingCartList.stream().map(ShoppingCart::getStoreProdId).collect(Collectors.toList()))
                .eq(StoreProduct::getDelFlag, Constants.UNDELETED));
        Map<Long, StoreProduct> storeProdMap = storeProdList.stream().collect(Collectors.toMap(StoreProduct::getId, Function.identity()));
        // 商品价格尺码map
        Map<String, StoreProductColorSize> priceSizeMap = priceSizeList.stream().collect(Collectors
                .toMap(x -> x.getStoreProdId().toString() + x.getStoreColorId().toString() + x.getSize(), x -> x));
        return shoppingCartList.stream().map(x -> {
            ShoppingCartDTO shopCartDTO = BeanUtil.toBean(x, ShoppingCartDTO.class).setMainPicUrl(mainPicMap.get(x.getStoreProdId()))
                    .setProdTitle(storeProdMap.containsKey(x.getStoreProdId()) ? storeProdMap.get(x.getStoreProdId()).getProdTitle() : "")
                    // 默认发货时效为3天
                    .setDeliveryTime(storeProdMap.containsKey(x.getStoreProdId()) ? storeProdMap.get(x.getStoreProdId()).getDeliveryTime() : 3)
                    .setStoreName(storeMap.containsKey(x.getStoreId()) ? storeMap.get(x.getStoreId()).getStoreName() : "");
            List<ShoppingCartDTO.SCDetailDTO> shopCartDetailList = detailMap.get(x.getId()).stream().map(detail -> {
                final StoreProductColorSize prodColorSize = priceSizeMap.get(x.getStoreProdId().toString() + detail.getStoreColorId().toString() + detail.getSize());
                return ObjectUtils.isEmpty(prodColorSize) ? null : BeanUtil.toBean(detail, ShoppingCartDTO.SCDetailDTO.class)
                        .setPrice(prodColorSize.getPrice()).setStoreProdColorSizeId(prodColorSize.getId())
                        .setAmount(prodColorSize.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity())));
            }).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
            return shopCartDTO.setDetailList(shopCartDetailList);
        }).collect(Collectors.toList());
    }

    /**
     * 获取用户进货车各状态数量
     *
     * @return ShopCartStatusCountResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public ShopCartStatusCountResDTO getStatusNum() {
        final Date now = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        final Date sixMonthAgo = java.sql.Date.valueOf(LocalDate.now().minusMonths(6));
        return this.shopCartMapper.getStatusNum(SecurityUtils.getUserId(), sixMonthAgo, now);
    }

    /**
     * 用户更新进货车明细数量
     *
     * @param updateQuantityDTO 更新入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer updateDetailQuantity(ShopCartDetailQuantityUpdateDTO updateQuantityDTO) {
        // 数量不能小于等于0
        if (0 >= updateQuantityDTO.getQuantity()) {
            throw new ServiceException("数量不能小于等于0!", HttpStatus.ERROR);
        }
        ShoppingCartDetail detail = Optional.ofNullable(this.shopCartDetailMapper.selectOne(new LambdaQueryWrapper<ShoppingCartDetail>()
                        .eq(ShoppingCartDetail::getId, updateQuantityDTO.getShoppingCartDetailId()).eq(ShoppingCartDetail::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("进货车明细不存在!", HttpStatus.ERROR));
        detail.setQuantity(updateQuantityDTO.getQuantity());
        return this.shopCartDetailMapper.updateById(detail);
    }

    /**
     * 下单后，删除用户进货车商品
     *
     * @param storeProdId 档口商品ID
     * @param userId      用户ID
     * @return Integer
     */
    @Override
    @Transactional
    public Integer removeShoppingCart(Long storeProdId, Long userId) {
        // 用户进货车是否存在该商品
        ShoppingCart shoppingCart = this.shopCartMapper.selectOne(new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getStoreProdId, storeProdId).eq(ShoppingCart::getUserId, userId)
                .eq(ShoppingCart::getDelFlag, Constants.UNDELETED));
        if (ObjectUtils.isEmpty(shoppingCart)) {
            return 0;
        }
        shoppingCart.setDelFlag(DELETED);
        int count = this.shopCartMapper.updateById(shoppingCart);
        // 找到进货车明细
        List<ShoppingCartDetail> detailList = this.shopCartDetailMapper.selectList(new LambdaQueryWrapper<ShoppingCartDetail>()
                .eq(ShoppingCartDetail::getShoppingCartId, shoppingCart.getId()).eq(ShoppingCartDetail::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isNotEmpty(detailList)) {
            detailList.forEach(detail -> detail.setDelFlag(DELETED));
            this.shopCartDetailMapper.updateById(detailList);
        }
        return count;
    }

    /**
     * 获取档口商品颜色尺码的库存
     *
     * @param stockList            库存数量
     * @param standardSizeList     当前商品的标准尺码
     * @param standardSizePriceMap 颜色尺码对应的价格
     * @return Map<Long, Map < Integer, Integer>>
     */
    private Map<String, List<ShopCartDetailResDTO.SCDStoreProdSizeStockDTO>> convertSizeStock(List<StoreProductStock> stockList, List<Integer> standardSizeList,
                                                                                              Map<String, BigDecimal> standardSizePriceMap) {
        Map<String, List<ShopCartDetailResDTO.SCDStoreProdSizeStockDTO>> colorSizeStockMap = new HashMap<>();
        if (CollectionUtils.isEmpty(stockList)) {
            return colorSizeStockMap;
        }
        // 标准尺码map
        Map<Integer, Integer> standardSizeMap = standardSizeList.stream().collect(Collectors.toMap(x -> x, x -> x));
        Map<String, StoreProductStock> colorStockMap = stockList.stream().collect(Collectors.toMap(x -> x.getStoreProdId().toString() + ":" + x.getStoreColorId().toString(), x -> x));
        colorStockMap.forEach((unionId, colorStock) -> {
            List<ShopCartDetailResDTO.SCDStoreProdSizeStockDTO> sizeStockList = new ArrayList<>();
            if (standardSizeMap.containsKey(SIZE_30)) {
                sizeStockList.add(new ShopCartDetailResDTO.SCDStoreProdSizeStockDTO().setSize(SIZE_30)
                        .setStock(ObjectUtils.defaultIfNull(colorStock.getSize30(), 0))
                        .setPrice(standardSizePriceMap.get(colorStock.getStoreColorId().toString() + ":" + SIZE_30)));
            }
            if (standardSizeMap.containsKey(SIZE_31)) {
                sizeStockList.add(new ShopCartDetailResDTO.SCDStoreProdSizeStockDTO().setSize(SIZE_31)
                        .setStock(ObjectUtils.defaultIfNull(colorStock.getSize31(), 0))
                        .setPrice(standardSizePriceMap.get(colorStock.getStoreColorId().toString() + ":" + SIZE_31)));
            }
            if (standardSizeMap.containsKey(SIZE_32)) {
                sizeStockList.add(new ShopCartDetailResDTO.SCDStoreProdSizeStockDTO().setSize(SIZE_32)
                        .setStock(ObjectUtils.defaultIfNull(colorStock.getSize32(), 0))
                        .setPrice(standardSizePriceMap.get(colorStock.getStoreColorId().toString() + ":" + SIZE_32)));
            }
            if (standardSizeMap.containsKey(SIZE_33)) {
                sizeStockList.add(new ShopCartDetailResDTO.SCDStoreProdSizeStockDTO().setSize(SIZE_33)
                        .setStock(ObjectUtils.defaultIfNull(colorStock.getSize33(), 0))
                        .setPrice(standardSizePriceMap.get(colorStock.getStoreColorId().toString() + ":" + SIZE_33)));
            }
            if (standardSizeMap.containsKey(SIZE_34)) {
                sizeStockList.add(new ShopCartDetailResDTO.SCDStoreProdSizeStockDTO().setSize(SIZE_34)
                        .setStock(ObjectUtils.defaultIfNull(colorStock.getSize34(), 0))
                        .setPrice(standardSizePriceMap.get(colorStock.getStoreColorId().toString() + ":" + SIZE_34)));
            }
            if (standardSizeMap.containsKey(SIZE_35)) {
                sizeStockList.add(new ShopCartDetailResDTO.SCDStoreProdSizeStockDTO().setSize(SIZE_35)
                        .setStock(ObjectUtils.defaultIfNull(colorStock.getSize35(), 0))
                        .setPrice(standardSizePriceMap.get(colorStock.getStoreColorId().toString() + ":" + SIZE_35)));
            }
            if (standardSizeMap.containsKey(SIZE_36)) {
                sizeStockList.add(new ShopCartDetailResDTO.SCDStoreProdSizeStockDTO().setSize(SIZE_36)
                        .setStock(ObjectUtils.defaultIfNull(colorStock.getSize36(), 0))
                        .setPrice(standardSizePriceMap.get(colorStock.getStoreColorId().toString() + ":" + SIZE_36)));
            }
            if (standardSizeMap.containsKey(SIZE_37)) {
                sizeStockList.add(new ShopCartDetailResDTO.SCDStoreProdSizeStockDTO().setSize(SIZE_37)
                        .setStock(ObjectUtils.defaultIfNull(colorStock.getSize37(), 0))
                        .setPrice(standardSizePriceMap.get(colorStock.getStoreColorId().toString() + ":" + SIZE_37)));
            }
            if (standardSizeMap.containsKey(SIZE_38)) {
                sizeStockList.add(new ShopCartDetailResDTO.SCDStoreProdSizeStockDTO().setSize(SIZE_38)
                        .setStock(ObjectUtils.defaultIfNull(colorStock.getSize38(), 0))
                        .setPrice(standardSizePriceMap.get(colorStock.getStoreColorId().toString() + ":" + SIZE_38)));
            }
            if (standardSizeMap.containsKey(SIZE_39)) {
                sizeStockList.add(new ShopCartDetailResDTO.SCDStoreProdSizeStockDTO().setSize(SIZE_39)
                        .setStock(ObjectUtils.defaultIfNull(colorStock.getSize39(), 0))
                        .setPrice(standardSizePriceMap.get(colorStock.getStoreColorId().toString() + ":" + SIZE_39)));
            }
            if (standardSizeMap.containsKey(SIZE_40)) {
                sizeStockList.add(new ShopCartDetailResDTO.SCDStoreProdSizeStockDTO().setSize(SIZE_40)
                        .setStock(ObjectUtils.defaultIfNull(colorStock.getSize40(), 0))
                        .setPrice(standardSizePriceMap.get(colorStock.getStoreColorId().toString() + ":" + SIZE_40)));
            }
            if (standardSizeMap.containsKey(SIZE_41)) {
                sizeStockList.add(new ShopCartDetailResDTO.SCDStoreProdSizeStockDTO().setSize(SIZE_41)
                        .setStock(ObjectUtils.defaultIfNull(colorStock.getSize41(), 0))
                        .setPrice(standardSizePriceMap.get(colorStock.getStoreColorId().toString() + ":" + SIZE_41)));
            }
            if (standardSizeMap.containsKey(SIZE_42)) {
                sizeStockList.add(new ShopCartDetailResDTO.SCDStoreProdSizeStockDTO().setSize(SIZE_42)
                        .setStock(ObjectUtils.defaultIfNull(colorStock.getSize42(), 0))
                        .setPrice(standardSizePriceMap.get(colorStock.getStoreColorId().toString() + ":" + SIZE_42)));
            }
            if (standardSizeMap.containsKey(SIZE_43)) {
                sizeStockList.add(new ShopCartDetailResDTO.SCDStoreProdSizeStockDTO().setSize(SIZE_43)
                        .setStock(ObjectUtils.defaultIfNull(colorStock.getSize43(), 0))
                        .setPrice(standardSizePriceMap.get(colorStock.getStoreColorId().toString() + ":" + SIZE_43)));
            }

            colorSizeStockMap.put(unionId, sizeStockList);
        });
        return colorSizeStockMap;
    }


}
