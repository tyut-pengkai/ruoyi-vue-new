package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.dto.storeHomepage.*;
import com.ruoyi.xkt.dto.storeProduct.StoreProdPriceAndMainPicAndTagDTO;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdMainPicDTO;
import com.ruoyi.xkt.enums.*;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IStoreHomepageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    final StoreProductFileMapper prodFileMapper;
    final StoreProductCategoryAttributeMapper prodCateAttrMapper;
    final StoreCertificateMapper storeCertMapper;
    final DailySaleProductMapper dailySaleProdMapper;
    final DailyProdTagMapper dailyProdTagMapper;
    final NoticeMapper noticeMapper;

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
            // 未设置推荐的商品，则获取档口最新上传的10个商品
            List<StoreRecommendResDTO> dailySaleTop10ProdList = this.storeProdMapper.selectLatest10List(storeId);
            return CollectionUtils.isEmpty(dailySaleTop10ProdList) ? Collections.emptyList() : this.getDefaultRecommendList(storeId, dailySaleTop10ProdList);
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
        StoreHomeTemplateOneResDTO templateOne = new StoreHomeTemplateOneResDTO().setTopLeftList(this.storeHomeMapper.selectTopLeftList(storeId));
        // 其他区域
        List<StoreHomepage> otherList = ObjectUtils.defaultIfNull(this.storeHomeMapper.selectList(new LambdaQueryWrapper<StoreHomepage>()
                .eq(StoreHomepage::getStoreId, storeId).eq(StoreHomepage::getDelFlag, Constants.UNDELETED)
                .in(StoreHomepage::getFileType, Arrays.asList(HomepageType.POPULAR_SALES.getValue(),
                        HomepageType.SLIDING_PICTURE_SMALL.getValue(), HomepageType.SEASON_NEW_PRODUCTS.getValue(),
                        HomepageType.STORE_RECOMMENDED.getValue(), HomepageType.SALES_RANKING.getValue()))), new ArrayList<>());
        // 商品ID列表
        List<Long> prodIdList = CollectionUtils.isEmpty(otherList) ? new ArrayList<>() : otherList.stream().map(StoreHomepage::getBizId).collect(Collectors.toList());
        // 筛选商品最新的50条数据
        List<StoreProduct> latest50ProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getStoreId, storeId).eq(StoreProduct::getDelFlag, Constants.UNDELETED)
                .orderByDesc(StoreProduct::getCreateTime).last("LIMIT 50"));
        CollectionUtils.addAll(prodIdList, latest50ProdList.stream().map(StoreProduct::getId).collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(latest50ProdList)) {
            return templateOne;
        }
        // 商品价格、主图、标签等
        List<StoreProdPriceAndMainPicAndTagDTO> attrList = this.storeProdMapper.selectPriceAndMainPicAndTagList(prodIdList);
        Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap = attrList.stream()
                .collect(Collectors.toMap(StoreProdPriceAndMainPicAndTagDTO::getStoreProdId, Function.identity()));

        // 顶部右侧商品2条
        List<StoreHomepage> topRightList = otherList.stream().filter(x -> Objects.equals(x.getFileType(), HomepageType.SLIDING_PICTURE_SMALL.getValue())).collect(Collectors.toList());
        List<StoreHomeTemplateItemResDTO> topRightRecommendList;
        if (CollectionUtils.isEmpty(topRightList)) {
            // 从latest50ProdList中随机选取最多2条数据
            List<StoreProduct> randomProductList = getRandomElements(latest50ProdList, 2);
            topRightRecommendList = randomProductList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        } else {
            topRightRecommendList = topRightList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        }
        // 顶部右侧推荐商品
        templateOne.setTopRightList(topRightRecommendList);

        // 店家推荐 5条
        List<StoreHomepage> storeRecommendList = otherList.stream().filter(x -> Objects.equals(x.getFileType(), HomepageType.STORE_RECOMMENDED.getValue())).collect(Collectors.toList());
        List<StoreHomeTemplateItemResDTO> recommendList;
        if (CollectionUtils.isEmpty(storeRecommendList)) {
            // 从latest50ProdList中随机选取最多5条数据
            List<StoreProduct> randomProductList = getRandomElements(latest50ProdList, 5);
            recommendList = randomProductList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        } else {
            recommendList = storeRecommendList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        }
        // 档口推荐列表
        templateOne.setRecommendList(recommendList);

        List<StoreHomeTemplateItemResDTO> popularRecommendList;
        // 人气爆款 5条
        List<StoreHomepage> popularSaleList = otherList.stream().filter(x -> Objects.equals(x.getFileType(), HomepageType.POPULAR_SALES.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(popularSaleList)) {
            // 从latest50ProdList中随机选取最多5条数据
            List<StoreProduct> randomProductList = getRandomElements(latest50ProdList, 5);
            popularRecommendList = randomProductList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        } else {
            popularRecommendList = popularSaleList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        }
        // 人气爆款列表
        templateOne.setPopularSaleList(popularRecommendList);

        // 当季新品 5条
        List<StoreHomepage> seasonNewProductsList = otherList.stream().filter(x -> Objects.equals(x.getFileType(), HomepageType.SEASON_NEW_PRODUCTS.getValue())).collect(Collectors.toList());
        List<StoreHomeTemplateItemResDTO> seasonNewRecommendList;
        if (CollectionUtils.isEmpty(seasonNewProductsList)) {
            // 从latest50ProdList中随机选取最多5条数据
            List<StoreProduct> randomProductList = getRandomElements(latest50ProdList, 5);
            seasonNewRecommendList = randomProductList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        } else {
            seasonNewRecommendList = seasonNewProductsList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        }
        // 当季新品列表
        templateOne.setNewProdList(seasonNewRecommendList);

        List<StoreHomeTemplateItemResDTO> saleRankRecommendList;
        // 销量排行 10条
        List<StoreHomepage> salesRankingList = otherList.stream().filter(x -> Objects.equals(x.getFileType(), HomepageType.SALES_RANKING.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(salesRankingList)) {
            // 从latest50ProdList中随机选取最多10条数据
            List<StoreProduct> randomProductList = getRandomElements(latest50ProdList, 10);
            saleRankRecommendList = randomProductList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        } else {
            saleRankRecommendList = salesRankingList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        }
        // 销量排行列表
        templateOne.setSaleRankList(saleRankRecommendList);
        return templateOne;
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
        // 获取档口发布的公告
        List<Notice> storeNoticeList = this.noticeMapper.selectList(new LambdaQueryWrapper<Notice>().eq(Notice::getStoreId, storeId)
                .eq(Notice::getDelFlag, Constants.UNDELETED).eq(Notice::getNoticeType, NoticeType.ANNOUNCEMENT.getValue())
                .eq(Notice::getOwnerType, NoticeOwnerType.STORE.getValue()).orderByDesc(Notice::getCreateTime));
        if (CollectionUtils.isEmpty(storeNoticeList)) {
            templateTwo.setNotice(null);
        } else {
            final Date now = new Date();
            Notice storeNotice = storeNoticeList.stream()
                    .filter(x -> Objects.equals(x.getPerpetuity(), 2) || (x.getEffectStart().before(now) && x.getEffectEnd().after(now)))
                    .findFirst().orElse(null);
            templateTwo.setNotice(BeanUtil.toBean(storeNotice, StoreHomeTemplateTwoResDTO.SHTTNoticeDTO.class));
        }
        List<StoreHomepage> otherList = this.storeHomeMapper.selectList(new LambdaQueryWrapper<StoreHomepage>()
                .eq(StoreHomepage::getStoreId, storeId).eq(StoreHomepage::getDelFlag, Constants.UNDELETED)
                .in(StoreHomepage::getFileType, Arrays.asList(HomepageType.POPULAR_SALES.getValue(),
                        HomepageType.SEASON_NEW_PRODUCTS.getValue(), HomepageType.STORE_RECOMMENDED.getValue(),
                        HomepageType.SALES_RANKING.getValue())));
        // 商品ID列表
        List<Long> prodIdList = CollectionUtils.isEmpty(otherList) ? new ArrayList<>() : otherList.stream().map(StoreHomepage::getBizId).collect(Collectors.toList());
        // 筛选商品最新的50条数据
        List<StoreProduct> latest50ProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getStoreId, storeId).eq(StoreProduct::getDelFlag, Constants.UNDELETED)
                .orderByDesc(StoreProduct::getCreateTime).last("LIMIT 50"));
        CollectionUtils.addAll(prodIdList, latest50ProdList.stream().map(StoreProduct::getId).collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(latest50ProdList)) {
            return templateTwo;
        }
        // 商品价格、主图、标签等
        List<StoreProdPriceAndMainPicAndTagDTO> attrList = this.storeProdMapper.selectPriceAndMainPicAndTagList(prodIdList);
        Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap = attrList.stream()
                .collect(Collectors.toMap(StoreProdPriceAndMainPicAndTagDTO::getStoreProdId, Function.identity()));

        // 店家推荐 5条
        List<StoreHomepage> storeRecommendList = otherList.stream().filter(x -> Objects.equals(x.getFileType(), HomepageType.STORE_RECOMMENDED.getValue())).collect(Collectors.toList());
        List<StoreHomeTemplateItemResDTO> recommendList;
        if (CollectionUtils.isEmpty(storeRecommendList)) {
            // 从latest50ProdList中随机选取最多5条数据
            List<StoreProduct> randomProductList = getRandomElements(latest50ProdList, 5);
            recommendList = randomProductList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        } else {
            recommendList = storeRecommendList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        }
        // 档口推荐列表
        templateTwo.setRecommendList(recommendList);

        List<StoreHomeTemplateItemResDTO> popularRecommendList;
        // 人气爆款 5条
        List<StoreHomepage> popularSaleList = otherList.stream().filter(x -> Objects.equals(x.getFileType(), HomepageType.POPULAR_SALES.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(popularSaleList)) {
            // 从latest50ProdList中随机选取最多5条数据
            List<StoreProduct> randomProductList = getRandomElements(latest50ProdList, 5);
            popularRecommendList = randomProductList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        } else {
            popularRecommendList = popularSaleList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        }
        // 人气爆款列表
        templateTwo.setPopularSaleList(popularRecommendList);

        // 当季新品 5条
        List<StoreHomepage> seasonNewProductsList = otherList.stream().filter(x -> Objects.equals(x.getFileType(), HomepageType.SEASON_NEW_PRODUCTS.getValue())).collect(Collectors.toList());
        List<StoreHomeTemplateItemResDTO> seasonNewRecommendList;
        if (CollectionUtils.isEmpty(seasonNewProductsList)) {
            // 从latest50ProdList中随机选取最多5条数据
            List<StoreProduct> randomProductList = getRandomElements(latest50ProdList, 5);
            seasonNewRecommendList = randomProductList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        } else {
            seasonNewRecommendList = seasonNewProductsList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        }
        // 当季新品列表
        templateTwo.setNewProdList(seasonNewRecommendList);

        List<StoreHomeTemplateItemResDTO> saleRankRecommendList;
        // 销量排行 10条
        List<StoreHomepage> salesRankingList = otherList.stream().filter(x -> Objects.equals(x.getFileType(), HomepageType.SALES_RANKING.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(salesRankingList)) {
            // 从latest50ProdList中随机选取最多10条数据
            List<StoreProduct> randomProductList = getRandomElements(latest50ProdList, 10);
            saleRankRecommendList = randomProductList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        } else {
            saleRankRecommendList = salesRankingList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        }
        // 销量排行列表
        templateTwo.setSaleRankList(saleRankRecommendList);
        return templateTwo;
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
        StoreHomeTemplateThirdResDTO templateThird = new StoreHomeTemplateThirdResDTO().setTopLeftList(this.storeHomeMapper.selectTopLeftList(storeId));
        // 顶部右侧商品 及 店家推荐 和 销量排行
        List<StoreHomepage> otherList = this.storeHomeMapper.selectList(new LambdaQueryWrapper<StoreHomepage>()
                .eq(StoreHomepage::getStoreId, storeId).eq(StoreHomepage::getDelFlag, Constants.UNDELETED)
                .in(StoreHomepage::getFileType, Arrays.asList(HomepageType.SLIDING_PICTURE_SMALL.getValue(),
                        HomepageType.STORE_RECOMMENDED.getValue(), HomepageType.SALES_RANKING.getValue())));
        // 商品ID列表
        List<Long> prodIdList = CollectionUtils.isEmpty(otherList) ? new ArrayList<>() : otherList.stream().map(StoreHomepage::getBizId).collect(Collectors.toList());
        // 筛选商品最新的50条数据
        List<StoreProduct> latest50ProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getStoreId, storeId).eq(StoreProduct::getDelFlag, Constants.UNDELETED)
                .orderByDesc(StoreProduct::getCreateTime).last("LIMIT 50"));
        CollectionUtils.addAll(prodIdList, latest50ProdList.stream().map(StoreProduct::getId).collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(latest50ProdList)) {
            return templateThird;
        }
        // 商品价格、主图、标签等
        List<StoreProdPriceAndMainPicAndTagDTO> attrList = this.storeProdMapper.selectPriceAndMainPicAndTagList(prodIdList);
        Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap = attrList.stream()
                .collect(Collectors.toMap(StoreProdPriceAndMainPicAndTagDTO::getStoreProdId, Function.identity()));

        // 顶部右侧商品2条
        List<StoreHomepage> topRightList = otherList.stream().filter(x -> Objects.equals(x.getFileType(), HomepageType.SLIDING_PICTURE_SMALL.getValue())).collect(Collectors.toList());
        List<StoreHomeTemplateItemResDTO> topRightRecommendList;
        if (CollectionUtils.isEmpty(topRightList)) {
            // 从latest50ProdList中随机选取最多2条数据
            List<StoreProduct> randomProductList = getRandomElements(latest50ProdList, 2);
            topRightRecommendList = randomProductList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        } else {
            topRightRecommendList = topRightList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        }
        // 顶部右侧推荐商品
        templateThird.setTopRightList(topRightRecommendList);

        // 店家推荐 10条
        List<StoreHomepage> storeRecommendList = otherList.stream().filter(x -> Objects.equals(x.getFileType(), HomepageType.STORE_RECOMMENDED.getValue())).collect(Collectors.toList());
        List<StoreHomeTemplateItemResDTO> recommendList;
        if (CollectionUtils.isEmpty(storeRecommendList)) {
            // 从latest50ProdList中随机选取最多5条数据
            List<StoreProduct> randomProductList = getRandomElements(latest50ProdList, 10);
            recommendList = randomProductList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        } else {
            recommendList = storeRecommendList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        }
        // 档口推荐列表
        templateThird.setRecommendList(recommendList);

        List<StoreHomeTemplateItemResDTO> saleRankRecommendList;
        // 销量排行 10条
        List<StoreHomepage> salesRankingList = otherList.stream().filter(x -> Objects.equals(x.getFileType(), HomepageType.SALES_RANKING.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(salesRankingList)) {
            // 从latest50ProdList中随机选取最多10条数据
            List<StoreProduct> randomProductList = getRandomElements(latest50ProdList, 10);
            saleRankRecommendList = randomProductList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        } else {
            saleRankRecommendList = salesRankingList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        }
        // 销量排行列表
        templateThird.setSaleRankList(saleRankRecommendList);
        return templateThird;
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
        // 商品ID列表
        List<Long> prodIdList = CollectionUtils.isEmpty(otherList) ? new ArrayList<>() : otherList.stream().map(StoreHomepage::getBizId).collect(Collectors.toList());
        // 筛选商品最新的50条数据
        List<StoreProduct> latest50ProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getStoreId, storeId).eq(StoreProduct::getDelFlag, Constants.UNDELETED)
                .orderByDesc(StoreProduct::getCreateTime).last("LIMIT 50"));
        CollectionUtils.addAll(prodIdList, latest50ProdList.stream().map(StoreProduct::getId).collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(latest50ProdList)) {
            return templateFour;
        }
        // 商品价格、主图、标签等
        List<StoreProdPriceAndMainPicAndTagDTO> attrList = this.storeProdMapper.selectPriceAndMainPicAndTagList(prodIdList);
        Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap = attrList.stream()
                .collect(Collectors.toMap(StoreProdPriceAndMainPicAndTagDTO::getStoreProdId, Function.identity()));

        // 顶部右侧商品2条
        List<StoreHomepage> topRightList = otherList.stream().filter(x -> Objects.equals(x.getFileType(), HomepageType.SLIDING_PICTURE_SMALL.getValue())).collect(Collectors.toList());
        List<StoreHomeTemplateItemResDTO> topRightRecommendList;
        if (CollectionUtils.isEmpty(topRightList)) {
            // 从latest50ProdList中随机选取最多2条数据
            List<StoreProduct> randomProductList = getRandomElements(latest50ProdList, 2);
            topRightRecommendList = randomProductList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        } else {
            topRightRecommendList = topRightList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        }
        // 顶部右侧推荐商品
        templateFour.setTopRightList(topRightRecommendList);

        // 店家推荐 5条
        List<StoreHomepage> storeRecommendList = otherList.stream().filter(x -> Objects.equals(x.getFileType(), HomepageType.STORE_RECOMMENDED.getValue())).collect(Collectors.toList());
        List<StoreHomeTemplateItemResDTO> recommendList;
        if (CollectionUtils.isEmpty(storeRecommendList)) {
            // 从latest50ProdList中随机选取最多5条数据
            List<StoreProduct> randomProductList = getRandomElements(latest50ProdList, 5);
            recommendList = randomProductList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        } else {
            recommendList = storeRecommendList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        }
        // 档口推荐列表
        templateFour.setRecommendList(recommendList);

        List<StoreHomeTemplateItemResDTO> popularRecommendList;
        // 人气爆款 5条
        List<StoreHomepage> popularSaleList = otherList.stream().filter(x -> Objects.equals(x.getFileType(), HomepageType.POPULAR_SALES.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(popularSaleList)) {
            // 从latest50ProdList中随机选取最多5条数据
            List<StoreProduct> randomProductList = getRandomElements(latest50ProdList, 5);
            popularRecommendList = randomProductList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        } else {
            popularRecommendList = popularSaleList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        }
        // 人气爆款列表
        templateFour.setPopularSaleList(popularRecommendList);

        // 当季新品 5条
        List<StoreHomepage> seasonNewProductsList = otherList.stream().filter(x -> Objects.equals(x.getFileType(), HomepageType.SEASON_NEW_PRODUCTS.getValue())).collect(Collectors.toList());
        List<StoreHomeTemplateItemResDTO> seasonNewRecommendList;
        if (CollectionUtils.isEmpty(seasonNewProductsList)) {
            // 从latest50ProdList中随机选取最多5条数据
            List<StoreProduct> randomProductList = getRandomElements(latest50ProdList, 5);
            seasonNewRecommendList = randomProductList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        } else {
            seasonNewRecommendList = seasonNewProductsList.stream().map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        }
        // 当季新品列表
        templateFour.setNewProdList(seasonNewRecommendList);
        return templateFour;
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
        // 获取档口发布的公告
        List<Notice> storeNoticeList = this.noticeMapper.selectList(new LambdaQueryWrapper<Notice>().eq(Notice::getStoreId, storeId)
                .eq(Notice::getDelFlag, Constants.UNDELETED).eq(Notice::getNoticeType, NoticeType.ANNOUNCEMENT.getValue())
                .eq(Notice::getOwnerType, NoticeOwnerType.STORE.getValue()).orderByDesc(Notice::getCreateTime));
        if (CollectionUtils.isEmpty(storeNoticeList)) {
            templateFive.setNotice(null);
        } else {
            final Date now = new Date();
            Notice storeNotice = storeNoticeList.stream()
                    .filter(x -> Objects.equals(x.getPerpetuity(), 2) || (x.getEffectStart().before(now) && x.getEffectEnd().after(now)))
                    .findFirst().orElse(null);
//            templateFive.setNotice(BeanUtil.toBean(storeNotice, StoreHomeTemplateTwoResDTO.SHTTNoticeDTO.class));
        }


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


    /**
     * 从列表中随机选择指定数量的元素
     *
     * @param list 列表
     * @param count 需要选择的数量
     * @param <T> 元素类型
     * @return 随机选择的元素列表
     */
    private <T> List<T> getRandomElements(List<T> list, int count) {
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        // 创建列表副本以避免修改原列表
        List<T> copyList = new ArrayList<>(list);
        Collections.shuffle(copyList);
        // 返回不超过列表大小和所需数量的元素
        return copyList.stream().limit(Math.min(count, copyList.size())).collect(Collectors.toList());
    }

    /**
     * 获取默认的推荐推荐商品
     *
     * @param storeId                档口ID
     * @param dailySaleTop10ProdList 近5日销量排名前10的商品
     * @return List<StoreRecommendResDTO>
     */
    private List<StoreRecommendResDTO> getDefaultRecommendList(Long storeId, List<StoreRecommendResDTO> dailySaleTop10ProdList) {
        // 获取商品标签
        List<DailyProdTag> prodTagList = this.dailyProdTagMapper.selectList(new LambdaQueryWrapper<DailyProdTag>()
                .eq(DailyProdTag::getStoreId, storeId).eq(DailyProdTag::getDelFlag, Constants.UNDELETED)
                .in(DailyProdTag::getStoreProdId, dailySaleTop10ProdList.stream().map(StoreRecommendResDTO::getStoreProdId).collect(Collectors.toList())));
        Map<String, List<String>> tagMap = CollectionUtils.isEmpty(prodTagList) ? new HashMap<>()
                : prodTagList.stream().collect(Collectors.groupingBy(x -> x.getStoreProdId().toString(), Collectors.mapping(DailyProdTag::getTag, Collectors.toList())));
        // 获取商品主图及视频等
        List<StoreProductFile> prodFileList = this.prodFileMapper.selectList(new LambdaQueryWrapper<StoreProductFile>()
                .eq(StoreProductFile::getStoreId, storeId).eq(StoreProductFile::getDelFlag, Constants.UNDELETED).eq(StoreProductFile::getOrderNum, ORDER_NUM_1)
                .in(StoreProductFile::getStoreProdId, dailySaleTop10ProdList.stream().map(StoreRecommendResDTO::getStoreProdId).collect(Collectors.toList()))
                .in(StoreProductFile::getFileType, Arrays.asList(FileType.MAIN_PIC.getValue(), FileType.MAIN_PIC_VIDEO.getValue())));
        // 档口商品主图map
        Map<String, Long> mainPicMap = prodFileList.stream().filter(x -> Objects.equals(x.getFileType(), FileType.MAIN_PIC.getValue()))
                .collect(Collectors.toMap(x -> x.getStoreProdId().toString(), StoreProductFile::getFileId));
        // 档口商品视频map
        Map<String, Long> videoMap = prodFileList.stream().filter(x -> Objects.equals(x.getFileType(), FileType.MAIN_PIC_VIDEO.getValue()))
                .collect(Collectors.toMap(x -> x.getStoreProdId().toString(), StoreProductFile::getFileId));
        List<SysFile> fileList = this.fileMapper.selectList(new LambdaQueryWrapper<SysFile>().eq(SysFile::getDelFlag, Constants.UNDELETED)
                .in(SysFile::getId, prodFileList.stream().map(StoreProductFile::getFileId).collect(Collectors.toList())));
        Map<Long, String> fileMap = CollectionUtils.isEmpty(fileList) ? new HashMap<>() : fileList.stream().collect(Collectors.toMap(SysFile::getId, SysFile::getFileUrl));
        dailySaleTop10ProdList.forEach(x -> {
            x.setTags(tagMap.getOrDefault(x.getStoreProdId(), new ArrayList<>()));
            x.setMainPicUrl(fileMap.get(mainPicMap.get(x.getStoreProdId())));
            x.setHasVideo(videoMap.containsKey(x.getStoreProdId()));
        });
        return dailySaleTop10ProdList;
    }


}
