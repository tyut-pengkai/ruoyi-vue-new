package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.dto.storeHomepage.*;
import com.ruoyi.xkt.dto.storeProduct.StoreProdPriceAndMainPicAndTagDTO;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdMainPicDTO;
import com.ruoyi.xkt.enums.AdDisplayType;
import com.ruoyi.xkt.enums.FileType;
import com.ruoyi.xkt.enums.HomepageJumpType;
import com.ruoyi.xkt.enums.HomepageType;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IStoreHomepageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ruoyi.common.constant.Constants.*;

/**
 * 档口首页Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RequiredArgsConstructor
@Service
public class StoreHomepageServiceImpl implements IStoreHomepageService {

    final SysFileMapper fileMapper;
    final StoreHomepageMapper storeHomeMapper;
    final StoreMapper storeMapper;
    final StoreProductMapper storeProdMapper;
    final StoreProductDetailMapper prodDetailMapper;
    final StoreProductColorMapper prodColorMapper;
    final StoreProductColorSizeMapper prodColorSizeMapper;
    final StoreProductStockMapper prodStockMapper;
    final StoreProductColorPriceMapper prodColorPriceMapper;
    final StoreProductFileMapper prodFileMapper;
    final StoreProductCategoryAttributeMapper prodCateAttrMapper;
    final StoreCertificateMapper storeCertMapper;

    /**
     * 新增档口首页各部分图
     *
     * @param homepageDTO 新增档口首页各部分图
     * @return Integer
     */
    @Override
    @Transactional
    public Integer insert(StoreHomeDecorationDTO homepageDTO) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(homepageDTO.getStoreId())) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        List<StoreHomepage> homepageList = this.insertToHomepage(homepageDTO);
        if (CollectionUtils.isEmpty(homepageList)) {
            return 0;
        }
        // 当前档口首页各部分总的文件大小
        BigDecimal totalSize = homepageDTO.getBigBannerList().stream().map(x -> ObjectUtils.defaultIfNull(x.getFileSize(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                        .eq(Store::getId, homepageDTO.getStoreId()).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        store.setTemplateNum(homepageDTO.getTemplateNum());
        // 更新档口首页使用的总的文件容量
        store.setStorageUsage(ObjectUtils.defaultIfNull(store.getStorageUsage(), BigDecimal.ZERO).add(totalSize));
        this.storeMapper.updateById(store);
        return homepageList.size();
    }


    /**
     * 获取档口首页各个部分的图信息
     *
     * @param storeId 档口ID
     * @return StoreHomeDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreHomeDecorationResDTO selectByStoreId(Long storeId) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(storeId)) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                        .eq(Store::getId, storeId).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        List<StoreHomepage> homeList = this.storeHomeMapper.selectList(new LambdaQueryWrapper<StoreHomepage>()
                .eq(StoreHomepage::getStoreId, storeId).eq(StoreHomepage::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isEmpty(homeList)) {
            return new StoreHomeDecorationResDTO();
        }
        final List<Long> fileIdList = homeList.stream().map(StoreHomepage::getFileId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(fileIdList)) {
            return new StoreHomeDecorationResDTO();
        }
        List<SysFile> fileList = Optional.ofNullable(this.fileMapper.selectList(new LambdaQueryWrapper<SysFile>()
                        .in(SysFile::getId, fileIdList).eq(SysFile::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("文件不存在", HttpStatus.ERROR));
        Map<Long, SysFile> fileMap = fileList.stream().collect(Collectors.toMap(SysFile::getId, Function.identity()));
        Map<Long, StoreProduct> storeProdMap = new HashMap<>();
        Map<Long, String> mainPicMap = new HashMap<>();
        // 档口商品ID列表
        List<Long> storeProdIdList = homeList.stream()
                .filter(x -> Objects.equals(x.getJumpType(), HomepageJumpType.JUMP_PRODUCT.getValue())).map(StoreHomepage::getBizId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(storeProdIdList)) {
            // 所有的档口商品ID
            List<StoreProduct> storeProdList = Optional.ofNullable(this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                            .eq(StoreProduct::getStoreId, storeId).in(StoreProduct::getId, storeProdIdList)
                            .eq(StoreProduct::getDelFlag, Constants.UNDELETED)))
                    .orElseThrow(() -> new ServiceException("档口商品不存在", HttpStatus.ERROR));
            storeProdMap = storeProdList.stream().collect(Collectors.toMap(StoreProduct::getId, Function.identity()));
            // 查找排名第一个商品主图列表
            List<StoreProdMainPicDTO> mainPicList = this.prodFileMapper.selectMainPicByStoreProdIdList(storeProdIdList, FileType.MAIN_PIC.getValue(), ORDER_NUM_1);
            mainPicMap = CollectionUtils.isEmpty(mainPicList) ? new HashMap<>() : mainPicList.stream()
                    .collect(Collectors.toMap(StoreProdMainPicDTO::getStoreProdId, StoreProdMainPicDTO::getFileUrl));
        }
        Map<Long, StoreProduct> finalStoreProdMap = storeProdMap;
        // 轮播图
        List<StoreHomeDecorationResDTO.DecorationDTO> bigBannerList = homeList.stream().filter(x -> Objects.equals(x.getFileType(), HomepageType.SLIDING_PICTURE.getValue()))
                .map(x -> {
                    StoreHomeDecorationResDTO.DecorationDTO decorationDTO = BeanUtil.toBean(x, StoreHomeDecorationResDTO.DecorationDTO.class)
                            .setBizName((Objects.equals(x.getJumpType(), HomepageJumpType.JUMP_PRODUCT.getValue()))
                                    ? (finalStoreProdMap.containsKey(x.getBizId()) ? finalStoreProdMap.get(x.getBizId()).getProdArtNum() : null)
                                    : (ObjectUtils.isEmpty(x.getBizId()) ? null : store.getStoreName()));
                    if (fileMap.containsKey(x.getFileId())) {
                        decorationDTO.setFileType(x.getFileType()).setFileUrl(fileMap.get(x.getFileId()).getFileUrl());
                    }
                    return decorationDTO;
                }).collect(Collectors.toList());
        // 其它图部分
        Map<Long, String> finalMainPicMap = mainPicMap;
        List<StoreHomeDecorationResDTO.DecorationDTO> decorList = homeList.stream().filter(x -> !Objects.equals(x.getFileType(), HomepageType.SLIDING_PICTURE.getValue()))
                .map(x -> BeanUtil.toBean(x, StoreHomeDecorationResDTO.DecorationDTO.class)
                        .setBizName(finalStoreProdMap.containsKey(x.getBizId()) ? finalStoreProdMap.get(x.getBizId()).getProdArtNum() : null)
                        .setFileType(x.getFileType()).setFileUrl(finalMainPicMap.get(x.getBizId())))
                .collect(Collectors.toList());
        return new StoreHomeDecorationResDTO() {{
            setTemplateNum(store.getTemplateNum());
            setBigBannerList(bigBannerList);
            setDecorationList(decorList);
        }};
    }

    /**
     * 更新档口首页各部分图信息
     *
     * @param homepageDTO 更新的dto
     * @return Integer
     */
    @Override
    @Transactional
    public Integer updateStoreHomepage(StoreHomeDecorationDTO homepageDTO) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(homepageDTO.getStoreId())) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        // 先将所有的档口模板的文件都删除掉
        List<StoreHomepage> oldHomeList = this.storeHomeMapper.selectList(new LambdaQueryWrapper<StoreHomepage>()
                .eq(StoreHomepage::getStoreId, homepageDTO.getStoreId()).eq(StoreHomepage::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isNotEmpty(oldHomeList)) {
            oldHomeList.forEach(x -> x.setDelFlag(Constants.DELETED));
            this.storeHomeMapper.updateById(oldHomeList);
        }
        // 新增档口首页各个部分的图信息
        List<StoreHomepage> homepageList = this.insertToHomepage(homepageDTO);
        Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                        .eq(Store::getId, homepageDTO.getStoreId()).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        store.setTemplateNum(homepageDTO.getTemplateNum());
        this.storeMapper.updateById(store);
        return homepageList.size();
    }

    /**
     * 获取档口推荐商品列表
     *
     * @param storeId 档口ID
     * @return List<StoreRecommendResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreRecommendResDTO> getStoreRecommendList(Long storeId) {
        List<StoreHomepage> recommendList = this.storeHomeMapper.selectList(new LambdaQueryWrapper<StoreHomepage>()
                .eq(StoreHomepage::getStoreId, storeId).eq(StoreHomepage::getDelFlag, Constants.UNDELETED)
                .eq(StoreHomepage::getFileType, HomepageType.STORE_RECOMMENDED.getValue()));
        if (CollectionUtils.isEmpty(recommendList)) {
            return Collections.emptyList();
        }
        // 商品价格、主图、标签等
        List<StoreProdPriceAndMainPicAndTagDTO> attrList = this.storeProdMapper.selectPriceAndMainPicAndTagList(recommendList.stream()
                .map(StoreHomepage::getBizId).collect(Collectors.toList()));
        Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap = attrList.stream()
                .collect(Collectors.toMap(StoreProdPriceAndMainPicAndTagDTO::getStoreProdId, Function.identity()));
        return recommendList.stream().map(x -> {
                    StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getBizId());
                    if (ObjectUtils.isEmpty(dto)) {
                        return null;
                    }
                    final List<String> tags = ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? Arrays.asList(dto.getTagStr().split(",")) : null;
                    return BeanUtil.toBean(dto, StoreRecommendResDTO.class).setTags(tags);
                })
                .filter(ObjectUtils::isNotEmpty)
                .collect(Collectors.toList());
    }

    /**
     * 档口首页模板一返回数据
     *
     * @param storeId 档口ID
     * @return StoreHomeTemplateOneResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreHomeTemplateOneResDTO getTemplateOne(Long storeId) {
        // 顶部轮播大图
        StoreHomeTemplateOneResDTO templateTwo = new StoreHomeTemplateOneResDTO().setTopLeftList(this.storeHomeMapper.selectTopLeftList(storeId));
        // 其他区域
        List<StoreHomepage> otherList = this.storeHomeMapper.selectList(new LambdaQueryWrapper<StoreHomepage>()
                .eq(StoreHomepage::getStoreId, storeId).eq(StoreHomepage::getDelFlag, Constants.UNDELETED)
                .in(StoreHomepage::getFileType, Arrays.asList(HomepageType.POPULAR_SALES.getValue(),
                        HomepageType.SLIDING_PICTURE_SMALL.getValue(), HomepageType.SEASON_NEW_PRODUCTS.getValue(),
                        HomepageType.STORE_RECOMMENDED.getValue(), HomepageType.SALES_RANKING.getValue())));
        if (CollectionUtils.isEmpty(otherList)) {
            return templateTwo;
        }
        final List<Long> storeProdIdList = otherList.stream().map(StoreHomepage::getBizId).collect(Collectors.toList());
        List<StoreProdPriceAndMainPicAndTagDTO> attrList = storeProdMapper.selectPriceAndMainPicAndTagList(storeProdIdList);
        Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap = attrList.stream().collect(Collectors.toMap(StoreProdPriceAndMainPicAndTagDTO::getStoreProdId, x -> x));
        return templateTwo
                .setTopRightList(this.getTemplateTypeList(otherList, attrMap, HomepageType.SLIDING_PICTURE_SMALL.getValue(), 2))
                .setRecommendList(this.getTemplateTypeList(otherList, attrMap, HomepageType.STORE_RECOMMENDED.getValue(), 5))
                .setPopularSaleList(this.getTemplateTypeList(otherList, attrMap, HomepageType.POPULAR_SALES.getValue(), 5))
                .setNewProdList(this.getTemplateTypeList(otherList, attrMap, HomepageType.SEASON_NEW_PRODUCTS.getValue(), 5))
                .setSaleRankList(this.getTemplateTypeList(otherList, attrMap, HomepageType.SALES_RANKING.getValue(), 10));
    }

    /**
     * 档口首页模板二返回数据
     *
     * @param storeId 档口ID
     * @return StoreHomeTemplateTwoResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreHomeTemplateTwoResDTO getTemplateTwo(Long storeId) {
        // 顶部轮播大图
        StoreHomeTemplateTwoResDTO templateTwo = new StoreHomeTemplateTwoResDTO().setTopLeftList(this.storeHomeMapper.selectTopLeftList(storeId));

        // TODO 获取档口公告
        // TODO 获取档口公告
        // TODO 获取档口公告

        List<StoreHomepage> otherList = this.storeHomeMapper.selectList(new LambdaQueryWrapper<StoreHomepage>()
                .eq(StoreHomepage::getStoreId, storeId).eq(StoreHomepage::getDelFlag, Constants.UNDELETED)
                .in(StoreHomepage::getFileType, Arrays.asList(HomepageType.POPULAR_SALES.getValue(),
                        HomepageType.SEASON_NEW_PRODUCTS.getValue(), HomepageType.STORE_RECOMMENDED.getValue(),
                        HomepageType.SALES_RANKING.getValue())));
        if (CollectionUtils.isEmpty(otherList)) {
            return templateTwo;
        }
        final List<Long> storeProdIdList = otherList.stream().map(StoreHomepage::getBizId).collect(Collectors.toList());
        List<StoreProdPriceAndMainPicAndTagDTO> attrList = storeProdMapper.selectPriceAndMainPicAndTagList(storeProdIdList);
        Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap = attrList.stream().collect(Collectors.toMap(StoreProdPriceAndMainPicAndTagDTO::getStoreProdId, x -> x));
        return templateTwo
                .setRecommendList(this.getTemplateTypeList(otherList, attrMap, HomepageType.STORE_RECOMMENDED.getValue(), 5))
                .setPopularSaleList(this.getTemplateTypeList(otherList, attrMap, HomepageType.POPULAR_SALES.getValue(), 5))
                .setNewProdList(this.getTemplateTypeList(otherList, attrMap, HomepageType.SEASON_NEW_PRODUCTS.getValue(), 5))
                .setSaleRankList(this.getTemplateTypeList(otherList, attrMap, HomepageType.SALES_RANKING.getValue(), 10));
    }

    /**
     * 档口首页模板三返回数据
     *
     * @param storeId 档口ID
     * @return StoreHomeTemplateThirdResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreHomeTemplateThirdResDTO getTemplateThird(Long storeId) {
        // 顶部轮播大图
        StoreHomeTemplateThirdResDTO templateOne = new StoreHomeTemplateThirdResDTO().setTopLeftList(this.storeHomeMapper.selectTopLeftList(storeId));
        // 顶部右侧商品 及 店家推荐 和 销量排行
        List<StoreHomepage> otherList = this.storeHomeMapper.selectList(new LambdaQueryWrapper<StoreHomepage>()
                .eq(StoreHomepage::getStoreId, storeId).eq(StoreHomepage::getDelFlag, Constants.UNDELETED)
                .in(StoreHomepage::getFileType, Arrays.asList(HomepageType.SLIDING_PICTURE_SMALL.getValue(),
                        HomepageType.STORE_RECOMMENDED.getValue(), HomepageType.SALES_RANKING.getValue())));
        if (CollectionUtils.isEmpty(otherList)) {
            return templateOne;
        }
        final List<Long> storeProdIdList = otherList.stream().map(StoreHomepage::getBizId).collect(Collectors.toList());
        List<StoreProdPriceAndMainPicAndTagDTO> attrList = storeProdMapper.selectPriceAndMainPicAndTagList(storeProdIdList);
        Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap = attrList.stream().collect(Collectors.toMap(StoreProdPriceAndMainPicAndTagDTO::getStoreProdId, x -> x));
        return templateOne.setTopRightList(this.getTemplateTypeList(otherList, attrMap, HomepageType.SLIDING_PICTURE_SMALL.getValue(), 2))
                .setRecommendList(this.getTemplateTypeList(otherList, attrMap, HomepageType.STORE_RECOMMENDED.getValue(), 10))
                .setSaleRankList(this.getTemplateTypeList(otherList, attrMap, HomepageType.SALES_RANKING.getValue(), 10));
    }

    /**
     * 档口首页模板四返回数据
     *
     * @param storeId 档口ID
     * @return StoreHomeTemplateFourResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreHomeTemplateFourResDTO getTemplateFour(Long storeId) {
        // 顶部轮播大图
        StoreHomeTemplateFourResDTO templateFour = new StoreHomeTemplateFourResDTO().setTopLeftList(this.storeHomeMapper.selectTopLeftList(storeId));
        // 其他区域
        List<StoreHomepage> otherList = this.storeHomeMapper.selectList(new LambdaQueryWrapper<StoreHomepage>()
                .eq(StoreHomepage::getStoreId, storeId).eq(StoreHomepage::getDelFlag, Constants.UNDELETED)
                .in(StoreHomepage::getFileType, Arrays.asList(HomepageType.POPULAR_SALES.getValue(),
                        HomepageType.SLIDING_PICTURE_SMALL.getValue(), HomepageType.SEASON_NEW_PRODUCTS.getValue(),
                        HomepageType.STORE_RECOMMENDED.getValue())));
        if (CollectionUtils.isEmpty(otherList)) {
            return templateFour;
        }
        final List<Long> storeProdIdList = otherList.stream().map(StoreHomepage::getBizId).collect(Collectors.toList());
        List<StoreProdPriceAndMainPicAndTagDTO> attrList = storeProdMapper.selectPriceAndMainPicAndTagList(storeProdIdList);
        Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap = attrList.stream().collect(Collectors.toMap(StoreProdPriceAndMainPicAndTagDTO::getStoreProdId, x -> x));
        return templateFour
                .setTopRightList(this.getTemplateTypeList(otherList, attrMap, HomepageType.SLIDING_PICTURE_SMALL.getValue(), 2))
                .setRecommendList(this.getTemplateTypeList(otherList, attrMap, HomepageType.STORE_RECOMMENDED.getValue(), 5))
                .setPopularSaleList(this.getTemplateTypeList(otherList, attrMap, HomepageType.POPULAR_SALES.getValue(), 5))
                .setNewProdList(this.getTemplateTypeList(otherList, attrMap, HomepageType.SEASON_NEW_PRODUCTS.getValue(), 5));
    }

    /**
     * 档口首页模板五返回数据
     *
     * @param storeId 档口ID
     * @return StoreHomeTemplateFiveResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreHomeTemplateFiveResDTO getTemplateFive(Long storeId) {
        // 顶部轮播大图
        StoreHomeTemplateFiveResDTO templateFive = new StoreHomeTemplateFiveResDTO().setTopLeftList(this.storeHomeMapper.selectTopLeftList(storeId));

        // TODO 获取档口公告
        // TODO 获取档口公告
        // TODO 获取档口公告

        // 其他区域
        List<StoreHomepage> otherList = this.storeHomeMapper.selectList(new LambdaQueryWrapper<StoreHomepage>()
                .eq(StoreHomepage::getStoreId, storeId).eq(StoreHomepage::getDelFlag, Constants.UNDELETED)
                .in(StoreHomepage::getFileType, Arrays.asList(HomepageType.POPULAR_SALES.getValue(),
                        HomepageType.SLIDING_PICTURE_SMALL.getValue(), HomepageType.SEASON_NEW_PRODUCTS.getValue(),
                        HomepageType.STORE_RECOMMENDED.getValue(), HomepageType.SALES_RANKING.getValue())));
        if (CollectionUtils.isEmpty(otherList)) {
            return templateFive;
        }
        final List<Long> storeProdIdList = otherList.stream().map(StoreHomepage::getBizId).collect(Collectors.toList());
        List<StoreProdPriceAndMainPicAndTagDTO> attrList = storeProdMapper.selectPriceAndMainPicAndTagList(storeProdIdList);
        Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap = attrList.stream().collect(Collectors.toMap(StoreProdPriceAndMainPicAndTagDTO::getStoreProdId, x -> x));
        return templateFive
                .setTopRightList(this.getTemplateTypeList(otherList, attrMap, HomepageType.SLIDING_PICTURE_SMALL.getValue(), 2))
                .setRecommendList(this.getTemplateTypeList(otherList, attrMap, HomepageType.STORE_RECOMMENDED.getValue(), 10))
                .setSaleRankList(this.getTemplateTypeList(otherList, attrMap, HomepageType.SALES_RANKING.getValue(), 10));
    }

    /**
     * 获取档口首页各个类型的数据
     *
     * @param list         各个的数据列表
     * @param attrMap      各个属性map
     * @param templateType 当前模板type
     * @param limitCount   返回商品数量
     * @return List<StoreHomeTemplateItemResDTO>
     */
    private List<StoreHomeTemplateItemResDTO> getTemplateTypeList(List<StoreHomepage> list, Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap,
                                                                  Integer templateType, Integer limitCount) {
        // 顶部右侧商品
        return list.stream().filter(x -> Objects.equals(x.getFileType(), templateType))
                .map(x -> {
                    StoreProdPriceAndMainPicAndTagDTO attr = attrMap.get(x.getBizId());
                    final List<String> tags = ObjectUtils.isNotEmpty(attr) && StringUtils.isNotBlank(attr.getTagStr())
                            ? Arrays.asList(attr.getTagStr().split(",")) : null;
                    return new StoreHomeTemplateItemResDTO().setOrderNum(x.getOrderNum())
                            .setDisplayType(AdDisplayType.PRODUCT.getValue()).setTags(tags)
                            .setStoreId(ObjectUtils.isNotEmpty(attr) ? attr.getStoreId() : null)
                            .setStoreName(ObjectUtils.isNotEmpty(attr) ? attr.getStoreName() : null)
                            .setStoreProdId(ObjectUtils.isNotEmpty(attr) ? attr.getStoreProdId() : null)
                            .setProdArtNum(ObjectUtils.isNotEmpty(attr) ? attr.getProdArtNum() : null)
                            .setMainPicUrl(ObjectUtils.isNotEmpty(attr) ? attr.getMainPicUrl() : null)
                            .setProdPrice(ObjectUtils.isNotEmpty(attr) ? attr.getMinPrice() : null)
                            .setProdTitle(ObjectUtils.isNotEmpty(attr) ? attr.getProdTitle() : null)
                            .setHasVideo(ObjectUtils.isNotEmpty(attr) ? attr.getHasVideo() : Boolean.FALSE);
                })
                .limit(limitCount)
                .collect(Collectors.toList());
    }

    /**
     * 获取档口商品颜色尺码的库存
     *
     * @param stockList        库存数量
     * @param standardSizeList 当前商品的标准尺码
     * @return Map<Long, Map < Integer, Integer>>
     */
    private Map<String, List<StoreHomeProdResDTO.StoreProdSizeStockDTO>> convertSizeStock(List<StoreProductStock> stockList, List<StoreProductColorSize> standardSizeList) {
        Map<String, List<StoreHomeProdResDTO.StoreProdSizeStockDTO>> colorSizeStockMap = new HashMap<>();
        if (CollectionUtils.isEmpty(stockList)) {
            return colorSizeStockMap;
        }
        // 标准尺码map
        Map<Integer, StoreProductColorSize> standardSizeMap = standardSizeList.stream().collect(Collectors.toMap(StoreProductColorSize::getSize, Function.identity()));
        Map<String, List<StoreProductStock>> map = stockList.stream().collect(Collectors.groupingBy(x -> x.getStoreProdId().toString() + x.getStoreColorId().toString()));
        map.forEach((unionId, tempStockList) -> {
            List<StoreHomeProdResDTO.StoreProdSizeStockDTO> sizeStockList = new ArrayList<>();
            Integer size30Stock = tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize30(), 0)).reduce(0, Integer::sum);
            if (standardSizeMap.containsKey(SIZE_30)) {
                sizeStockList.add(new StoreHomeProdResDTO.StoreProdSizeStockDTO() {{
                    setSize(SIZE_30);
                    setStock(size30Stock);
                }});
            }
            Integer size31Stock = tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize31(), 0)).reduce(0, Integer::sum);
            if (standardSizeMap.containsKey(SIZE_31)) {
                sizeStockList.add(new StoreHomeProdResDTO.StoreProdSizeStockDTO() {{
                    setSize(SIZE_31);
                    setStock(size31Stock);
                }});
            }
            Integer size32Stock = tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize32(), 0)).reduce(0, Integer::sum);
            if (standardSizeMap.containsKey(SIZE_32)) {
                sizeStockList.add(new StoreHomeProdResDTO.StoreProdSizeStockDTO() {{
                    setSize(SIZE_32);
                    setStock(size32Stock);
                }});
            }
            Integer size33Stock = tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize33(), 0)).reduce(0, Integer::sum);
            if (standardSizeMap.containsKey(SIZE_33)) {
                sizeStockList.add(new StoreHomeProdResDTO.StoreProdSizeStockDTO() {{
                    setSize(SIZE_33);
                    setStock(size33Stock);
                }});
            }
            Integer size34Stock = tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize34(), 0)).reduce(0, Integer::sum);
            if (standardSizeMap.containsKey(SIZE_34)) {
                sizeStockList.add(new StoreHomeProdResDTO.StoreProdSizeStockDTO() {{
                    setSize(SIZE_34);
                    setStock(size34Stock);
                }});
            }
            Integer size35Stock = tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize35(), 0)).reduce(0, Integer::sum);
            if (standardSizeMap.containsKey(SIZE_35)) {
                sizeStockList.add(new StoreHomeProdResDTO.StoreProdSizeStockDTO() {{
                    setSize(SIZE_35);
                    setStock(size35Stock);
                }});
            }
            Integer size36Stock = tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize36(), 0)).reduce(0, Integer::sum);
            if (standardSizeMap.containsKey(SIZE_36)) {
                sizeStockList.add(new StoreHomeProdResDTO.StoreProdSizeStockDTO() {{
                    setSize(SIZE_36);
                    setStock(size36Stock);
                }});
            }
            Integer size37Stock = tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize37(), 0)).reduce(0, Integer::sum);
            if (standardSizeMap.containsKey(SIZE_37)) {
                sizeStockList.add(new StoreHomeProdResDTO.StoreProdSizeStockDTO() {{
                    setSize(SIZE_37);
                    setStock(size37Stock);
                }});
            }
            Integer size38Stock = tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize38(), 0)).reduce(0, Integer::sum);
            if (standardSizeMap.containsKey(SIZE_38)) {
                sizeStockList.add(new StoreHomeProdResDTO.StoreProdSizeStockDTO() {{
                    setSize(SIZE_38);
                    setStock(size38Stock);
                }});
            }
            Integer size39Stock = tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize39(), 0)).reduce(0, Integer::sum);
            if (standardSizeMap.containsKey(SIZE_39)) {
                sizeStockList.add(new StoreHomeProdResDTO.StoreProdSizeStockDTO() {{
                    setSize(SIZE_39);
                    setStock(size39Stock);
                }});
            }
            Integer size40Stock = tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize40(), 0)).reduce(0, Integer::sum);
            if (standardSizeMap.containsKey(SIZE_40)) {
                sizeStockList.add(new StoreHomeProdResDTO.StoreProdSizeStockDTO() {{
                    setSize(SIZE_40);
                    setStock(size40Stock);
                }});
            }
            Integer size41Stock = tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize41(), 0)).reduce(0, Integer::sum);
            if (standardSizeMap.containsKey(SIZE_41)) {
                sizeStockList.add(new StoreHomeProdResDTO.StoreProdSizeStockDTO() {{
                    setSize(SIZE_41);
                    setStock(size41Stock);
                }});
            }
            Integer size42Stock = tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize42(), 0)).reduce(0, Integer::sum);
            if (standardSizeMap.containsKey(SIZE_42)) {
                sizeStockList.add(new StoreHomeProdResDTO.StoreProdSizeStockDTO() {{
                    setSize(SIZE_42);
                    setStock(size42Stock);
                }});
            }
            Integer size43Stock = tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize43(), 0)).reduce(0, Integer::sum);
            if (standardSizeMap.containsKey(SIZE_43)) {
                sizeStockList.add(new StoreHomeProdResDTO.StoreProdSizeStockDTO() {{
                    setSize(SIZE_43);
                    setStock(size43Stock);
                }});
            }
            colorSizeStockMap.put(unionId, sizeStockList);
        });
        return colorSizeStockMap;
    }


    /**
     * 新增档口首页模板展示
     *
     * @param homepageDTO 新增档口首页入参
     * @return
     */
    private List<StoreHomepage> insertToHomepage(StoreHomeDecorationDTO homepageDTO) {
        List<StoreHomepage> homePageList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(homepageDTO.getBigBannerList())) {
            // 新增的首页轮播大图部分
            List<SysFile> bigBannerFileList = homepageDTO.getBigBannerList().stream().filter(x -> StringUtils.isNotBlank(x.getFileUrl())
                            && StringUtils.isNotBlank(x.getFileName()) && ObjectUtils.isNotEmpty(x.getFileSize()) && ObjectUtils.isNotEmpty(x.getOrderNum()))
                    .map(x -> BeanUtil.toBean(x, SysFile.class)).collect(Collectors.toList());
            this.fileMapper.insert(bigBannerFileList);
            Map<String, SysFile> bigBannerMap = bigBannerFileList.stream().collect(Collectors.toMap(SysFile::getFileName, Function.identity()));
            homePageList.addAll(homepageDTO.getBigBannerList().stream().map(x -> BeanUtil.toBean(x, StoreHomepage.class).setStoreId(homepageDTO.getStoreId())
                            .setFileId(bigBannerMap.containsKey(x.getFileName()) ? bigBannerMap.get(x.getFileName()).getId() : null))
                    .collect(Collectors.toList()));
        }
        if (CollectionUtils.isNotEmpty(homepageDTO.getDecorationList())) {
            homePageList.addAll(homepageDTO.getDecorationList().stream().map(x -> BeanUtil.toBean(x, StoreHomepage.class).setStoreId(homepageDTO.getStoreId()))
                    .collect(Collectors.toList()));
        }
        if (CollectionUtils.isNotEmpty(homePageList)) {
            this.storeHomeMapper.insert(homePageList);
        }
        return homePageList;
    }


}
