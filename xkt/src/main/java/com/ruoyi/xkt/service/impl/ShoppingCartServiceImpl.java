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
    final StoreProductColorPriceMapper prodColorPriceMapper;
    final StoreProductFileMapper prodFileMapper;
    final StoreProductCategoryAttributeMapper prodCateAttrMapper;
    final StoreMapper storeMapper;


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
        // 获取所有标准尺码
        List<StoreProductColorSize> standardSizeList = this.prodColorSizeMapper.selectList(new LambdaQueryWrapper<StoreProductColorSize>()
                .in(StoreProductColorSize::getStoreProdId, shoppingCartList.stream().map(ShopCartPageResDTO::getStoreProdId).collect(Collectors.toList()))
                .eq(StoreProductColorSize::getDelFlag, Constants.UNDELETED).eq(StoreProductColorSize::getStandard, ProductSizeStatus.STANDARD.getValue()));
        if (CollectionUtils.isEmpty(standardSizeList)) {
            return Page.convert(new PageInfo<>(shoppingCartList));
        }
        // 以storeProdId为key, 取标准尺码的最大值和最小值组成字符串 eg: 34 - 40
        Map<Long, String> minAndMaxSizeMap = standardSizeList.stream().collect(Collectors.groupingBy(
                StoreProductColorSize::getStoreProdId,
                Collectors.collectingAndThen(
                        Collectors.mapping(StoreProductColorSize::getSize, Collectors.toList()),
                        sizeList -> {
                            if (sizeList.isEmpty()) {
                                return ""; // 处理空列表的情况，返回空字符串或其他默认值
                            }
                            int minSize = Collections.min(sizeList);
                            int maxSize = Collections.max(sizeList);
                            return minSize + "-" + maxSize;
                        })));
        // 设置标准尺码
        shoppingCartList.forEach(x -> {
            x.setMainPicUrl(mainPicMap.getOrDefault(x.getStoreProdId(), null));
            x.getDetailList()
                    .forEach(detail -> detail.setStandardSize(minAndMaxSizeMap.getOrDefault(x.getStoreProdId(), "")));
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
        Map<String, List<StoreProductColorSize>> colorSizeMap = standardSizeList.stream().collect(Collectors
                .groupingBy(x -> x.getStoreProdId().toString() + x.getStoreColorId().toString()));
        // 获取商品颜色列表
        List<StoreProductColor> colorList = this.prodColorMapper.selectList(new LambdaQueryWrapper<StoreProductColor>()
                .eq(StoreProductColor::getStoreProdId, shoppingCart.getStoreProdId()).eq(StoreProductColor::getDelFlag, Constants.UNDELETED));
        // 档口商品颜色价格列表
        List<StoreProductColorPrice> colorPriceList = this.prodColorPriceMapper.selectList(new LambdaQueryWrapper<StoreProductColorPrice>()
                .eq(StoreProductColorPrice::getStoreProdId, shoppingCart.getStoreProdId()).eq(StoreProductColorPrice::getDelFlag, Constants.UNDELETED));
        Map<String, StoreProductColorPrice> colorPriceMap = colorPriceList.stream().collect(Collectors
                .toMap(x -> x.getStoreProdId().toString() + x.getStoreColorId().toString(), Function.identity()));
        // 根据标准尺码去找对应尺码的库存数量
        List<StoreProductStock> prodStockList = this.prodStockMapper.selectList(new LambdaQueryWrapper<StoreProductStock>()
                .eq(StoreProductStock::getStoreProdId, shoppingCart.getStoreProdId())
                .in(StoreProductStock::getStoreProdColorId, colorList.stream().map(StoreProductColor::getId).distinct().collect(Collectors.toList()))
                .eq(StoreProductStock::getDelFlag, Constants.UNDELETED));
        // 获取档口颜色尺码的库存数量
        Map<String, List<ShopCartDetailResDTO.SCDStoreProdSizeStockDTO>> colorSizeStockMap = this.convertSizeStock(prodStockList, standardSizeList);
        List<ShopCartDetailResDTO.SCDStoreProdColorDTO> colorSizeStockList = colorList.stream()
                .map(color -> BeanUtil.toBean(color, ShopCartDetailResDTO.SCDStoreProdColorDTO.class)
                        .setStoreProdColorId(color.getId())
                        // 获取颜色设定的价格
                        .setPrice(colorPriceMap.containsKey(color.getStoreProdId().toString() + color.getStoreColorId().toString())
                                ? colorPriceMap.get(color.getStoreProdId().toString() + color.getStoreColorId().toString()).getPrice() : null)
                        // 设定库存
                        .setSizeStockList(colorSizeMap.containsKey(color.getStoreProdId().toString() + color.getStoreColorId().toString())
                                ? colorSizeStockMap.get(color.getStoreProdId().toString() + color.getStoreColorId().toString()) : null))
                .collect(Collectors.toList());
        return new ShopCartDetailResDTO() {{
            setProdArtNum(shoppingCart.getProdArtNum()).setStoreProdId(shoppingCart.getStoreProdId())
                    .setStandardSizeList(standardSizeList.stream().map(StoreProductColorSize::getSize)
                            .sorted(Comparator.comparing(Function.identity()))
                            .collect(Collectors.toList()))
                    .setColorList(colorSizeStockList)
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
        List<ShoppingCart> shoppingCartList = this.shopCartMapper.selectList(new LambdaQueryWrapper<ShoppingCart>()
                .in(ShoppingCart::getStoreProdId, listDTO.getStoreProdIdList()).eq(ShoppingCart::getDelFlag, Constants.UNDELETED)
                .eq(ShoppingCart::getUserId, SecurityUtils.getUserId()));
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
        // 获取明细商品的价格
        List<StoreProductColorPrice> priceList = this.prodColorPriceMapper.selectList(new LambdaQueryWrapper<StoreProductColorPrice>()
                .in(StoreProductColorPrice::getStoreProdId, shoppingCartList.stream().map(ShoppingCart::getStoreProdId).collect(Collectors.toList()))
                .in(StoreProductColorPrice::getStoreColorId, detailList.stream().map(ShoppingCartDetail::getStoreColorId).collect(Collectors.toList()))
                .eq(StoreProductColorPrice::getDelFlag, Constants.UNDELETED));
        // 商品价格map
        Map<String, BigDecimal> priceMap = priceList.stream().collect(Collectors
                .toMap(x -> x.getStoreProdId().toString() + x.getStoreColorId().toString(), x -> ObjectUtils.defaultIfNull(x.getPrice(), BigDecimal.ZERO)));
        // 获取商品价格尺码
        List<StoreProductColorSize> priceSizeList = this.prodColorSizeMapper.selectList(new LambdaQueryWrapper<StoreProductColorSize>()
                .in(StoreProductColorSize::getStoreProdId, shoppingCartList.stream().map(ShoppingCart::getStoreProdId).collect(Collectors.toList()))
                .in(StoreProductColorSize::getStoreColorId, detailList.stream().map(ShoppingCartDetail::getStoreColorId).collect(Collectors.toList()))
                .eq(StoreProductColorSize::getDelFlag, Constants.UNDELETED));
        // 商品价格尺码map
        Map<String, Long> priceSizeMap = priceSizeList.stream().collect(Collectors
                .toMap(x -> x.getStoreProdId().toString() + x.getStoreColorId().toString() + x.getSize(), StoreProductColorSize::getId));
        return shoppingCartList.stream().map(x -> {
            ShoppingCartDTO shopCartDTO = BeanUtil.toBean(x, ShoppingCartDTO.class).setMainPicUrl(mainPicMap.get(x.getStoreProdId()))
                    .setStoreName(ObjectUtils.isNotEmpty(storeMap.get(x.getStoreId())) ? storeMap.get(x.getStoreId()).getStoreName() : "");
            List<ShoppingCartDTO.SCDetailDTO> shopCartDetailList = detailMap.get(x.getId()).stream().map(detail -> {
                final BigDecimal price = ObjectUtils.defaultIfNull(priceMap.get(x.getStoreProdId().toString() + detail.getStoreColorId().toString()), BigDecimal.ZERO);
                return BeanUtil.toBean(detail, ShoppingCartDTO.SCDetailDTO.class).setPrice(price)
                        .setStoreProdColorSizeId(priceSizeMap.get(x.getStoreProdId().toString() + detail.getStoreColorId().toString() + detail.getSize()))
                        .setAmount(price.multiply(BigDecimal.valueOf(detail.getQuantity())));
            }).collect(Collectors.toList());
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
        return this.shopCartMapper.getStatusNum(SecurityUtils.getUserId());
    }

    /**
     * 获取档口商品颜色尺码的库存
     *
     * @param stockList        库存数量
     * @param standardSizeList 当前商品的标准尺码
     * @return Map<Long, Map < Integer, Integer>>
     */
    private Map<String, List<ShopCartDetailResDTO.SCDStoreProdSizeStockDTO>> convertSizeStock(List<StoreProductStock> stockList, List<StoreProductColorSize> standardSizeList) {
        Map<String, List<ShopCartDetailResDTO.SCDStoreProdSizeStockDTO>> colorSizeStockMap = new HashMap<>();
        if (CollectionUtils.isEmpty(stockList)) {
            return colorSizeStockMap;
        }
        // 标准尺码map
        Map<Integer, StoreProductColorSize> standardSizeMap = standardSizeList.stream().collect(Collectors.toMap(StoreProductColorSize::getSize, Function.identity()));
        Map<String, List<StoreProductStock>> map = stockList.stream().collect(Collectors.groupingBy(x -> x.getStoreProdId().toString() + x.getStoreColorId().toString()));
        map.forEach((unionId, tempStockList) -> {
            List<ShopCartDetailResDTO.SCDStoreProdSizeStockDTO> sizeStockList = new ArrayList<>();
            Integer size30Stock = tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize30(), 0)).reduce(0, Integer::sum);
            if (standardSizeMap.containsKey(SIZE_30)) {
                sizeStockList.add(new ShopCartDetailResDTO.SCDStoreProdSizeStockDTO() {{
                    setSize(SIZE_30);
                    setStock(size30Stock);
                }});
            }
            Integer size31Stock = tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize31(), 0)).reduce(0, Integer::sum);
            if (standardSizeMap.containsKey(SIZE_31)) {
                sizeStockList.add(new ShopCartDetailResDTO.SCDStoreProdSizeStockDTO() {{
                    setSize(SIZE_31);
                    setStock(size31Stock);
                }});
            }
            Integer size32Stock = tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize32(), 0)).reduce(0, Integer::sum);
            if (standardSizeMap.containsKey(SIZE_32)) {
                sizeStockList.add(new ShopCartDetailResDTO.SCDStoreProdSizeStockDTO() {{
                    setSize(SIZE_32);
                    setStock(size32Stock);
                }});
            }
            Integer size33Stock = tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize33(), 0)).reduce(0, Integer::sum);
            if (standardSizeMap.containsKey(SIZE_33)) {
                sizeStockList.add(new ShopCartDetailResDTO.SCDStoreProdSizeStockDTO() {{
                    setSize(SIZE_33);
                    setStock(size33Stock);
                }});
            }
            Integer size34Stock = tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize34(), 0)).reduce(0, Integer::sum);
            if (standardSizeMap.containsKey(SIZE_34)) {
                sizeStockList.add(new ShopCartDetailResDTO.SCDStoreProdSizeStockDTO() {{
                    setSize(SIZE_34);
                    setStock(size34Stock);
                }});
            }
            Integer size35Stock = tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize35(), 0)).reduce(0, Integer::sum);
            if (standardSizeMap.containsKey(SIZE_35)) {
                sizeStockList.add(new ShopCartDetailResDTO.SCDStoreProdSizeStockDTO() {{
                    setSize(SIZE_35);
                    setStock(size35Stock);
                }});
            }
            Integer size36Stock = tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize36(), 0)).reduce(0, Integer::sum);
            if (standardSizeMap.containsKey(SIZE_36)) {
                sizeStockList.add(new ShopCartDetailResDTO.SCDStoreProdSizeStockDTO() {{
                    setSize(SIZE_36);
                    setStock(size36Stock);
                }});
            }
            Integer size37Stock = tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize37(), 0)).reduce(0, Integer::sum);
            if (standardSizeMap.containsKey(SIZE_37)) {
                sizeStockList.add(new ShopCartDetailResDTO.SCDStoreProdSizeStockDTO() {{
                    setSize(SIZE_37);
                    setStock(size37Stock);
                }});
            }
            Integer size38Stock = tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize38(), 0)).reduce(0, Integer::sum);
            if (standardSizeMap.containsKey(SIZE_38)) {
                sizeStockList.add(new ShopCartDetailResDTO.SCDStoreProdSizeStockDTO() {{
                    setSize(SIZE_38);
                    setStock(size38Stock);
                }});
            }
            Integer size39Stock = tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize39(), 0)).reduce(0, Integer::sum);
            if (standardSizeMap.containsKey(SIZE_39)) {
                sizeStockList.add(new ShopCartDetailResDTO.SCDStoreProdSizeStockDTO() {{
                    setSize(SIZE_39);
                    setStock(size39Stock);
                }});
            }
            Integer size40Stock = tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize40(), 0)).reduce(0, Integer::sum);
            if (standardSizeMap.containsKey(SIZE_40)) {
                sizeStockList.add(new ShopCartDetailResDTO.SCDStoreProdSizeStockDTO() {{
                    setSize(SIZE_40);
                    setStock(size40Stock);
                }});
            }
            Integer size41Stock = tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize41(), 0)).reduce(0, Integer::sum);
            if (standardSizeMap.containsKey(SIZE_41)) {
                sizeStockList.add(new ShopCartDetailResDTO.SCDStoreProdSizeStockDTO() {{
                    setSize(SIZE_41);
                    setStock(size41Stock);
                }});
            }
            Integer size42Stock = tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize42(), 0)).reduce(0, Integer::sum);
            if (standardSizeMap.containsKey(SIZE_42)) {
                sizeStockList.add(new ShopCartDetailResDTO.SCDStoreProdSizeStockDTO() {{
                    setSize(SIZE_42);
                    setStock(size42Stock);
                }});
            }
            Integer size43Stock = tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize43(), 0)).reduce(0, Integer::sum);
            if (standardSizeMap.containsKey(SIZE_43)) {
                sizeStockList.add(new ShopCartDetailResDTO.SCDStoreProdSizeStockDTO() {{
                    setSize(SIZE_43);
                    setStock(size43Stock);
                }});
            }
            colorSizeStockMap.put(unionId, sizeStockList);
        });
        return colorSizeStockMap;
    }


}
