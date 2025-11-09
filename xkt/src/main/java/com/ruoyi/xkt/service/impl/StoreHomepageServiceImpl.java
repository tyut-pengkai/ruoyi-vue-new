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

import static com.ruoyi.common.constant.Constants.ORDER_NUM_1;

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
        final List<Long> fileIdList = homeList.stream().map(StoreHomepage::getFileId).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
        List<SysFile> fileList = Optional.ofNullable(this.fileMapper.selectList(new LambdaQueryWrapper<SysFile>()
                        .in(SysFile::getId, fileIdList).eq(SysFile::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("文件不存在", HttpStatus.ERROR));
        Map<Long, SysFile> fileMap = fileList.stream().collect(Collectors.toMap(SysFile::getId, Function.identity()));
        // 档口商品ID列表
        List<Long> storeProdIdList = homeList.stream()
                .filter(x -> Objects.equals(x.getDisplayType(), HomepageJumpType.JUMP_PRODUCT.getValue())).map(StoreHomepage::getBizId).collect(Collectors.toList());
        // 所有的档口商品ID
        List<StoreProduct> storeProdList = Optional.ofNullable(this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                        .eq(StoreProduct::getStoreId, storeId).in(StoreProduct::getId, storeProdIdList).eq(StoreProduct::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口商品不存在", HttpStatus.ERROR));
        Map<Long, StoreProduct> storeProdMap = CollectionUtils.isEmpty(storeProdList) ? new HashMap<>()
                : storeProdList.stream().collect(Collectors.toMap(StoreProduct::getId, Function.identity()));
        // 查找排名第一个商品主图列表
        List<StoreProdMainPicDTO> mainPicList = this.prodFileMapper.selectMainPicByStoreProdIdList(storeProdIdList, FileType.MAIN_PIC.getValue(), ORDER_NUM_1);
        Map<Long, String> mainPicMap = CollectionUtils.isEmpty(mainPicList) ? new HashMap<>() : mainPicList.stream()
                .collect(Collectors.toMap(StoreProdMainPicDTO::getStoreProdId, StoreProdMainPicDTO::getFileUrl));
        // 轮播图
        List<StoreHomeDecorationResDTO.DecorationDTO> bigBannerList = homeList.stream()
                .filter(x -> Objects.equals(x.getFileType(), HomepageType.SLIDING_PICTURE.getValue()))
                .map(x -> {
                    StoreHomeDecorationResDTO.DecorationDTO decorationDTO = BeanUtil.toBean(x, StoreHomeDecorationResDTO.DecorationDTO.class)
                            .setFileType(x.getFileType()).setFileUrl(fileMap.containsKey(x.getFileId()) ? fileMap.get(x.getFileId()).getFileUrl() : "");
                    // 跳转到商品
                    if (Objects.equals(x.getDisplayType(), HomepageJumpType.JUMP_PRODUCT.getValue())) {
                        decorationDTO.setBizName(storeProdMap.containsKey(x.getBizId()) ? storeProdMap.get(x.getBizId()).getProdArtNum() : "");
                        // 跳转到档口首页
                    } else if (Objects.equals(x.getDisplayType(), HomepageJumpType.JUMP_STORE.getValue())) {
                        decorationDTO.setBizName(ObjectUtils.isEmpty(x.getBizId()) ? "" : store.getStoreName());
                    }
                    return decorationDTO;
                }).collect(Collectors.toList());
        // 其它图部分
        List<StoreHomeDecorationResDTO.DecorationDTO> decorList = homeList.stream().filter(x -> !Objects.equals(x.getFileType(), HomepageType.SLIDING_PICTURE.getValue()))
                .map(x -> BeanUtil.toBean(x, StoreHomeDecorationResDTO.DecorationDTO.class)
                        .setBizName(storeProdMap.containsKey(x.getBizId()) ? storeProdMap.get(x.getBizId()).getProdArtNum() : null)
                        .setFileType(x.getFileType()).setFileUrl(mainPicMap.get(x.getBizId())))
                .collect(Collectors.toList());
        return new StoreHomeDecorationResDTO() {{
            setStoreId(storeId);
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
        // 筛选商品最新的30条数据
        List<StoreProduct> latest50ProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getStoreId, storeId).eq(StoreProduct::getDelFlag, Constants.UNDELETED)
                .orderByDesc(StoreProduct::getCreateTime).last("LIMIT 30"));
        CollectionUtils.addAll(prodIdList, latest50ProdList.stream().map(StoreProduct::getId).collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(latest50ProdList)) {
            return templateOne;
        }
        // 商品价格、主图、标签等
        List<StoreProdPriceAndMainPicAndTagDTO> attrList = this.storeProdMapper.selectPriceAndMainPicAndTagList(prodIdList);
        Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap = attrList.stream()
                .collect(Collectors.toMap(StoreProdPriceAndMainPicAndTagDTO::getStoreProdId, Function.identity()));
        // 顶部右侧推荐商品
        templateOne.setTopRightList(this.getTopRightList(otherList, latest50ProdList, attrMap, 2));
        // 档口推荐列表
        templateOne.setRecommendList(this.getStoreRecommendProdList(otherList, latest50ProdList, attrMap, 5));
        // 人气爆款列表
        templateOne.setPopularSaleList(this.getPopularSaleList(otherList, latest50ProdList, attrMap, 5));
        // 当季新品列表
        templateOne.setNewProdList(this.getSeasonNewProdList(otherList, latest50ProdList, attrMap, 5));
        // 销量排行列表
        templateOne.setSaleRankList(this.getSaleRankList(otherList, latest50ProdList, attrMap, 10));
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
                .orderByDesc(StoreProduct::getCreateTime).last("LIMIT 30"));
        CollectionUtils.addAll(prodIdList, latest50ProdList.stream().map(StoreProduct::getId).collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(latest50ProdList)) {
            return templateTwo;
        }
        // 商品价格、主图、标签等
        List<StoreProdPriceAndMainPicAndTagDTO> attrList = this.storeProdMapper.selectPriceAndMainPicAndTagList(prodIdList);
        Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap = attrList.stream()
                .collect(Collectors.toMap(StoreProdPriceAndMainPicAndTagDTO::getStoreProdId, Function.identity()));
        // 档口推荐列表
        templateTwo.setRecommendList(this.getStoreRecommendProdList(otherList, latest50ProdList, attrMap, 5));
        // 人气爆款列表
        templateTwo.setPopularSaleList(this.getPopularSaleList(otherList, latest50ProdList, attrMap, 5));
        // 当季新品列表
        templateTwo.setNewProdList(this.getSeasonNewProdList(otherList, latest50ProdList, attrMap, 5));
        // 销量排行列表
        templateTwo.setSaleRankList(this.getSaleRankList(otherList, latest50ProdList, attrMap, 10));
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
                .orderByDesc(StoreProduct::getCreateTime).last("LIMIT 30"));
        CollectionUtils.addAll(prodIdList, latest50ProdList.stream().map(StoreProduct::getId).collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(latest50ProdList)) {
            return templateThird;
        }
        // 商品价格、主图、标签等
        List<StoreProdPriceAndMainPicAndTagDTO> attrList = this.storeProdMapper.selectPriceAndMainPicAndTagList(prodIdList);
        Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap = attrList.stream()
                .collect(Collectors.toMap(StoreProdPriceAndMainPicAndTagDTO::getStoreProdId, Function.identity()));
        // 顶部右侧推荐商品
        templateThird.setTopRightList(this.getTopRightList(otherList, latest50ProdList, attrMap, 2));
        // 档口推荐列表
        templateThird.setRecommendList(this.getStoreRecommendProdList(otherList, latest50ProdList, attrMap, 10));
        // 销量排行列表
        templateThird.setSaleRankList(this.getSaleRankList(otherList, latest50ProdList, attrMap, 10));
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
                .orderByDesc(StoreProduct::getCreateTime).last("LIMIT 30"));
        CollectionUtils.addAll(prodIdList, latest50ProdList.stream().map(StoreProduct::getId).collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(latest50ProdList)) {
            return templateFour;
        }
        // 商品价格、主图、标签等
        List<StoreProdPriceAndMainPicAndTagDTO> attrList = this.storeProdMapper.selectPriceAndMainPicAndTagList(prodIdList);
        Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap = attrList.stream()
                .collect(Collectors.toMap(StoreProdPriceAndMainPicAndTagDTO::getStoreProdId, Function.identity()));
        // 顶部右侧推荐商品
        templateFour.setTopRightList(this.getTopRightList(otherList, latest50ProdList, attrMap, 2));
        // 档口推荐列表
        templateFour.setRecommendList(this.getStoreRecommendProdList(otherList, latest50ProdList, attrMap, 5));
        // 人气爆款列表
        templateFour.setPopularSaleList(this.getPopularSaleList(otherList, latest50ProdList, attrMap, 5));
        // 当季新品列表
        templateFour.setNewProdList(this.getSeasonNewProdList(otherList, latest50ProdList, attrMap, 5));
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
            templateFive.setNotice(BeanUtil.toBean(storeNotice, StoreHomeTemplateFiveResDTO.SHTFNoticeDTO.class));
        }
        // 其他区域
        List<StoreHomepage> otherList = this.storeHomeMapper.selectList(new LambdaQueryWrapper<StoreHomepage>()
                .eq(StoreHomepage::getStoreId, storeId).eq(StoreHomepage::getDelFlag, Constants.UNDELETED)
                .in(StoreHomepage::getFileType, Arrays.asList(HomepageType.POPULAR_SALES.getValue(),
                        HomepageType.SLIDING_PICTURE_SMALL.getValue(), HomepageType.SEASON_NEW_PRODUCTS.getValue(),
                        HomepageType.STORE_RECOMMENDED.getValue(), HomepageType.SALES_RANKING.getValue())));
        // 商品ID列表
        List<Long> prodIdList = CollectionUtils.isEmpty(otherList) ? new ArrayList<>() : otherList.stream().map(StoreHomepage::getBizId).collect(Collectors.toList());
        // 筛选商品最新的50条数据
        List<StoreProduct> latest50ProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getStoreId, storeId).eq(StoreProduct::getDelFlag, Constants.UNDELETED)
                .orderByDesc(StoreProduct::getCreateTime).last("LIMIT 30"));
        CollectionUtils.addAll(prodIdList, latest50ProdList.stream().map(StoreProduct::getId).collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(latest50ProdList)) {
            return templateFive;
        }
        // 商品价格、主图、标签等
        List<StoreProdPriceAndMainPicAndTagDTO> attrList = this.storeProdMapper.selectPriceAndMainPicAndTagList(prodIdList);
        Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap = attrList.stream()
                .collect(Collectors.toMap(StoreProdPriceAndMainPicAndTagDTO::getStoreProdId, Function.identity()));
        // 顶部右侧推荐商品
        templateFive.setTopRightList(this.getTopRightList(otherList, latest50ProdList, attrMap, 2));
        // 档口推荐列表
        templateFive.setRecommendList(this.getStoreRecommendProdList(otherList, latest50ProdList, attrMap, 10));
        // 销量排行列表
        templateFive.setSaleRankList(this.getSaleRankList(otherList, latest50ProdList, attrMap, 10));
        return templateFive;
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
            List<SysFile> bigBannerFileList = homepageDTO.getBigBannerList().stream().filter(x -> StringUtils.isNotBlank(x.getFileUrl()))
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
     * @param list  列表
     * @param count 需要选择的数量
     * @param <T>   元素类型
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

    /**
     * 设置档口首页右侧商品列表
     *
     * @param otherList        其它部分列表数据
     * @param latest50ProdList 档口最新50条商品数据
     * @param attrMap          商品属性数据
     * @param count
     * @return
     */
    private List<StoreHomeTemplateItemResDTO> getTopRightList(List<StoreHomepage> otherList, List<StoreProduct> latest50ProdList,
                                                              Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap, int count) {
        // 顶部右侧商品2条
        List<StoreHomepage> topRightList = otherList.stream().filter(x -> Objects.equals(x.getFileType(), HomepageType.SLIDING_PICTURE_SMALL.getValue())).collect(Collectors.toList());
        List<StoreHomeTemplateItemResDTO> topRightRecommendList;
        if (CollectionUtils.isEmpty(topRightList)) {
            // 从latest50ProdList中随机选取最多2条数据
            List<StoreProduct> randomProductList = getRandomElements(latest50ProdList, count);
            topRightRecommendList = randomProductList.stream().filter(x -> attrMap.containsKey(x.getId())).map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        } else {
            topRightRecommendList = topRightList.stream().filter(x -> attrMap.containsKey(x.getBizId())).map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getBizId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        }
        return topRightRecommendList;
    }

    /**
     * 获取档口推荐列表
     *
     * @param otherList        档口首页各部分数据
     * @param latest50ProdList 档口最新的50条商品
     * @param attrMap          属性map
     * @param count            随机获取数据条数
     * @return
     */
    private List<StoreHomeTemplateItemResDTO> getStoreRecommendProdList(List<StoreHomepage> otherList, List<StoreProduct> latest50ProdList,
                                                                        Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap, int count) {
        // 店家推荐 5条
        List<StoreHomepage> storeRecommendList = otherList.stream().filter(x -> Objects.equals(x.getFileType(), HomepageType.STORE_RECOMMENDED.getValue())).collect(Collectors.toList());
        List<StoreHomeTemplateItemResDTO> recommendList;
        if (CollectionUtils.isEmpty(storeRecommendList)) {
            // 从latest50ProdList中随机选取最多5条数据
            List<StoreProduct> randomProductList = getRandomElements(latest50ProdList, count);
            recommendList = randomProductList.stream().filter(x -> attrMap.containsKey(x.getId())).map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        } else {
            recommendList = storeRecommendList.stream().filter(x -> attrMap.containsKey(x.getBizId())).map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getBizId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        }
        return recommendList;
    }

    /**
     * 获取人气爆款列表
     *
     * @param otherList        档口首页其它部分数据
     * @param latest50ProdList 档口最新50条商品数据
     * @param attrMap          属性map
     * @param count            截取的数量
     * @return
     */
    private List<StoreHomeTemplateItemResDTO> getPopularSaleList(List<StoreHomepage> otherList, List<StoreProduct> latest50ProdList,
                                                                 Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap, int count) {

        List<StoreHomeTemplateItemResDTO> popularRecommendList;
        // 人气爆款 5条
        List<StoreHomepage> popularSaleList = otherList.stream().filter(x -> Objects.equals(x.getFileType(), HomepageType.POPULAR_SALES.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(popularSaleList)) {
            // 从latest50ProdList中随机选取最多5条数据
            List<StoreProduct> randomProductList = getRandomElements(latest50ProdList, count);
            popularRecommendList = randomProductList.stream().filter(x -> attrMap.containsKey(x.getId())).map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        } else {
            popularRecommendList = popularSaleList.stream().filter(x -> attrMap.containsKey(x.getBizId())).map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getBizId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        }
        return popularRecommendList;
    }

    /**
     * 获取当季新品列表数据
     *
     * @param otherList        档口首页其它部分数据
     * @param latest50ProdList 档口最新的50条商品
     * @param attrMap          属性map
     * @param count            截取数据
     * @return
     */
    private List<StoreHomeTemplateItemResDTO> getSeasonNewProdList(List<StoreHomepage> otherList, List<StoreProduct> latest50ProdList,
                                                                   Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap, int count) {
        // 当季新品 5条
        List<StoreHomepage> seasonNewProductsList = otherList.stream().filter(x -> Objects.equals(x.getFileType(), HomepageType.SEASON_NEW_PRODUCTS.getValue())).collect(Collectors.toList());
        List<StoreHomeTemplateItemResDTO> seasonNewRecommendList;
        if (CollectionUtils.isEmpty(seasonNewProductsList)) {
            // 从latest50ProdList中随机选取最多5条数据
            List<StoreProduct> randomProductList = getRandomElements(latest50ProdList, count);
            seasonNewRecommendList = randomProductList.stream().filter(x -> attrMap.containsKey(x.getId())).map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        } else {
            seasonNewRecommendList = seasonNewProductsList.stream().filter(x -> attrMap.containsKey(x.getBizId())).map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getBizId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        }
        return seasonNewRecommendList;
    }

    /**
     * 获取销量排行列表
     *
     * @param otherList        档口首页其它部分数据
     * @param latest50ProdList 档口最新的50条商品
     * @param attrMap          商品属性map
     * @param count            截取数量
     * @return
     */
    private List<StoreHomeTemplateItemResDTO> getSaleRankList(List<StoreHomepage> otherList, List<StoreProduct> latest50ProdList,
                                                              Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap, int count) {
        List<StoreHomeTemplateItemResDTO> saleRankRecommendList;
        // 销量排行 10条
        List<StoreHomepage> salesRankingList = otherList.stream().filter(x -> Objects.equals(x.getFileType(), HomepageType.SALES_RANKING.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(salesRankingList)) {
            // 从latest50ProdList中随机选取最多10条数据
            List<StoreProduct> randomProductList = getRandomElements(latest50ProdList, count);
            saleRankRecommendList = randomProductList.stream().filter(x -> attrMap.containsKey(x.getId())).map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        } else {
            saleRankRecommendList = salesRankingList.stream().filter(x -> attrMap.containsKey(x.getBizId())).map(x -> {
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(x.getBizId());
                return BeanUtil.toBean(dto, StoreHomeTemplateItemResDTO.class)
                        .setDisplayType(AdDisplayType.PRODUCT.getValue()).setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setTags(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTagStr()) ? StrUtil.split(dto.getTagStr(), ",") : null);
            }).collect(Collectors.toList());
        }
        return saleRankRecommendList;
    }


}
