package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.CreateResponse;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.AdValidator;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.HtmlValidator;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.es.EsClientWrapper;
import com.ruoyi.framework.notice.fs.FsNotice;
import com.ruoyi.framework.oss.OSSClientWrapper;
import com.ruoyi.system.domain.dto.productCategory.ProdCateDTO;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.dto.es.ESProductDTO;
import com.ruoyi.xkt.dto.picture.ProductPicSyncDTO;
import com.ruoyi.xkt.dto.picture.ProductPicSyncResultDTO;
import com.ruoyi.xkt.dto.storeColor.StoreColorDTO;
import com.ruoyi.xkt.dto.storeProdCateAttr.StoreProdCateAttrDTO;
import com.ruoyi.xkt.dto.storeProdColor.StoreProdColorDTO;
import com.ruoyi.xkt.dto.storeProdColorSize.StoreProdSizeDTO;
import com.ruoyi.xkt.dto.storeProdProcess.StoreProdProcessDTO;
import com.ruoyi.xkt.dto.storeProdSvc.StoreProdSvcDTO;
import com.ruoyi.xkt.dto.storeProduct.*;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdFileDTO;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdFileResDTO;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdMainPicDTO;
import com.ruoyi.xkt.dto.userBrowsingHistory.UserBrowsingHisDTO;
import com.ruoyi.xkt.dto.userNotice.UserFocusAndFavUserIdDTO;
import com.ruoyi.xkt.enums.*;
import com.ruoyi.xkt.manager.TencentAuthManager;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IPictureService;
import com.ruoyi.xkt.service.IStoreProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ruoyi.common.constant.Constants.*;

/**
 * 档口商品Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StoreProductServiceImpl implements IStoreProductService {

    final StoreProductMapper storeProdMapper;
    final StoreProductFileMapper storeProdFileMapper;
    final StoreProductCategoryAttributeMapper storeProdCateAttrMapper;
    final StoreProductColorMapper storeProdColorMapper;
    final StoreProductServiceMapper storeProdSvcMapper;
    final StoreProductDetailMapper storeProdDetailMapper;
    final StoreColorMapper storeColorMapper;
    final SysFileMapper fileMapper;
    final StoreProductColorSizeMapper storeProdColorSizeMapper;
    final StoreProductProcessMapper storeProdProcMapper;
    final StoreMapper storeMapper;
    final EsClientWrapper esClientWrapper;
    final SysProductCategoryMapper prodCateMapper;
    final StoreProductStockMapper prodStockMapper;
    final StoreCustomerMapper storeCusMapper;
    final StoreCustomerProductDiscountMapper storeCusProdDiscMapper;
    final IPictureService pictureService;
    final StoreProductStatisticsMapper storeProductStatisticsMapper;
    final StoreHomepageMapper storeHomepageMapper;
    final RedisCache redisCache;
    final NoticeMapper noticeMapper;
    final UserNoticeMapper userNoticeMapper;
    final UserSubscriptionsMapper userSubMapper;
    final OSSClientWrapper ossClient;
    final TencentAuthManager tencentAuthManager;
    final FsNotice fsNotice;


    /**
     * 查询档口商品
     *
     * @param storeProdId 档口商品主键
     * @return 档口商品
     */
    @Override
    @Transactional(readOnly = true)
    public StoreProdResDTO selectStoreProductByStoreProdId(Long storeProdId) {
        StoreProduct storeProd = Optional.ofNullable(this.storeProdMapper.selectOne(new LambdaQueryWrapper<StoreProduct>()
                        .eq(StoreProduct::getId, storeProdId).eq(StoreProduct::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口商品不存在!", HttpStatus.ERROR));
        StoreProdResDTO storeProdResDTO = BeanUtil.toBean(storeProd, StoreProdResDTO.class).setStoreProdId(storeProd.getId());
        // 档口文件（商品主图、主图视频、下载的商品详情）
        List<StoreProdFileResDTO> fileResList = this.storeProdFileMapper.selectListByStoreProdId(storeProdId);
        // 档口类目属性列表
        StoreProductCategoryAttribute cateAttr = this.storeProdCateAttrMapper.selectByStoreProdId(storeProdId);
        // 档口所有颜色列表
        List<StoreColorDTO> allColorList = this.storeColorMapper.selectListByStoreProdId(storeProd.getStoreId());
        // 档口当前商品颜色列表
        List<StoreProdColorDTO> colorList = this.storeProdColorMapper.selectListByStoreProdId(storeProdId);
        // 颜色与内里材质映射
        Map<String, String> colorLiningMaterialMap = colorList.stream().collect(Collectors
                .toMap(StoreProdColorDTO::getColorName, StoreProdColorDTO::getShoeUpperLiningMaterial, (v1, v2) -> v2));
        // 档口商品颜色尺码价格列表
        List<StoreProdSizeDTO> sizeList = this.storeProdColorSizeMapper.selectListByStoreProdId(storeProdId);
        sizeList.forEach(size -> size.setShoeUpperLiningMaterial(colorLiningMaterialMap.get(size.getColorName())));
        // 档口商品详情
        StoreProductDetail prodDetail = this.storeProdDetailMapper.selectByStoreProdId(storeProdId);
        // 档口服务承诺
        StoreProductService storeProductSvc = this.storeProdSvcMapper.selectByStoreProdId(storeProdId);
        // 档口生产工艺信息
        StoreProductProcess prodProcess = this.storeProdProcMapper.selectByStoreProdId(storeProdId);
        return storeProdResDTO.setFileList(fileResList).setAllColorList(allColorList)
                .setDetail(ObjectUtils.isNotEmpty(prodDetail) ? prodDetail.getDetail() : "")
                .setColorList(colorList).setSizeList(sizeList).setCateAttr(BeanUtil.toBean(cateAttr, StoreProdCateAttrDTO.class))
                .setSvc(BeanUtil.toBean(storeProductSvc, StoreProdSvcDTO.class)).setProcess(BeanUtil.toBean(prodProcess, StoreProdProcessDTO.class));
    }

    /**
     * 根据页面请求DTO查询商店产品分页信息
     *
     * @param pageDTO 页面请求DTO，包含分页查询条件
     * @return 商店产品分页响应DTO列表
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StoreProdPageResDTO> page(StoreProdPageDTO pageDTO) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(pageDTO.getStoreId())) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        // 如果是已删除状态，则只查询已删除数据
        pageDTO.setDelFlag(pageDTO.getProdStatusList().stream().allMatch(x -> Objects.equals(x, EProductStatus.REMOVED.getValue()))
                ? Constants.DELETED : Constants.UNDELETED);
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        // 调用Mapper方法查询商店产品分页信息
        List<StoreProdPageResDTO> prodList = storeProdColorMapper.selectStoreProdColorPage(pageDTO);
        if (CollectionUtils.isNotEmpty(prodList)) {
            // 查询商品的 最低售价 及 价格尺码
            List<StoreProductColorSize> prodColorSizeList = this.storeProdColorSizeMapper.selectList(new LambdaQueryWrapper<StoreProductColorSize>()
                    .in(StoreProductColorSize::getStoreProdId, prodList.stream().map(StoreProdPageResDTO::getStoreProdId).collect(Collectors.toList()))
                    .in(StoreProductColorSize::getStoreColorId, prodList.stream().map(StoreProdPageResDTO::getStoreColorId).collect(Collectors.toList()))
                    .eq(StoreProductColorSize::getDelFlag, Constants.UNDELETED).eq(StoreProductColorSize::getStandard, 1));
            // key storeProdId:storeColorId value list
            Map<String, List<StoreProductColorSize>> prodColorSizeMap = prodColorSizeList.stream().collect(Collectors
                    .groupingBy(x -> x.getStoreProdId() + ":" + x.getStoreColorId()));
            prodList.forEach(x -> {
                List<StoreProductColorSize> colorSizeList = prodColorSizeMap.get(x.getStoreProdId() + ":" + x.getStoreColorId());
                if (CollectionUtils.isNotEmpty(colorSizeList)) {
                    BigDecimal minPrice = colorSizeList.stream().map(size -> ObjectUtils.defaultIfNull(size.getPrice(), BigDecimal.ZERO))
                            .min(Comparator.comparing(Function.identity())).get();
                    String standardSize = colorSizeList.stream().map(size -> size.getSize().toString()).collect(Collectors.joining(","));
                    x.setPrice(minPrice).setStandard(standardSize);
                }
            });
        }
        return CollectionUtils.isEmpty(prodList) ? Page.empty(pageDTO.getPageSize(), pageDTO.getPageNum()) : Page.convert(new PageInfo<>(prodList));
    }

    @Override
    @Transactional
    public int create(StoreProdDTO createDTO) throws IOException {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(createDTO.getStoreId())) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        } // 校验标题中是否包含违禁词
        List<String> prohibitedWords = AdValidator.findProhibitedWords(createDTO.getProdTitle().trim());
        if (CollectionUtils.isNotEmpty(prohibitedWords)) {
            throw new ServiceException("商品标题含有违禁词: " + String.join(",", prohibitedWords), HttpStatus.ERROR);
        }
        // 校验富文本标签是否合法
        boolean isValid = HtmlValidator.isValidHtml(createDTO.getDetail());
        if (!isValid) {
            // 清理 HTML
            createDTO.setDetail(HtmlValidator.sanitizeHtml(createDTO.getDetail()));
        }
        // 判断同档口商品货号是否重复
        List<StoreProduct> existProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getStoreId, createDTO.getStoreId()).eq(StoreProduct::getProdArtNum, createDTO.getProdArtNum().trim()));
        if (CollectionUtils.isNotEmpty(existProdList)) {
            throw new ServiceException("商品货号重复，请修改后重新提交!", HttpStatus.ERROR);
        }
        // 组装StoreProduct数据
        StoreProduct storeProd = BeanUtil.toBean(createDTO, StoreProduct.class).setVoucherDate(DateUtils.getNowDate())
                .setRecommendWeight(0L).setSaleWeight(0L).setPopularityWeight(0L);
        int count = this.storeProdMapper.insert(storeProd);
        // 新增档口商品颜色相关
        this.createProdColor(createDTO, storeProd.getId(), storeProd.getStoreId(), storeProd.getProdArtNum());
        // 新增档口商品其它属性
        this.createOtherProperties(createDTO, storeProd);
        // 非私款的商品 且 立即发布 将商品同步到 ES 商品文档，并将商品主图同步到 以图搜款服务中
        if (Objects.equals(storeProd.getPrivateItem(), EProductItemType.NON_PRIVATE_ITEM.getValue())
                && Objects.equals(storeProd.getListingWay(), ListingType.RIGHT_NOW.getValue())) {
            // redis中的档口
            Store store = this.redisCache.getCacheObject(CacheConstants.STORE_KEY + storeProd.getStoreId());
            // 向ES索引: product_info 创建文档
            this.createESDoc(storeProd, createDTO, store.getStoreName());
            // 搜图服务同步
            sync2ImgSearchServer(storeProd.getId(), createDTO.getFileList());
            // 新增档口商品动态、关注档口通知公告
            this.createNotice(storeProd, store.getStoreName());
        }
        return count;
    }

    /**
     * 修改档口商品
     *
     * @param updateDTO 档口商品
     * @return 结果
     */
    @Override
    @Transactional
    public int update(final Long storeProdId, StoreProdDTO updateDTO) throws IOException {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(updateDTO.getStoreId())) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        // 校验标题中是否包含违禁词
        List<String> prohibitedWords = AdValidator.findProhibitedWords(updateDTO.getProdTitle().trim());
        if (CollectionUtils.isNotEmpty(prohibitedWords)) {
            throw new ServiceException("商品标题含有违禁词: " + String.join(",", prohibitedWords), HttpStatus.ERROR);
        }
        // 校验富文本标签是否合法
        boolean isValid = HtmlValidator.isValidHtml(updateDTO.getDetail());
        if (!isValid) {
            // 清理 HTML
            updateDTO.setDetail(HtmlValidator.sanitizeHtml(updateDTO.getDetail()));
        }
        List<StoreProduct> existProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getStoreId, updateDTO.getStoreId()).ne(StoreProduct::getId, storeProdId)
                .eq(StoreProduct::getProdArtNum, updateDTO.getProdArtNum().trim()));
        if (CollectionUtils.isNotEmpty(existProdList)) {
            throw new ServiceException("商品货号重复，请修改后重新提交!", HttpStatus.ERROR);
        }
        StoreProduct storeProd = Optional.ofNullable(this.storeProdMapper.selectOne(new LambdaQueryWrapper<StoreProduct>()
                        .eq(StoreProduct::getId, storeProdId).eq(StoreProduct::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口商品不存在!", HttpStatus.ERROR));
        // 更新档口商品信息
        BeanUtil.copyProperties(updateDTO, storeProd);
        int count = this.storeProdMapper.updateById(storeProd);
        // 档口文件（商品主图、主图视频、下载的商品详情）的del_flag置为2
        this.storeProdFileMapper.updateDelFlagByStoreProdId(storeProdId);
        // 档口类目属性列表的 del_flag置为2
        this.storeProdCateAttrMapper.updateDelFlagByStoreProdId(storeProdId);
        // 档口详情内容的del_flag置为2
        this.storeProdDetailMapper.updateDelFlagByStoreProdId(storeProdId);
        // 档口服务承诺的del_flag置为2
        this.storeProdSvcMapper.updateDelFlagByStoreProdId(storeProdId);
        // 档口工艺信息的del_flag置为2
        this.storeProdProcMapper.updateDelFlagByStoreProdId(storeProdId);
        // 更新档口商品颜色价格及尺码等
        this.updateColorRelation(updateDTO, storeProd.getId(), storeProd.getStoreId(), storeProd.getProdArtNum());
        // 处理更新逻辑
        this.updateOtherProperties(updateDTO, storeProd);
        // 只有非私款商品 且 只有在售和尾货状态，更新ES 信息 及 图搜
        if (Objects.equals(storeProd.getPrivateItem(), EProductItemType.NON_PRIVATE_ITEM.getValue())
                && (Objects.equals(storeProd.getProdStatus(), EProductStatus.ON_SALE.getValue())
                || Objects.equals(storeProd.getProdStatus(), EProductStatus.TAIL_GOODS.getValue()))) {
            // 从redis中获取store
            Store store = this.redisCache.getCacheObject(CacheConstants.STORE_KEY + storeProd.getStoreId());
            // 更新索引: product_info 的文档
            this.updateESDoc(storeProd, updateDTO, store.getStoreName());
            // 搜图服务同步
            sync2ImgSearchServer(storeProd.getId(), updateDTO.getFileList());
            // 更新档口商品动态、关注档口、收藏商品通知公告
            this.updateNotice(storeProd, store.getStoreName());
        }
        return count;
    }


    /**
     * 更新商品其它属性
     *
     * @param updateDTO 更新入参
     * @param storeProd 档口商品
     */
    private void updateOtherProperties(StoreProdDTO updateDTO, StoreProduct storeProd) {
        // 上传的文件列表
        final List<StoreProdFileDTO> fileDTOList = updateDTO.getFileList();
        // 将文件插入到SysFile表中
        List<SysFile> fileList = BeanUtil.copyToList(fileDTOList, SysFile.class);
        this.fileMapper.insert(fileList);
        // 将文件名称和文件ID映射到Map中
        Map<String, Long> fileMap = fileList.stream().collect(Collectors.toMap(SysFile::getFileName, SysFile::getId));
        // 档口文件（商品主图、主图视频、下载的商品详情）
        List<StoreProductFile> prodFileList = fileDTOList.stream().map(x -> BeanUtil.toBean(x, StoreProductFile.class)
                        .setFileId(fileMap.get(x.getFileName())).setStoreProdId(storeProd.getId()).setStoreId(updateDTO.getStoreId())
                        .setPicZipStatus(Objects.equals(x.getFileType(), FileType.DOWNLOAD.getValue())
                                ? StoreProdFileZipStatus.PENDING.getValue() : StoreProdFileZipStatus.NON_ZIP.getValue()))
                .collect(Collectors.toList());
        this.storeProdFileMapper.insert(prodFileList);
        // 档口类目属性
        this.storeProdCateAttrMapper.insert(BeanUtil.toBean(updateDTO.getCateAttr(), StoreProductCategoryAttribute.class)
                .setStoreProdId(storeProd.getId()).setStoreId(storeProd.getStoreId()));
        // 档口详情内容
        StoreProductDetail storeProdDetail = new StoreProductDetail().setDetail(updateDTO.getDetail()).setStoreProdId(storeProd.getId());
        this.storeProdDetailMapper.insert(storeProdDetail);
        // 档口服务承诺
        if (ObjectUtils.isNotEmpty(updateDTO.getSvc())) {
            this.storeProdSvcMapper.insert(BeanUtil.toBean(updateDTO.getSvc(), StoreProductService.class).setStoreProdId(storeProd.getId()));
        }
        // 档口生产工艺信息
        if (ObjectUtils.isNotEmpty(updateDTO.getProcess())) {
            this.storeProdProcMapper.insert(BeanUtil.toBean(updateDTO.getProcess(), StoreProductProcess.class).setStoreProdId(storeProd.getId()));
        }
    }

    /**
     * 更新档口商品其它属性
     *
     * @param updateDTO   更新入参
     * @param storeProdId 档口商品ID
     * @param storeId     档口ID
     * @param prodArtNum  货号
     */
    private void updateColorRelation(StoreProdDTO updateDTO, Long storeProdId, Long storeId, String prodArtNum) {
        // 处理档口所有颜色
        Map<String, Long> storeColorMap = updateDTO.getAllColorList().stream().filter(x -> ObjectUtils.isNotEmpty(x.getStoreColorId()))
                .collect(Collectors.toMap(StoreColorDTO::getColorName, StoreColorDTO::getStoreColorId));
        List<StoreColor> newColorList = updateDTO.getAllColorList().stream().filter(x -> ObjectUtils.isEmpty(x.getStoreColorId()))
                .map(x -> BeanUtil.toBean(x, StoreColor.class).setStoreId(updateDTO.getStoreId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(newColorList)) {
            this.storeColorMapper.insert(newColorList);
            storeColorMap.putAll(newColorList.stream().collect(Collectors.toMap(StoreColor::getColorName, StoreColor::getId)));
        }
        // 所有颜色列表
        List<StoreProductColor> dbProdColorList = this.storeProdColorMapper.selectList(new LambdaQueryWrapper<StoreProductColor>()
                .eq(StoreProductColor::getStoreProdId, storeProdId).eq(StoreProductColor::getDelFlag, Constants.UNDELETED));
        Map<String, StoreProductColor> existColorMap = dbProdColorList.stream().collect(Collectors.toMap(StoreProductColor::getColorName, x -> x));
        // 当前商品已存在的storeColorId列表
        List<Long> exitProdColorIdList = dbProdColorList.stream().map(StoreProductColor::getStoreColorId).collect(Collectors.toList());
        // 当前商品待更新的storeColorId列表
        List<Long> updateProdColorIdList = updateDTO.getSizeList().stream().map(StoreProdDTO.SPCSizeDTO::getColorName).map(storeColorMap::get).collect(Collectors.toList());
        // 待更新的商品颜色内里map
        Map<String, String> updateColorLineMaterialMap = updateDTO.getSizeList().stream().collect(Collectors.toMap(StoreProdDTO.SPCSizeDTO::getColorName, StoreProdDTO.SPCSizeDTO::getShoeUpperLiningMaterial, (v1, v2) -> v2));
        dbProdColorList.stream()
                // 判断有哪些是删除的颜色
                .filter(color -> !updateProdColorIdList.contains(color.getStoreColorId()))
                .forEach(color -> color.setDelFlag(Constants.DELETED));
        // 新增的颜色。分为两种情况:1. 完全新增的颜色 2. 已存在，新加到商品中
        final List<String> updateProdColorNameList = updateDTO.getSizeList().stream().map(StoreProdDTO.SPCSizeDTO::getColorName).distinct().collect(Collectors.toList());
        for (int i = 0; i < updateProdColorNameList.size(); i++) {
            final Long updateProdColorId = storeColorMap.get(updateProdColorNameList.get(i));
            // 已存在的颜色则不新增
            if (exitProdColorIdList.contains(updateProdColorId)) {
                StoreProductColor existColor = existColorMap.get(updateProdColorNameList.get(i));
                if (ObjectUtils.isNotEmpty(existColor)) {
                    // 修改颜色的内里属性
                    existColor.setShoeUpperLiningMaterial(updateColorLineMaterialMap.get(updateProdColorNameList.get(i)));
                }
                continue;
            }
            // 新增的商品颜色
            dbProdColorList.add(new StoreProductColor().setStoreColorId(updateProdColorId).setStoreProdId(storeProdId).setStoreId(storeId)
                    .setShoeUpperLiningMaterial(updateColorLineMaterialMap.get(updateProdColorNameList.get(i)))
                    .setColorName(updateProdColorNameList.get(i)).setOrderNum(i + 1).setProdStatus(EProductStatus.ON_SALE.getValue()));
        }
        // 更新商品颜色或新增商品颜色
        this.storeProdColorMapper.insertOrUpdate(dbProdColorList);

        // 待更新的商品颜色尺码map 按照颜色尺码 升序排列
        Map<String, StoreProdDTO.SPCSizeDTO> updateColorSizeMap = updateDTO.getSizeList().stream()
                .sorted(Comparator.comparing(StoreProdDTO.SPCSizeDTO::getStoreColorId, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(StoreProdDTO.SPCSizeDTO::getSize))
                .collect(Collectors.toMap(x -> ObjectUtils.defaultIfNull(x.getStoreColorId(), "") + ":" + x.getSize(),
                        Function.identity(), (existing, replacement) -> existing, LinkedHashMap::new));
        // 处理商品颜色和尺码对应的价格
        List<StoreProductColorSize> dbProdColorSizeList = this.storeProdColorSizeMapper.selectList(new LambdaQueryWrapper<StoreProductColorSize>()
                .eq(StoreProductColorSize::getStoreProdId, storeProdId).eq(StoreProductColorSize::getDelFlag, Constants.UNDELETED));
        Map<String, StoreProductColorSize> dbProdColorSizeMap = dbProdColorSizeList.stream().collect(Collectors.toMap(x -> x.getStoreColorId() + ":" + x.getSize(), x -> x));
        dbProdColorSizeList.forEach(dbColorSize -> {
            final String dbKey = dbColorSize.getStoreColorId() + ":" + dbColorSize.getSize();
            // 判断哪些颜色是删除的颜色
            if (!updateColorSizeMap.containsKey(dbKey)) {
                dbColorSize.setDelFlag(Constants.DELETED);
            }
        });
        // 判断哪些颜色是新增 或 更新的颜色
        updateColorSizeMap.forEach((updateKey, updateColorSize) -> {
            StoreProductColorSize dbColorSize = dbProdColorSizeMap.get(updateKey);
            // 已存在则更新
            if (ObjectUtils.isNotEmpty(dbColorSize)) {
                dbProdColorSizeList.add(dbColorSize.setPrice(updateColorSize.getPrice()).setStandard(updateColorSize.getStandard()));
            } else {
                // 不存在则新增
                dbProdColorSizeList.add(new StoreProductColorSize().setSize(updateColorSize.getSize()).setStoreProdId(storeProdId)
                        .setStandard(updateColorSize.getStandard()).setStoreColorId(storeColorMap.get(updateColorSize.getColorName()))
                        .setPrice(updateColorSize.getPrice()).setNextSn(0));
            }
        });
        this.storeProdColorSizeMapper.insertOrUpdate(dbProdColorSizeList);
        // 设置档口商品价格尺码的barcode_prefix
        List<StoreProductColorSize> nullSnPrefixList = dbProdColorSizeList.stream().filter(x -> StringUtils.isEmpty(x.getSnPrefix()))
                .peek(x -> x.setSnPrefix(storeId + String.format("%08d", x.getId()))).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(nullSnPrefixList)) {
            this.storeProdColorSizeMapper.updateById(nullSnPrefixList);
        }

        // 正在生效的颜色
        List<StoreProductColor> validColorList = dbProdColorList.stream().filter(x -> Objects.equals(x.getDelFlag(), UNDELETED)).collect(Collectors.toList());
        final List<Long> validColorIdList = validColorList.stream().map(StoreProductColor::getId).collect(Collectors.toList());
        // 如果数据库不存在该商品颜色的库存，则初始化
        List<StoreProductStock> dbProdStockList = Optional.ofNullable(this.prodStockMapper.selectList(new LambdaQueryWrapper<StoreProductStock>()
                .eq(StoreProductStock::getStoreProdId, storeProdId).eq(StoreProductStock::getDelFlag, Constants.UNDELETED))).orElse(new ArrayList<>());
        final List<Long> dbExistStockColorIdList = dbProdStockList.stream().map(StoreProductStock::getStoreProdColorId).collect(Collectors.toList());
        // 有哪些颜色是删除的
        dbProdStockList.stream().filter(x -> !validColorIdList.contains(x.getStoreProdColorId())).forEach(x -> x.setDelFlag(Constants.DELETED));
        // 有哪些是新增的颜色
        validColorList.stream().filter(x -> !dbExistStockColorIdList.contains(x.getId()))
                .forEach(x -> dbProdStockList.add(new StoreProductStock().setStoreId(storeId).setStoreProdId(storeProdId).setStoreProdColorId(x.getId())
                        .setProdArtNum(prodArtNum).setStoreColorId(x.getStoreColorId()).setColorName(x.getColorName())));
        this.prodStockMapper.insertOrUpdate(dbProdStockList);
    }

    /**
     * 新增档口商品其它属性
     *
     * @param createDTO 新增入参
     * @param storeProd 档口商品
     */
    private void createOtherProperties(StoreProdDTO createDTO, StoreProduct storeProd) {
        // 上传的文件列表
        final List<StoreProdFileDTO> fileDTOList = createDTO.getFileList();
        // 将文件插入到SysFile表中
        List<SysFile> fileList = BeanUtil.copyToList(fileDTOList, SysFile.class);
        this.fileMapper.insert(fileList);
        // 将文件名称和文件ID映射到Map中
        Map<String, Long> fileMap = fileList.stream().collect(Collectors.toMap(SysFile::getFileName, SysFile::getId));
        // 档口文件（商品主图、主图视频、下载的商品详情）
        List<StoreProductFile> prodFileList = fileDTOList.stream().map(x -> BeanUtil.toBean(x, StoreProductFile.class)
                        .setFileId(fileMap.get(x.getFileName())).setStoreProdId(storeProd.getId()).setStoreId(createDTO.getStoreId())
                        .setPicZipStatus(Objects.equals(x.getFileType(), FileType.DOWNLOAD.getValue())
                                ? StoreProdFileZipStatus.PENDING.getValue() : StoreProdFileZipStatus.NON_ZIP.getValue()))
                .collect(Collectors.toList());
        this.storeProdFileMapper.insert(prodFileList);
        // 档口类目属性
        this.storeProdCateAttrMapper.insert(BeanUtil.toBean(createDTO.getCateAttr(), StoreProductCategoryAttribute.class)
                .setStoreProdId(storeProd.getId()).setStoreId(storeProd.getStoreId()));
        // 档口详情内容
        StoreProductDetail storeProdDetail = new StoreProductDetail().setDetail(createDTO.getDetail()).setStoreProdId(storeProd.getId());
        this.storeProdDetailMapper.insert(storeProdDetail);
        // 档口服务承诺
        if (ObjectUtils.isNotEmpty(createDTO.getSvc())) {
            this.storeProdSvcMapper.insert(BeanUtil.toBean(createDTO.getSvc(), StoreProductService.class).setStoreProdId(storeProd.getId()));
        }
        // 档口生产工艺信息
        if (ObjectUtils.isNotEmpty(createDTO.getProcess())) {
            this.storeProdProcMapper.insert(BeanUtil.toBean(createDTO.getProcess(), StoreProductProcess.class).setStoreProdId(storeProd.getId()));
        }
    }

    /**
     * 新增档口商品颜色等
     *
     * @param createDTO   入参
     * @param storeProdId 档口商品ID
     * @param storeId     档口ID
     * @param prodArtNum  货号
     */
    private void createProdColor(StoreProdDTO createDTO, Long storeProdId, Long storeId, String prodArtNum) {
        // 处理档口所有颜色
        Map<String, Long> storeColorMap = createDTO.getAllColorList().stream().filter(x -> ObjectUtils.isNotEmpty(x.getStoreColorId()))
                .collect(Collectors.toMap(StoreColorDTO::getColorName, StoreColorDTO::getStoreColorId));
        // 新增的颜色
        List<StoreColor> newColorList = createDTO.getAllColorList().stream().filter(x -> ObjectUtils.isEmpty(x.getStoreColorId()))
                .map(x -> BeanUtil.toBean(x, StoreColor.class).setStoreId(createDTO.getStoreId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(newColorList)) {
            this.storeColorMapper.insert(newColorList);
            storeColorMap.putAll(newColorList.stream().collect(Collectors.toMap(StoreColor::getColorName, StoreColor::getId)));
        }
        // 新增档口颜色尺码与价格
        List<StoreProductColor> prodColorList = new ArrayList<>();
        final List<String> prodColorNameList = createDTO.getSizeList().stream().map(StoreProdDTO.SPCSizeDTO::getColorName).distinct().collect(Collectors.toList());
        // 颜色与内里的关系
        final Map<String, String> colorLiningMaterialMap = createDTO.getSizeList().stream().collect(Collectors
                .toMap(StoreProdDTO.SPCSizeDTO::getColorName, StoreProdDTO.SPCSizeDTO::getShoeUpperLiningMaterial, (v1, v2) -> v2));
        for (int i = 0; i < prodColorNameList.size(); i++) {
            prodColorList.add(new StoreProductColor().setStoreColorId(storeColorMap.get(prodColorNameList.get(i))).setStoreProdId(storeProdId)
                    .setShoeUpperLiningMaterial(colorLiningMaterialMap.get(prodColorNameList.get(i))).setColorName(prodColorNameList.get(i))
                    .setStoreId(storeId).setOrderNum(i + 1).setProdStatus(EProductStatus.ON_SALE.getValue()));
        }
        this.storeProdColorMapper.insert(prodColorList);
        // 新增档口颜色尺码对应价格
        List<StoreProductColorSize> prodColorSizeList = createDTO.getSizeList().stream().map(x -> new StoreProductColorSize().setSize(x.getSize()).setStoreProdId(storeProdId)
                        .setStandard(x.getStandard()).setStoreColorId(storeColorMap.get(x.getColorName())).setPrice(x.getPrice()).setNextSn(0))
                .collect(Collectors.toList());
        this.storeProdColorSizeMapper.insert(prodColorSizeList);
        // 设置档口商品价格尺码的barcode_prefix
        prodColorSizeList.forEach(x -> x.setSnPrefix(storeId + String.format("%08d", x.getId())));
        this.storeProdColorSizeMapper.updateById(prodColorSizeList);
        // 新增档口库存初始化数据
        List<StoreProductStock> prodStockList = prodColorList.stream().map(x -> new StoreProductStock().setStoreId(storeId)
                        .setStoreProdId(storeProdId).setStoreProdColorId(x.getId()).setProdArtNum(prodArtNum)
                        .setStoreColorId(x.getStoreColorId()).setColorName(x.getColorName()))
                .collect(Collectors.toList());
        this.prodStockMapper.insert(prodStockList);
        // 设置了档口商品全部优惠的客户，新增商品优惠
        this.createStoreCusDiscount(prodColorList, storeProdId, prodArtNum);
    }

    /**
     * 更新档口商品颜色的状态 2在售 3尾货 4下架
     *
     * @param prodStatusDTO 入参
     */
    @Override
    @Transactional
    public Integer updateStoreProductStatus(StoreProdStatusDTO prodStatusDTO) throws IOException {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(prodStatusDTO.getStoreId())) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        // 判断商品状态是否不存在
        EProductStatus.of(prodStatusDTO.getProdStatus());
        // 查询所有的商品颜色
        List<StoreProductColor> storeProdColorList = this.storeProdColorMapper.selectList(new LambdaQueryWrapper<StoreProductColor>()
                .in(StoreProductColor::getId, prodStatusDTO.getStoreProdColorIdList()).eq(StoreProductColor::getDelFlag, Constants.UNDELETED)
                .eq(StoreProductColor::getStoreId, prodStatusDTO.getStoreId()));
        if (CollectionUtils.isEmpty(storeProdColorList)) {
            throw new ServiceException("商品颜色不存在!", HttpStatus.ERROR);
        }
        storeProdColorList.forEach(x -> {
            x.setProdStatus(prodStatusDTO.getProdStatus());
            // 删除颜色
            if (Objects.equals(prodStatusDTO.getProdStatus(), EProductStatus.REMOVED.getValue())) {
                x.setDelFlag(DELETED);
            }
        });
        int count = this.storeProdColorMapper.updateById(storeProdColorList).size();
        final List<Long> storeProdIdList = storeProdColorList.stream().map(StoreProductColor::getStoreProdId).collect(Collectors.toList());
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .in(StoreProduct::getId, storeProdIdList).eq(StoreProduct::getDelFlag, Constants.UNDELETED)
                .eq(StoreProduct::getStoreId, prodStatusDTO.getStoreId()));
        Map<Long, StoreProduct> storeProdMap = storeProdList.stream().collect(Collectors.toMap(StoreProduct::getId, Function.identity()));
        // 旧的商品状态map
        final Map<Long, Integer> storeProdStatusMap = storeProdList.stream().collect(Collectors.toMap(StoreProduct::getId, StoreProduct::getProdStatus));
        // 筛选所有的商品，判断商品的状态 此处不能筛选del_flag=0，因为页面删除时会将del_flag置为2
        List<StoreProductColor> curProdColorList = this.storeProdColorMapper.selectList(new LambdaQueryWrapper<StoreProductColor>()
                .in(StoreProductColor::getStoreProdId, storeProdIdList).eq(StoreProductColor::getStoreId, prodStatusDTO.getStoreId()));
        Map<Long, List<StoreProductColor>> prodColorMap = curProdColorList.stream().collect(Collectors.groupingBy(StoreProductColor::getStoreProdId));
        List<StoreProduct> updateProdList = new ArrayList<>();
        // 已删除的商品列表，需要同时删除 商品库存及商品优惠
        List<Long> deleteProdIdList = new ArrayList<>();
        prodColorMap.forEach((storeProdId, prodColorList) -> {
            StoreProduct storeProduct = Optional.ofNullable(storeProdMap.get(storeProdId)).orElseThrow(() -> new ServiceException("商品不存在!", HttpStatus.ERROR));
            // 商品颜色中只要有一个为在售，则商品状态为在售
            if (prodColorList.stream().anyMatch(x -> Objects.equals(x.getProdStatus(), EProductStatus.ON_SALE.getValue()))) {
                storeProduct.setProdStatus(EProductStatus.ON_SALE.getValue());
                // 商品颜色中只要有一个为尾货，则商品状态为尾货
            } else if (prodColorList.stream().anyMatch(x -> Objects.equals(x.getProdStatus(), EProductStatus.TAIL_GOODS.getValue()))) {
                storeProduct.setProdStatus(EProductStatus.TAIL_GOODS.getValue());
                // 商品颜色只有一个为下架，则商品状态为下架
            } else if (prodColorList.stream().anyMatch(x -> Objects.equals(x.getProdStatus(), EProductStatus.OFF_SALE.getValue()))) {
                storeProduct.setProdStatus(EProductStatus.OFF_SALE.getValue());
                // 商品颜色必须全部为已删除，则商品状态为已删除
            } else {
                deleteProdIdList.add(storeProdId);
                storeProduct.setDelFlag(DELETED);
                storeProduct.setProdStatus(EProductStatus.REMOVED.getValue());
            }
            updateProdList.add(storeProduct);
        });
        this.storeProdMapper.updateById(updateProdList);
        // 已删除的商品列表，需要同时删除 商品库存及商品优惠
        if (CollectionUtils.isNotEmpty(deleteProdIdList)) {
            this.prodStockMapper.deleteProdStock(deleteProdIdList);
            this.storeCusProdDiscMapper.deleteProdDisc(deleteProdIdList);
        }
        // 非私款的商品，才需要更新图搜及ES
        final List<StoreProduct> nonPrivateProdList = updateProdList.stream()
                .filter(x -> Objects.equals(x.getPrivateItem(), EProductItemType.NON_PRIVATE_ITEM.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(nonPrivateProdList)) {
            return count;
        }
        // 非删除商品颜色，则更新ES
        if (!Objects.equals(prodStatusDTO.getProdStatus(), EProductStatus.REMOVED.getValue())) {
            // 需要更新状态的商品
            Map<Long, Integer> updateESProdStatusMap = nonPrivateProdList.stream().filter(x -> !Objects.equals(storeProdStatusMap.get(x.getId()), x.getProdStatus()))
                    .collect(Collectors.toMap(StoreProduct::getId, StoreProduct::getProdStatus));
            // 没有需要更新商品状态的商品
            if (MapUtils.isEmpty(updateESProdStatusMap)) {
                return count;
            }
            // 更新ES中商品状态
            this.updateESProdStatus(updateESProdStatusMap);
        } else {
            // 需要删除的商品
            List<StoreProduct> deleteESProdList = nonPrivateProdList.stream().filter(x -> !Objects.equals(storeProdStatusMap.get(x.getId()), x.getProdStatus())).collect(Collectors.toList());
            List<Long> updateESProdIdList = deleteESProdList.stream().map(StoreProduct::getId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(updateESProdIdList)) {
                return count;
            }
            // 删除ES中商品
            this.deleteESDoc(updateESProdIdList);
            // 搜图服务同步
            updateESProdIdList.forEach(spId -> sync2ImgSearchServer(spId, ListUtil.empty()));
            // 新增消息通知
            this.offSaleOrReSaleProd(deleteESProdList, Boolean.TRUE, prodStatusDTO.getStoreId());
        }
        return count;
    }

    /**
     * 推广营销查询最近30天上新商品
     *
     * @param storeId    档口ID
     * @param prodArtNum 商品货号
     * @return List<StoreProdFuzzyLatest20ResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreProdFuzzyLatest30ResDTO> fuzzyQueryLatest30List(Long storeId, String prodArtNum) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(storeId)) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        LocalDateTime daysAgo = LocalDateTime.now().minusDays(30).withHour(0).withMinute(0).withSecond(0);
        LambdaQueryWrapper<StoreProduct> queryWrapper = new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getStoreId, storeId).eq(StoreProduct::getDelFlag, Constants.UNDELETED)
                .between(StoreProduct::getCreateTime, daysAgo, LocalDateTime.now());
        if (StringUtils.isNotBlank(prodArtNum)) {
            queryWrapper.like(StoreProduct::getProdArtNum, prodArtNum);
        }
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(queryWrapper);
        return storeProdList.stream().map(x -> new StoreProdFuzzyLatest30ResDTO().setStoreId(x.getStoreId())
                .setStoreProdId(x.getId()).setProdArtNum(x.getProdArtNum())).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public PicPackInfoDTO getPicPackDownloadUrl(PicPackReqDTO picPackReqDTO) {
        Assert.notNull(picPackReqDTO.getUserId());
        String reqCacheKey = CacheConstants.PIC_PACK_USER_REQ_COUNT_CACHE + picPackReqDTO.getUserId();
        String dailyReqCacheKey = CacheConstants.PIC_PACK_USER_DAILY_REQ_COUNT_CACHE + picPackReqDTO.getUserId();
        int reqCount = Optional.ofNullable((Integer) redisCache.getCacheObject(reqCacheKey)).orElse(0);
        int dailyReqCount = Optional.ofNullable((Integer) redisCache.getCacheObject(dailyReqCacheKey)).orElse(0);
        //每日下载次数限制：50次
        if (dailyReqCount > 49) {
            throw new ServiceException("您今日的免费下载限额(50款/日)已用完，请明日再来!如需扩大下载限额，请联系平台客服");
        }
        //5次请求后需要输入验证码
        if (reqCount > 4) {
            //需验证验证码
            if (StrUtil.isEmpty(picPackReqDTO.getTicket()) || StrUtil.isEmpty(picPackReqDTO.getRandstr())) {
                //未传验证码
                return new PicPackInfoDTO(true);
            }
            //验证码校验
            boolean pass = tencentAuthManager.validate(picPackReqDTO.getTicket(), picPackReqDTO.getRandstr());
            if (!pass) {
                throw new ServiceException("验证未通过");
            }
            //重置请求次数
            reqCount = 0;
        }
        SysFile file = fileMapper.selectById(picPackReqDTO.getFileId());
        Assert.notNull(file);
        String downloadUrl;
        try {
            //获取完整下载链接
            downloadUrl = ossClient.generateUrl(ossClient.getConfiguration().getBucketName(), file.getFileUrl(),
                    600000L, true).toString();
        } catch (Exception e) {
            log.error("获取图包异常", e);
            throw new ServiceException("获取图包失败");
        }
        //请求次数+1
        redisCache.setCacheObject(reqCacheKey, reqCount + 1);
        redisCache.setCacheObject(dailyReqCacheKey, dailyReqCount + 1);
        if (0 == dailyReqCount) {
            //每日首次下载
            long expire = DateUtil.endOfDay(new Date()).getTime() - System.currentTimeMillis();
            redisCache.expire(dailyReqCacheKey, expire, TimeUnit.MILLISECONDS);
        }
        return new PicPackInfoDTO(file.getId(), file.getFileName(), file.getFileUrl(), file.getFileSize(), downloadUrl, false);
    }

    /**
     * 模糊查询系统所有商品
     *
     * @param storeId    档口ID
     * @param prodArtNum 货号
     * @return StoreProdFuzzyResDTO
     */
    @Override
    public List<StoreProdFuzzyResDTO> fuzzyQuery(Long storeId, String prodArtNum) {
        LambdaQueryWrapper<StoreProduct> queryWrapper = new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getDelFlag, Constants.UNDELETED).eq(StoreProduct::getStoreId, storeId);
        if (StringUtils.isNotBlank(prodArtNum)) {
            queryWrapper.like(StoreProduct::getProdArtNum, prodArtNum);
        }
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(queryWrapper);
        return storeProdList.stream().map(x -> new StoreProdFuzzyResDTO()
                        .setStoreProdId(x.getId()).setStoreId(x.getStoreId()).setProdArtNum(x.getProdArtNum()))
                .collect(Collectors.toList());
    }

    /**
     * 获取商城 档口首页商品状态数量
     *
     * @param storeId 档口ID
     * @return StoreProdStatusCountResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreProdStatusCountResDTO getPcStatusNum(Long storeId) {
        return this.storeProdMapper.getStatusNum(storeId);
    }

    /**
     * 获取档口商品PC端下载信息
     *
     * @param storeProdId 档口商品ID
     * @return StoreProdPcDownloadInfoResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreProdPcDownloadInfoResDTO getPcDownloadInfo(Long storeProdId) {
        // 商品下载量+1
        redisCache.valueIncr(CacheConstants.PRODUCT_STATISTICS_DOWNLOAD_COUNT, storeProdId);
        StoreProdPcDownloadInfoResDTO downloadInfo = this.storeProdMapper.selectDownloadProdInfo(storeProdId);
        // 获取下载图包
        return downloadInfo.setFileList(this.storeProdFileMapper.selectDownloadFileList(storeProdId));
    }


    /**
     * 根据档口ID和商品货号模糊查询货号列表
     *
     * @param storeId    档口ID
     * @param prodArtNum 商品货号
     * @return List<String>
     */
    /**
     * 根据商店ID和产品货号模糊查询产品列表
     *
     * @param storeId    商店ID，用于查询特定商店的产品
     * @param prodArtNum 产品货号，用于模糊匹配产品
     * @return 返回一个包含模糊查询结果的列表，每个元素包含产品信息和颜色信息
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreProdFuzzyColorResDTO> fuzzyQueryColorList(Long storeId, String prodArtNum) {
        // 初始化查询条件，确保查询的是指定商店且未删除的产品
        LambdaQueryWrapper<StoreProduct> queryWrapper = new LambdaQueryWrapper<StoreProduct>()
                .in(StoreProduct::getProdStatus, Arrays.asList(EProductStatus.ON_SALE.getValue(), EProductStatus.TAIL_GOODS.getValue()))
                .eq(StoreProduct::getStoreId, storeId).eq(StoreProduct::getDelFlag, Constants.UNDELETED);
        // 如果产品货号非空，添加模糊查询条件
        if (StringUtils.isNotBlank(prodArtNum)) {
            queryWrapper.like(StoreProduct::getProdArtNum, prodArtNum);
        }
        // 执行查询，获取产品列表
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(queryWrapper);
        // 如果查询结果为空，直接返回空列表
        if (CollectionUtils.isEmpty(storeProdList)) {
            return new ArrayList<>();
        }
        // 提取查询结果中的产品ID列表
        List<Long> storeProdIdList = storeProdList.stream().map(StoreProduct::getId).distinct().collect(Collectors.toList());
        // 查询与产品ID列表关联的颜色信息
        List<StoreProductColor> colorList = this.storeProdColorMapper.selectList(new LambdaQueryWrapper<StoreProductColor>()
                .in(StoreProductColor::getStoreProdId, storeProdIdList).eq(StoreProductColor::getDelFlag, Constants.UNDELETED));
        // 将颜色信息按产品ID分组，并转换为所需的颜色DTO列表
        Map<Long, List<StoreProdFuzzyColorResDTO.SPFCColorDTO>> colorMap = CollectionUtils.isEmpty(colorList) ? new HashMap<>()
                : colorList.stream().collect(Collectors.groupingBy(StoreProductColor::getStoreProdId, Collectors
                .collectingAndThen(Collectors.toList(), list -> list.stream()
                        .map(y -> BeanUtil.toBean(y, StoreProdFuzzyColorResDTO.SPFCColorDTO.class))
                        .collect(Collectors.toList()))));
        // 将产品列表转换为所需的产品DTO列表，并关联颜色信息
        return storeProdList.stream().map(x -> BeanUtil.toBean(x, StoreProdFuzzyColorResDTO.class).setStoreProdId(x.getId())
                .setColorList(colorMap.getOrDefault(x.getId(), new ArrayList<>()))).collect(Collectors.toList());
    }

    /**
     * 根据档口ID和商品货号模糊查询货号列表，返回数据带上商品主图
     *
     * @param storeId    档口ID
     * @param prodArtNum 商品货号
     * @return List<StoreProdFuzzyResPicDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreProdFuzzyResPicDTO> fuzzyQueryResPicList(Long storeId, String prodArtNum) {
        return this.storeProdMapper.fuzzyQueryResPicList(storeId, prodArtNum);
    }

    /**
     * 获取商品所有的风格
     *
     * @return List<String>
     */
    @Override
    @Transactional(readOnly = true)
    public List<String> getStyleList() {
        return this.storeProdMapper.getStyleList();
    }

    /**
     * APP获取档口商品详情
     *
     * @param storeProdId 档口商品ID
     * @return StoreProdAppResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreProdAppResDTO getAppInfo(Long storeProdId) {
        // 档口商品的基础信息
        StoreProdAppResDTO appResDTO = this.storeProdMapper.getAppInfo(storeProdId, SecurityUtils.getUserId());
        StoreProductCategoryAttribute cateAttr = this.storeProdCateAttrMapper.selectOne(new LambdaQueryWrapper<StoreProductCategoryAttribute>()
                .eq(StoreProductCategoryAttribute::getStoreProdId, storeProdId).eq(StoreProductCategoryAttribute::getDelFlag, Constants.UNDELETED));
        List<StoreProductColor> colorList = this.storeProdColorMapper.selectList(new LambdaQueryWrapper<StoreProductColor>()
                .eq(StoreProductColor::getStoreProdId, storeProdId).eq(StoreProductColor::getDelFlag, Constants.UNDELETED));
        List<StoreProductColorSize> colorSizeList = this.storeProdColorSizeMapper.selectList(new LambdaQueryWrapper<StoreProductColorSize>()
                .eq(StoreProductColorSize::getStoreProdId, storeProdId).eq(StoreProductColorSize::getDelFlag, Constants.UNDELETED)
                .eq(StoreProductColorSize::getStandard, ProductSizeStatus.STANDARD.getValue()));
        // 查询商品主图及主图视频
        List<StoreProdFileResDTO> fileList = this.storeProdFileMapper.selectVideoAndMainPicList(storeProdId, Arrays.asList(FileType.MAIN_PIC.getValue(), FileType.MAIN_PIC_VIDEO.getValue()));
        // 第一张商品主图
        final String mainPicUrl = fileList.stream().filter(x -> Objects.equals(x.getFileType(), FileType.MAIN_PIC.getValue()))
                .filter(x -> Objects.equals(x.getOrderNum(), ORDER_NUM_1)).map(StoreProdFileResDTO::getFileUrl).findAny().orElse("");
        // 将用户浏览足迹添加到redis中
        this.updateUserBrowsingToRedis(storeProdId, appResDTO.getStoreId(), appResDTO.getStoreName(), appResDTO.getProdArtNum(),
                appResDTO.getProdTitle(), appResDTO.getMinPrice(), mainPicUrl);
        // 商品浏览量+1
        redisCache.valueIncr(CacheConstants.PRODUCT_STATISTICS_VIEW_COUNT, storeProdId);
        return appResDTO.setTags(StringUtils.isNotBlank(appResDTO.getTagStr()) ? StrUtil.split(appResDTO.getTagStr(), ",") : null)
                // 拼接几色几码
                .setSpecification((CollectionUtils.isNotEmpty(colorList) ? colorList.size() + "色" : "") +
                        (CollectionUtils.isNotEmpty(colorSizeList) ? colorSizeList.size() + "码" : ""))
                // 获取商品的属性
                .setCateAttrMap(this.getCateAttrMap(cateAttr))
                // 获取商品的主图视频及主图
                .setFileList(fileList)
                // 获取商品的服务承诺
                .setSvc(this.storeProdSvcMapper.selectSvc(storeProdId));
    }


    /**
     * 获取档口商品颜色及sku等
     *
     * @param storeProdId 档口商品ID
     * @return StoreProdSkuResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreProdSkuResDTO getSkuList(Long storeProdId) {
        StoreProduct storeProd = Optional.ofNullable(this.storeProdMapper.selectOne(new LambdaQueryWrapper<StoreProduct>()
                        .eq(StoreProduct::getId, storeProdId).eq(StoreProduct::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口商品不存在!", HttpStatus.ERROR));
        StoreProdSkuResDTO skuDTO = BeanUtil.toBean(storeProd, StoreProdSkuResDTO.class);
        // 获取商品主图
        List<StoreProdMainPicDTO> prodFileList = this.storeProdFileMapper
                .selectMainPicByStoreProdIdList(Collections.singletonList(storeProdId), FileType.MAIN_PIC.getValue(), ORDER_NUM_1);
        if (CollectionUtils.isNotEmpty(prodFileList)) {
            skuDTO.setMainPicUrl(prodFileList.get(0).getFileUrl());
        }
        // 档口商品的sku列表
        List<StoreProdSkuDTO> prodSkuList = this.storeProdMapper.selectSkuList(storeProdId);
        if (CollectionUtils.isEmpty(prodSkuList)) {
            return skuDTO;
        }
        // 获取当前档口商品的sku列表
        Map<String, Integer> colorSizeStockMap = this.getProdStockList(storeProdId);
        // 组装所有的档口商品sku
        List<StoreProdSkuItemDTO> colorList = prodSkuList.stream().map(x -> BeanUtil.toBean(x, StoreProdSkuItemDTO.class)).distinct().collect(Collectors.toList());
        // storeColorId的sku列表
        Map<Long, List<StoreProdSkuDTO>> colorSizeMap = prodSkuList.stream().collect(Collectors.groupingBy(StoreProdSkuDTO::getStoreColorId));
        colorList.forEach(color -> {
            List<StoreProdSkuDTO> sizeList = Optional.ofNullable(colorSizeMap.get(color.getStoreColorId()))
                    .orElseThrow(() -> new ServiceException("获取商品sku失败，请联系客服!", HttpStatus.ERROR));
            color.setSizeStockList(sizeList.stream().map(size -> new StoreProdSkuItemDTO.SPSISizeStockDTO().setPrice(size.getPrice())
                            .setStoreProdColorSizeId(size.getStoreProdColorSizeId()).setSize(size.getSize()).setStandard(size.getStandard())
                            .setStock(colorSizeStockMap.get(color.getStoreColorId() + ":" + size.getSize())))
                    .collect(Collectors.toList()));
        });
        return skuDTO.setStoreProdId(storeProdId).setColorList(colorList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertOrUpdateProductStatistics(Long storeProdId, Integer incrViewCount, Integer incrDownloadCount,
                                                Integer incrImgSearchCount, Date date) {
        StoreProduct product = storeProdMapper.selectById(storeProdId);
        Assert.notNull(product);
        Assert.notNull(date);
        StoreProductStatistics statistics = storeProductStatisticsMapper.selectOne(Wrappers
                .lambdaQuery(StoreProductStatistics.class)
                .eq(StoreProductStatistics::getStoreProdId, storeProdId)
                .eq(StoreProductStatistics::getVoucherDate, DateUtil.formatDate(date)));
        Long vc = Optional.ofNullable(incrViewCount).map(Integer::longValue).orElse(0L);
        Long dc = Optional.ofNullable(incrDownloadCount).map(Integer::longValue).orElse(0L);
        Long isc = Optional.ofNullable(incrImgSearchCount).map(Integer::longValue).orElse(0L);
        if (statistics == null) {
            statistics = new StoreProductStatistics();
            statistics.setStoreId(product.getStoreId());
            statistics.setStoreProdId(storeProdId);
            statistics.setViewCount(vc);
            statistics.setDownloadCount(dc);
            statistics.setImgSearchCount(isc);
            statistics.setVoucherDate(date);
            statistics.setVersion(0);
            statistics.setDelFlag(UNDELETED);
            storeProductStatisticsMapper.insert(statistics);
        } else {
            statistics.setViewCount(statistics.getViewCount() + vc);
            statistics.setDownloadCount(statistics.getDownloadCount() + dc);
            statistics.setImgSearchCount(statistics.getImgSearchCount() + isc);
            storeProductStatisticsMapper.updateById(statistics);
        }
    }

    /**
     * PC获取档口商品详情
     *
     * @param storeProdId 档口商品ID
     * @return StoreProdPCResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreProdPCResDTO getPCInfo(Long storeProdId) {
        // 商品基础信息
        StoreProdPCResDTO prodInfoDTO = ObjectUtils.defaultIfNull(this.storeProdMapper.selectPCProdInfo(storeProdId, SecurityUtils.getUserIdSafe()), new StoreProdPCResDTO());
        // 获取商品的属性
        StoreProductCategoryAttribute cateAttr = this.storeProdCateAttrMapper.selectOne(new LambdaQueryWrapper<StoreProductCategoryAttribute>()
                .eq(StoreProductCategoryAttribute::getStoreProdId, storeProdId).eq(StoreProductCategoryAttribute::getDelFlag, Constants.UNDELETED));
        List<StoreProdFileResDTO> fileList = this.storeProdFileMapper
                .selectVideoAndMainPicList(storeProdId, Arrays.asList(FileType.MAIN_PIC_VIDEO.getValue(), FileType.MAIN_PIC.getValue()));
        prodInfoDTO
                // 获取商品的属性
                .setCateAttrMap(this.getCateAttrMap(cateAttr))
                // 获取商品的主图视频及主图
                .setFileList(fileList);
        // 档口商品的sku列表
        List<StoreProdSkuDTO> prodSkuList = this.storeProdMapper.selectSkuList(storeProdId);
        if (CollectionUtils.isEmpty(prodSkuList)) {
            return prodInfoDTO.setColorList(null);
        }
        // 获取当前档口商品的sku列表
        Map<String, Integer> colorSizeStockMap = this.getProdStockList(storeProdId);
        // 组装所有的档口商品sku
        List<StoreProdSkuItemDTO> colorList = prodSkuList.stream().map(x -> BeanUtil.toBean(x, StoreProdSkuItemDTO.class)).distinct().collect(Collectors.toList());
        // storeColorId的sku列表
        Map<Long, List<StoreProdSkuDTO>> colorSizeMap = prodSkuList.stream().collect(Collectors.groupingBy(StoreProdSkuDTO::getStoreColorId));
        colorList.forEach(color -> {
            List<StoreProdSkuDTO> sizeList = Optional.ofNullable(colorSizeMap.get(color.getStoreColorId()))
                    .orElseThrow(() -> new ServiceException("获取商品sku失败，请联系客服!", HttpStatus.ERROR));
            color.setSizeStockList(sizeList.stream().map(size -> new StoreProdSkuItemDTO.SPSISizeStockDTO()
                            .setStoreProdColorSizeId(size.getStoreProdColorSizeId()).setSize(size.getSize()).setStandard(size.getStandard())
                            .setStock(colorSizeStockMap.get(color.getStoreColorId() + ":" + size.getSize())))
                    .collect(Collectors.toList()));
        });
        final BigDecimal minPrice = prodSkuList.stream().map(x -> ObjectUtils.defaultIfNull(x.getPrice(), BigDecimal.ZERO))
                .min(Comparator.comparing(x -> x)).orElse(null);
        final String mainPicUrl = fileList.stream().filter(x -> Objects.equals(x.getFileType(), FileType.MAIN_PIC.getValue()))
                .filter(x -> Objects.equals(x.getOrderNum(), ORDER_NUM_1)).map(StoreProdFileResDTO::getFileUrl).findAny().orElse("");
        // 将用户浏览足迹添加到redis中
        this.updateUserBrowsingToRedis(storeProdId, prodInfoDTO.getStoreId(), prodInfoDTO.getStoreName(), prodInfoDTO.getProdArtNum(), prodInfoDTO.getProdTitle(), minPrice, mainPicUrl);
        // 商品浏览量+1
        redisCache.valueIncr(CacheConstants.PRODUCT_STATISTICS_VIEW_COUNT, storeProdId);
        return prodInfoDTO.setColorList(colorList);
    }

    /**
     * 获取档口商品各个状态数量
     *
     * @param storeId 档口ID
     * @return StoreProdStatusCountResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreProdStatusCountResDTO getStatusNum(Long storeId) {
        return this.storeProdColorMapper.getStatusNum(storeId);
    }

    /**
     * 获取商品状态下分类数量
     *
     * @param cateNumDTO 查询入参
     * @return StoreProdStatusCateCountResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreProdStatusCateCountResDTO> getStatusCateNum(StoreProdStatusCateNumDTO cateNumDTO) {
        // 商城首页展示的分类对应的数量，需要过滤掉  私款商品
        List<StoreProdStatusCateCountDTO> statusCateNumList = this.storeProdMapper.getStatusCateNum(cateNumDTO);
        List<StoreProdStatusCateCountResDTO> countList = new ArrayList<>();
        statusCateNumList.stream().collect(Collectors.groupingBy(StoreProdStatusCateCountDTO::getProdStatus))
                .forEach((prodStatus, cateList) -> countList.add(new StoreProdStatusCateCountResDTO().setProdStatus(prodStatus)
                        .setCateCountList(BeanUtil.copyToList(cateList, StoreProdStatusCateCountResDTO.SPSCCCateCountDTO.class))));
        return countList;
    }

    /**
     * 给设置了所有商品优惠的客户创建优惠
     *
     * @param colorList   档口商品颜色列表
     * @param storeProdId 档口商品ID
     * @param prodArtNum  货号
     */
    private void createStoreCusDiscount(List<StoreProductColor> colorList, Long storeProdId, String prodArtNum) {
        // 档口给那些客户设置了所有商品优惠
        List<StoreCustomer> existCusDiscList = this.storeCusMapper.selectList(new LambdaQueryWrapper<StoreCustomer>()
                .isNotNull(StoreCustomer::getAllProdDiscount).eq(StoreCustomer::getDelFlag, UNDELETED));
        if (CollectionUtils.isEmpty(existCusDiscList)) {
            return;
        }
        List<StoreCustomerProductDiscount> cusDiscList = new ArrayList<>();
        // 为这些客户绑定商品优惠
        colorList.forEach(color -> existCusDiscList.forEach(storeCus -> cusDiscList.add(new StoreCustomerProductDiscount()
                .setDiscount(storeCus.getAllProdDiscount()).setStoreId(storeCus.getStoreId()).setStoreProdId(storeProdId).setProdArtNum(prodArtNum)
                .setStoreCusId(storeCus.getId()).setStoreCusName(storeCus.getCusName()).setStoreProdColorId(color.getId()))));
        this.storeCusProdDiscMapper.insert(cusDiscList);
    }


    /**
     * 获取当前商品的sku列表
     *
     * @param storeProdId 档口商品ID
     * @return Map<String, Integer>
     */
    private Map<String, Integer> getProdStockList(Long storeProdId) {
        // 获取当前商品的库存列表
        List<StoreProductStock> stockList = this.prodStockMapper.selectList(new LambdaQueryWrapper<StoreProductStock>()
                .eq(StoreProductStock::getStoreProdId, storeProdId).eq(StoreProductStock::getDelFlag, Constants.UNDELETED));
        // 该商品storeColorId + size的库存maps
        Map<String, Integer> colorSizeStockMap = new ConcurrentHashMap<>();
        stockList.stream().collect(Collectors.groupingBy(StoreProductStock::getStoreColorId)).forEach((storeColorId, tempStockList) -> {
            colorSizeStockMap.put(storeColorId + ":" + SIZE_30, tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize30(), 0)).reduce(0, Integer::sum));
            colorSizeStockMap.put(storeColorId + ":" + SIZE_31, tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize31(), 0)).reduce(0, Integer::sum));
            colorSizeStockMap.put(storeColorId + ":" + SIZE_32, tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize32(), 0)).reduce(0, Integer::sum));
            colorSizeStockMap.put(storeColorId + ":" + SIZE_33, tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize33(), 0)).reduce(0, Integer::sum));
            colorSizeStockMap.put(storeColorId + ":" + SIZE_34, tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize34(), 0)).reduce(0, Integer::sum));
            colorSizeStockMap.put(storeColorId + ":" + SIZE_35, tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize35(), 0)).reduce(0, Integer::sum));
            colorSizeStockMap.put(storeColorId + ":" + SIZE_36, tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize36(), 0)).reduce(0, Integer::sum));
            colorSizeStockMap.put(storeColorId + ":" + SIZE_37, tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize37(), 0)).reduce(0, Integer::sum));
            colorSizeStockMap.put(storeColorId + ":" + SIZE_38, tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize38(), 0)).reduce(0, Integer::sum));
            colorSizeStockMap.put(storeColorId + ":" + SIZE_39, tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize39(), 0)).reduce(0, Integer::sum));
            colorSizeStockMap.put(storeColorId + ":" + SIZE_40, tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize40(), 0)).reduce(0, Integer::sum));
            colorSizeStockMap.put(storeColorId + ":" + SIZE_41, tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize41(), 0)).reduce(0, Integer::sum));
            colorSizeStockMap.put(storeColorId + ":" + SIZE_42, tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize42(), 0)).reduce(0, Integer::sum));
            colorSizeStockMap.put(storeColorId + ":" + SIZE_43, tempStockList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize43(), 0)).reduce(0, Integer::sum));
        });
        return colorSizeStockMap;
    }

    /**
     * 处理档口商品属性
     *
     * @param storeProd    档口商品实体
     * @param storeProdDTO 档口商品数据传输对象
     */
    private void handleStoreProdProperties(StoreProduct storeProd, StoreProdDTO storeProdDTO) {
        // 上传的文件列表
        final List<StoreProdFileDTO> fileDTOList = storeProdDTO.getFileList();
        // 将文件插入到SysFile表中
        List<SysFile> fileList = BeanUtil.copyToList(fileDTOList, SysFile.class);
        this.fileMapper.insert(fileList);
        // 将文件名称和文件ID映射到Map中
        Map<String, Long> fileMap = fileList.stream().collect(Collectors.toMap(SysFile::getFileName, SysFile::getId));
        // 档口文件（商品主图、主图视频、下载的商品详情）
        List<StoreProductFile> prodFileList = fileDTOList.stream().map(x -> BeanUtil.toBean(x, StoreProductFile.class)
                        .setFileId(fileMap.get(x.getFileName())).setStoreProdId(storeProd.getId()).setStoreId(storeProdDTO.getStoreId()))
                .collect(Collectors.toList());
        this.storeProdFileMapper.insert(prodFileList);
        // 档口类目属性
        this.storeProdCateAttrMapper.insert(BeanUtil.toBean(storeProdDTO.getCateAttr(), StoreProductCategoryAttribute.class)
                .setStoreProdId(storeProd.getId()).setStoreId(storeProd.getStoreId()));
        // 档口详情内容
        StoreProductDetail storeProdDetail = new StoreProductDetail().setDetail(storeProdDTO.getDetail()).setStoreProdId(storeProd.getId());
        this.storeProdDetailMapper.insert(storeProdDetail);
        // 档口服务承诺
        if (ObjectUtils.isNotEmpty(storeProdDTO.getSvc())) {
            this.storeProdSvcMapper.insert(BeanUtil.toBean(storeProdDTO.getSvc(), StoreProductService.class).setStoreProdId(storeProd.getId()));
        }
        // 档口生产工艺信息
        if (ObjectUtils.isNotEmpty(storeProdDTO.getProcess())) {
            this.storeProdProcMapper.insert(BeanUtil.toBean(storeProdDTO.getProcess(), StoreProductProcess.class).setStoreProdId(storeProd.getId()));
        }
    }

    /**
     * 向ES索引新增文档
     *
     * @param storeProd    档口产品
     * @param storeProdDTO 档口产品新增入参
     * @param storeName    档口名称
     */
    private void createESDoc(StoreProduct storeProd, StoreProdDTO storeProdDTO, String storeName) {
        ESProductDTO esProductDTO = this.getESDTO(storeProd, storeProdDTO, storeName);
        try {
            // 向索引中添加数据
            CreateResponse createResponse = esClientWrapper.getEsClient().create(e -> e.index(Constants.ES_IDX_PRODUCT_INFO)
                    .id(storeProd.getId().toString()).document(esProductDTO));
            log.info("createResponse.result() = {}", createResponse.result());
        } catch (Exception e) {
            fsNotice.sendMsg2DefaultChat("新增商品，同步到ES 失败", "storeId:" + storeProd.getStoreId() + " prodArtNum: " + storeProd.getProdArtNum());
            throw new ServiceException("新增商品，同步到ES 失败，storeId:" + storeProd.getStoreId() + " prodArtNum: " + storeProd.getProdArtNum(), HttpStatus.ERROR);
        }
    }

    /**
     * 向ES索引更新文档
     *
     * @param storeProd 档口商品
     * @param updateDTO 档口商品更新入参
     * @param storeName 档口名称
     */
    private void updateESDoc(StoreProduct storeProd, StoreProdDTO updateDTO, String storeName) {
        ESProductDTO esProductDTO = this.getESDTO(storeProd, updateDTO, storeName);
        try {
            UpdateResponse<ESProductDTO> updateResponse = esClientWrapper.getEsClient().update(u -> u
                    .index(Constants.ES_IDX_PRODUCT_INFO).doc(esProductDTO).id(storeProd.getId().toString()), ESProductDTO.class);
            log.info("updateResponse.result() = {}", updateResponse.result());
        } catch (Exception e) {
            fsNotice.sendMsg2DefaultChat("更新商品，同步到ES 失败", "storeProdId: " + storeProd.getId());
            throw new ServiceException("更新商品，同步到ES 失败，storeProdId:" + storeProd.getId(), HttpStatus.ERROR);
        }
    }


    /**
     * 组装ES 入参 DTO
     *
     * @param storeProd 档口商品
     * @param updateDTO 档口商品更新入参
     * @param storeName 档口名称
     * @return
     */
    private ESProductDTO getESDTO(StoreProduct storeProd, StoreProdDTO updateDTO, String storeName) {
        // 获取第一张主图
        String firstMainPic = updateDTO.getFileList().stream().filter(x -> Objects.equals(x.getFileType(), FileType.MAIN_PIC.getValue()))
                .min(Comparator.comparing(StoreProdFileDTO::getOrderNum)).map(StoreProdFileDTO::getFileUrl)
                .orElseThrow(() -> new ServiceException("商品主图不存在!", HttpStatus.ERROR));
        // 是否有主图视频
        boolean hasVideo = updateDTO.getFileList().stream().anyMatch(x -> Objects.equals(x.getFileType(), FileType.MAIN_PIC_VIDEO.getValue()));
        // 获取上一级分类的分类ID 及 分类名称
        ProdCateDTO parCate = this.prodCateMapper.getParentCate(updateDTO.getProdCateId());
        // 获取当前商品的最低价格
        BigDecimal minPrice = updateDTO.getSizeList().stream().min(Comparator.comparing(StoreProdDTO.SPCSizeDTO::getPrice))
                .map(StoreProdDTO.SPCSizeDTO::getPrice).orElseThrow(() -> new ServiceException("商品价格不存在!", HttpStatus.ERROR));
        // 获取使用季节
        String season = updateDTO.getCateAttr().getSuitableSeason();
        // 获取风格
        String style = updateDTO.getCateAttr().getStyle();
        // 组装
        ESProductDTO esProdDTO = BeanUtil.toBean(storeProd, ESProductDTO.class).setHasVideo(hasVideo)
                .setProdCateName(updateDTO.getProdCateName()).setStoreName(storeName).setMainPicUrl(firstMainPic)
                .setCreateTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, storeProd.getCreateTime()))
                .setSaleWeight(WEIGHT_DEFAULT_ZERO.toString()).setRecommendWeight(WEIGHT_DEFAULT_ZERO.toString()).setPopularityWeight(WEIGHT_DEFAULT_ZERO.toString())
                .setParCateId(parCate.getProdCateId().toString()).setParCateName(parCate.getName()).setProdPrice(minPrice.toString());
        if (StringUtils.isNotBlank(season)) {
            esProdDTO.setSeason(season);
        }
        if (StringUtils.isNotBlank(style)) {
            esProdDTO.setStyle(style);
            esProdDTO.setTags(Collections.singletonList(style));
        }
        return esProdDTO;
    }

    /**
     * 删除ES索引文档
     *
     * @param storeProdIdList 文档ID列表
     * @throws IOException
     */
    private void deleteESDoc(List<Long> storeProdIdList) throws IOException {
        try {
            // 删除ES索引文档
            List<BulkOperation> list = storeProdIdList.stream().map(x -> new BulkOperation.Builder().delete(
                    d -> d.id(String.valueOf(x)).index(Constants.ES_IDX_PRODUCT_INFO)).build()).collect(Collectors.toList());
            // 调用bulk方法执行批量插入操作
            BulkResponse bulkResponse = esClientWrapper.getEsClient().bulk(e -> e.index(Constants.ES_IDX_PRODUCT_INFO).operations(list));
            log.info("bulkResponse.items() = {}", bulkResponse.items());
        } catch (IOException | RuntimeException e) {
            // 记录日志并抛出或处理异常
            log.error("删除ES文档失败，商品ID: {}, 错误信息: {}", storeProdIdList, e.getMessage(), e);
            throw e; // 或者做其他补偿处理，比如异步重试
        }
    }

    /**
     * 重新创建ES索引下的文档
     *
     * @param reSaleList 重新上架商品列表
     * @throws IOException
     */
    private void reSaleCreateESDoc(List<StoreProduct> reSaleList) throws IOException {
        if (CollectionUtils.isEmpty(reSaleList)) {
            return;
        }
        List<ProductESDTO> esDTOList = this.storeProdMapper.selectESDTOList(reSaleList.stream()
                .map(StoreProduct::getId).distinct().collect(Collectors.toList()));
        Map<Long, ProductESDTO> esDTOMap = esDTOList.stream().collect(Collectors.toMap(ProductESDTO::getId, Function.identity()));
        // 构建一个批量数据集合
        List<BulkOperation> list = reSaleList.stream().map(x -> {
            final String createTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, x.getCreateTime());
            final ProductESDTO esDTO = Optional.ofNullable(esDTOMap.get(x.getId())).orElseThrow(() -> new ServiceException("档口商品不存在!", HttpStatus.ERROR));
            ESProductDTO esProductDTO = BeanUtil.toBean(x, ESProductDTO.class).setProdCateName(esDTO.getProdCateName()).setMainPicUrl(esDTO.getMainPic())
                    .setParCateId(esDTO.getParCateId()).setParCateName(esDTO.getParCateName()).setProdPrice(esDTO.getProdPrice()).setStoreName(esDTO.getStoreName())
                    .setSeason(esDTO.getSeason()).setStyle(esDTO.getStyle()).setCreateTime(createTime);
            return new BulkOperation.Builder().create(d -> d.document(esProductDTO).id(String.valueOf(x.getId()))
                    .index(Constants.ES_IDX_PRODUCT_INFO)).build();
        }).collect(Collectors.toList());
        // 调用bulk方法执行批量插入操作
        BulkResponse bulkResponse = esClientWrapper.getEsClient().bulk(e -> e.index(Constants.ES_IDX_PRODUCT_INFO).operations(list));
        System.out.println("bulkResponse.items() = " + bulkResponse.items());
    }

    /**
     * 搜图服务同步商品
     *
     * @param storeProductId
     * @param storeProdFiles
     */
    private void sync2ImgSearchServer(Long storeProductId, List<StoreProdFileDTO> storeProdFiles) {
        List<String> picKeys = CollUtil.emptyIfNull(storeProdFiles)
                .stream()
                .filter(o -> FileType.MAIN_PIC.getValue().equals(o.getFileType()))
                .map(StoreProdFileDTO::getFileUrl)
                .collect(Collectors.toList());
        sync2ImgSearchServer(storeProductId, picKeys, true);
    }

    private void sync2ImgSearchServer(Long storeProductId, List<String> picKeys, boolean async) {
        if (async) {
            ThreadUtil.execAsync(() -> {
                        ProductPicSyncResultDTO r =
                                pictureService.sync2ImgSearchServer(new ProductPicSyncDTO(storeProductId, picKeys));
                        log.info("商品图片同步至搜图服务器: id: {}, result: {}", storeProductId, JSONUtil.toJsonStr(r));
                    }
            );
        } else {
            ProductPicSyncResultDTO r =
                    pictureService.sync2ImgSearchServer(new ProductPicSyncDTO(storeProductId, picKeys));
            log.info("商品图片同步至搜图服务器: id: {}, result: {}", storeProductId, JSONUtil.toJsonStr(r));
        }
    }


    /**
     * 更新用户浏览记录到Redis
     * 此方法用于将用户的商品浏览信息更新到Redis缓存中，以便后续推荐或展示相关商品
     *
     * @param storeProdId 商店商品ID
     * @param storeId     商店ID
     * @param storeName   商店名称
     * @param prodArtNum  商品货号
     * @param prodTitle   商品标题
     * @param minPrice    商品的最低价格
     * @param mainPicUrl  商品主图URL
     */
    private void updateUserBrowsingToRedis(Long storeProdId, Long storeId, String storeName, String prodArtNum, String prodTitle, BigDecimal minPrice, String mainPicUrl) {
        final Long userId = SecurityUtils.getUserId();
        if (ObjectUtils.isEmpty(userId)) {
            return;
        }
        List<UserBrowsingHisDTO> browsingList = CollUtil.defaultIfEmpty(this.redisCache
                .getCacheObject(CacheConstants.USER_BROWSING_HISTORY + SecurityUtils.getUserId()), new ArrayList<>());
        browsingList.add(new UserBrowsingHisDTO().setUserId(userId).setProdArtNum(prodArtNum).setProdTitle(prodTitle)
                .setStoreId(storeId).setStoreName(storeName).setProdPrice(minPrice).setMainPicUrl(mainPicUrl)
                .setBrowsingTime(new Date()).setStoreProdId(storeProdId));
        this.redisCache.setCacheObject(CacheConstants.USER_BROWSING_HISTORY + SecurityUtils.getUserId(), browsingList);
    }

    /**
     * 新增商品时，新增系统通知
     *
     * @param storeProd 档口商品
     * @param storeName 档口名称
     */
    private void createNotice(StoreProduct storeProd, String storeName) {
        // 新增档口 商品动态 通知公告
        Long userId = SecurityUtils.getUserId();
        // 新增一条档口消息通知
        Notice notice = new Notice().setNoticeTitle(storeName + "商品上新啦!").setNoticeType(NoticeType.NOTICE.getValue())
                .setNoticeContent(storeName + "上新了货号为: " + storeProd.getProdArtNum() + " 的商品!请及时关注!")
                .setOwnerType(NoticeOwnerType.SYSTEM.getValue()).setStoreId(storeProd.getStoreId())
                .setUserId(userId).setPerpetuity(NoticePerpetuityType.PERMANENT.getValue());
        this.noticeMapper.insert(notice);
        final Date voucherDate = java.sql.Date.valueOf(LocalDate.now());
        // 新增消息通知列表
        List<UserNotice> userNoticeList = new ArrayList<>();
        // 新增档口商品动态
        userNoticeList.add(new UserNotice().setNoticeId(notice.getId())
                .setUserId(userId).setReadStatus(NoticeReadType.UN_READ.getValue()).setVoucherDate(voucherDate)
                .setTargetNoticeType(UserNoticeType.PRODUCT_DYNAMIC.getValue()));
        List<UserSubscriptions> userSubList = this.userSubMapper.selectList(new LambdaQueryWrapper<UserSubscriptions>()
                .eq(UserSubscriptions::getStoreId, storeProd.getStoreId()));
        if (CollectionUtils.isNotEmpty(userSubList)) {
            userSubList.forEach(x -> userNoticeList.add(new UserNotice().setNoticeId(notice.getId())
                    .setUserId(x.getUserId()).setReadStatus(NoticeReadType.UN_READ.getValue()).setVoucherDate(voucherDate)
                    .setTargetNoticeType(UserNoticeType.FOCUS_STORE.getValue())));
        }
        if (CollectionUtils.isEmpty(userNoticeList)) {
            return;
        }
        this.userNoticeMapper.insert(userNoticeList);
    }

    /**
     * 更新商品 新增消息通知
     *
     * @param storeProd 档口商品
     * @param storeName 档口名称
     */
    private void updateNotice(StoreProduct storeProd, String storeName) {
        // 新增档口 商品动态 通知公告
        Long userId = SecurityUtils.getUserId();
        // 新增一条档口消息通知
        Notice notice = new Notice().setNoticeTitle(storeName + "商品更新啦!").setNoticeType(NoticeType.NOTICE.getValue())
                .setNoticeContent(storeName + "更新了货号为: " + storeProd.getProdArtNum() + " 的商品!请及时关注!")
                .setOwnerType(NoticeOwnerType.SYSTEM.getValue()).setStoreId(storeProd.getStoreId())
                .setUserId(userId).setPerpetuity(NoticePerpetuityType.PERMANENT.getValue());
        this.noticeMapper.insert(notice);
        final Date voucherDate = java.sql.Date.valueOf(LocalDate.now());
        // 新增消息通知列表
        List<UserNotice> userNoticeList = new ArrayList<>();
        userNoticeList.add(new UserNotice().setNoticeId(notice.getId())
                .setUserId(userId).setReadStatus(NoticeReadType.UN_READ.getValue()).setVoucherDate(voucherDate)
                .setTargetNoticeType(UserNoticeType.PRODUCT_DYNAMIC.getValue()));
        // 关注档口 或 收藏商品用户
        List<UserFocusAndFavUserIdDTO> targetList = this.userSubMapper.selectFocusAndFavUserIdList(storeProd.getStoreId(), storeProd.getId());
        if (CollectionUtils.isNotEmpty(targetList)) {
            targetList.forEach(target -> userNoticeList.add(new UserNotice().setNoticeId(notice.getId())
                    .setUserId(target.getUserId()).setReadStatus(NoticeReadType.UN_READ.getValue()).setVoucherDate(voucherDate)
                    .setTargetNoticeType(target.getTargetNoticeType())));
        }
        if (CollectionUtils.isEmpty(userNoticeList)) {
            return;
        }
        this.userNoticeMapper.insert(userNoticeList);
    }

    /**
     * 下架或重新上架商品时，新增消息列表
     *
     * @param storeProdList 档口商品列表
     * @param offSale       true 下架商品 false 重新上架商品
     * @param storeId       档口ID
     */
    private void offSaleOrReSaleProd(List<StoreProduct> storeProdList, Boolean offSale, Long storeId) {
        // 新增档口 商品动态 通知公告
        Long userId = SecurityUtils.getUserId();
        Store store = redisCache.getCacheObject(CacheConstants.STORE_KEY + storeId);
        // 新增消息通知列表
        List<UserNotice> userNoticeList = new ArrayList<>();
        final Date voucherDate = java.sql.Date.valueOf(LocalDate.now());
        storeProdList.forEach(storeProd -> {
            // 新增一条档口消息通知
            Notice notice = new Notice().setNoticeType(NoticeType.NOTICE.getValue()).setUserId(userId)
                    .setPerpetuity(NoticePerpetuityType.PERMANENT.getValue())
                    .setOwnerType(NoticeOwnerType.SYSTEM.getValue()).setStoreId(storeProd.getStoreId())
                    .setNoticeTitle(ObjectUtils.isNotEmpty(store) ? store.getStoreName() : "" + "商品" + (offSale ? "下架" : "重新上架") + "啦!")
                    .setNoticeContent(ObjectUtils.isNotEmpty(store) ? store.getStoreName() : "" + (offSale ? "下架" : "重新上架")
                            + "了货号为: " + storeProd.getProdArtNum() + " 的商品!请及时关注!");
            this.noticeMapper.insert(notice);
            // 关注档口 或 收藏商品用户
            List<UserFocusAndFavUserIdDTO> targetList = this.userSubMapper.selectFocusAndFavUserIdList(storeProd.getStoreId(), storeProd.getId());
            if (CollectionUtils.isNotEmpty(targetList)) {
                targetList.forEach(x -> userNoticeList.add(new UserNotice().setNoticeId(notice.getId())
                        .setUserId(x.getUserId()).setReadStatus(NoticeReadType.UN_READ.getValue()).setVoucherDate(voucherDate)
                        .setTargetNoticeType(x.getTargetNoticeType())));
            }
        });
        if (CollectionUtils.isEmpty(userNoticeList)) {
            return;
        }
        this.userNoticeMapper.insert(userNoticeList);
    }

    /**
     * @param updateProdStatusMap 商品状态更新map
     * @throws IOException
     */
    private void updateESProdStatus(Map<Long, Integer> updateProdStatusMap) throws IOException {
        // 构建一个批量数据集合
        List<BulkOperation> list = new ArrayList<>();
        updateProdStatusMap.forEach((storeProdId, prodStatus) -> {
            // 构建部分文档更新请求
            list.add(new BulkOperation.Builder().update(u -> u
                            .action(a -> a.doc(new HashMap<String, Object>() {{
                                put("prodStatus", prodStatus);
                            }}))
                            .id(String.valueOf(storeProdId))
                            .index(Constants.ES_IDX_PRODUCT_INFO))
                    .build());
        });
        try {
            // 调用bulk方法执行批量更新操作
            BulkResponse bulkResponse = esClientWrapper.getEsClient().bulk(e -> e.index(Constants.ES_IDX_PRODUCT_INFO).operations(list));
            log.info("bulkResponse.result() = {}", bulkResponse.items());
        } catch (IOException | RuntimeException e) {
            // 记录日志并抛出或处理异常
            log.error("向ES更新商品状态失败，商品ID: {}, 错误信息: {}", updateProdStatusMap.keySet(), e.getMessage());
            throw e; // 或者做其他补偿处理，比如异步重试
        }
    }

    /**
     * 组装前端返回的map
     *
     * @param cateAttr 数据库类目属性
     * @return Map
     */
    private Map<String, String> getCateAttrMap(StoreProductCategoryAttribute cateAttr) {
        if (ObjectUtils.isEmpty(cateAttr)) {
            return new HashMap<>();
        }
        // 使用 LinkedHashMap 保持属性顺序
        Map<String, String> cateAttrMap = new LinkedHashMap<>();
        // 1. 帮面材质
        if (ObjectUtils.isNotEmpty(cateAttr.getUpperMaterial())) {
            cateAttrMap.put(Constants.CATE_RELATE_MAP.get(UPPER_MATERIAL), cateAttr.getUpperMaterial());
        }
        // 2. 靴筒内里材质
        if (ObjectUtils.isNotEmpty(cateAttr.getShaftLiningMaterial())) {
            cateAttrMap.put(Constants.CATE_RELATE_MAP.get(SHAFT_LINING_MATERIAL), cateAttr.getShaftLiningMaterial());
        }
        // 鞋面材质
        if (ObjectUtils.isNotEmpty(cateAttr.getShaftMaterial())) {
            cateAttrMap.put(Constants.CATE_RELATE_MAP.get(SHAFT_MATERIAL), cateAttr.getShaftMaterial());
        }
        // 靴款品名
        if (ObjectUtils.isNotEmpty(cateAttr.getShoeStyleName())) {
            cateAttrMap.put(Constants.CATE_RELATE_MAP.get(SHOE_STYLE_NAME), cateAttr.getShoeStyleName());
        }
        // 筒高
        if (ObjectUtils.isNotEmpty(cateAttr.getShaftHeight())) {
            cateAttrMap.put(Constants.CATE_RELATE_MAP.get(SHAFT_HEIGHT), cateAttr.getShaftHeight());
        }
        // 3. 鞋垫材质
        if (ObjectUtils.isNotEmpty(cateAttr.getInsoleMaterial())) {
            cateAttrMap.put(Constants.CATE_RELATE_MAP.get(INSOLE_MATERIAL), cateAttr.getInsoleMaterial());
        }
        // 4. 上市季节年份
        if (ObjectUtils.isNotEmpty(cateAttr.getReleaseYearSeason())) {
            cateAttrMap.put(Constants.CATE_RELATE_MAP.get(RELEASE_YEAR_SEASON), cateAttr.getReleaseYearSeason());
        }
        // 5. 后跟高
        if (ObjectUtils.isNotEmpty(cateAttr.getHeelHeight())) {
            cateAttrMap.put(Constants.CATE_RELATE_MAP.get(HEEL_HEIGHT), cateAttr.getHeelHeight());
        }
        // 6. 跟底款式
        if (ObjectUtils.isNotEmpty(cateAttr.getHeelType())) {
            cateAttrMap.put(Constants.CATE_RELATE_MAP.get(HEEL_TYPE), cateAttr.getHeelType());
        }
        // 7. 鞋头款式
        if (ObjectUtils.isNotEmpty(cateAttr.getToeStyle())) {
            cateAttrMap.put(Constants.CATE_RELATE_MAP.get(TOE_STYLE), cateAttr.getToeStyle());
        }
        // 8. 适合季节
        if (ObjectUtils.isNotEmpty(cateAttr.getSuitableSeason())) {
            cateAttrMap.put(Constants.CATE_RELATE_MAP.get(SUITABLE_SEASON), cateAttr.getSuitableSeason());
        }
        // 9. 开口深度
        if (ObjectUtils.isNotEmpty(cateAttr.getCollarDepth())) {
            cateAttrMap.put(Constants.CATE_RELATE_MAP.get(COLLAR_DEPTH), cateAttr.getCollarDepth());
        }
        // 10. 鞋底材质
        if (ObjectUtils.isNotEmpty(cateAttr.getOutsoleMaterial())) {
            cateAttrMap.put(Constants.CATE_RELATE_MAP.get(OUTSOLE_MATERIAL), cateAttr.getOutsoleMaterial());
        }
        // 11. 风格
        if (ObjectUtils.isNotEmpty(cateAttr.getStyle())) {
            cateAttrMap.put(Constants.CATE_RELATE_MAP.get(STYLE), cateAttr.getStyle());
        }
        // 12. 款式
        if (ObjectUtils.isNotEmpty(cateAttr.getDesign())) {
            cateAttrMap.put(Constants.CATE_RELATE_MAP.get(DESIGN), cateAttr.getDesign());
        }
        // 13. 皮质特征
        if (ObjectUtils.isNotEmpty(cateAttr.getLeatherFeatures())) {
            cateAttrMap.put(Constants.CATE_RELATE_MAP.get(LEATHER_FEATURES), cateAttr.getLeatherFeatures());
        }
        // 14. 制作工艺
        if (ObjectUtils.isNotEmpty(cateAttr.getManufacturingProcess())) {
            cateAttrMap.put(Constants.CATE_RELATE_MAP.get(MANUFACTURING_PROCESS), cateAttr.getManufacturingProcess());
        }
        // 15. 图案
        if (ObjectUtils.isNotEmpty(cateAttr.getPattern())) {
            cateAttrMap.put(Constants.CATE_RELATE_MAP.get(PATTERN), cateAttr.getPattern());
        }
        // 16. 闭合方式
        if (ObjectUtils.isNotEmpty(cateAttr.getClosureType())) {
            cateAttrMap.put(Constants.CATE_RELATE_MAP.get(CLOSURE_TYPE), cateAttr.getClosureType());
        }
        // 17. 适用场景
        if (ObjectUtils.isNotEmpty(cateAttr.getOccasion())) {
            cateAttrMap.put(Constants.CATE_RELATE_MAP.get(OCCASION), cateAttr.getOccasion());
        }
        // 18. 厚薄
        if (ObjectUtils.isNotEmpty(cateAttr.getThickness())) {
            cateAttrMap.put(Constants.CATE_RELATE_MAP.get(THICKNESS), cateAttr.getThickness());
        }
        // 19. 流行元素
        if (ObjectUtils.isNotEmpty(cateAttr.getFashionElements())) {
            cateAttrMap.put(Constants.CATE_RELATE_MAP.get(FASHION_ELEMENTS), cateAttr.getFashionElements());
        }
        // 20. 适用对象
        if (ObjectUtils.isNotEmpty(cateAttr.getSuitablePerson())) {
            cateAttrMap.put(Constants.CATE_RELATE_MAP.get(SUITABLE_PERSON), cateAttr.getSuitablePerson());
        }
        return cateAttrMap;
    }


}
