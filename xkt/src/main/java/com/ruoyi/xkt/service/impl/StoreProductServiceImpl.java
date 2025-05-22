package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.CreateResponse;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.es.EsClientWrapper;
import com.ruoyi.system.domain.dto.productCategory.ProdCateDTO;
import com.ruoyi.system.mapper.SysProductCategoryMapper;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.dto.es.ESProductDTO;
import com.ruoyi.xkt.dto.picture.ProductPicSyncDTO;
import com.ruoyi.xkt.dto.picture.ProductPicSyncResultDTO;
import com.ruoyi.xkt.dto.storeColor.StoreColorDTO;
import com.ruoyi.xkt.dto.storeProdCateAttr.StoreProdCateAttrDTO;
import com.ruoyi.xkt.dto.storeProdColor.StoreProdColorDTO;
import com.ruoyi.xkt.dto.storeProdColorPrice.StoreProdColorPriceSimpleDTO;
import com.ruoyi.xkt.dto.storeProdColorSize.StoreProdColorSizeDTO;
import com.ruoyi.xkt.dto.storeProdProcess.StoreProdProcessDTO;
import com.ruoyi.xkt.dto.storeProdSvc.StoreProdSvcDTO;
import com.ruoyi.xkt.dto.storeProduct.*;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdFileDTO;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdFileResDTO;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdMainPicDTO;
import com.ruoyi.xkt.enums.EProductStatus;
import com.ruoyi.xkt.enums.FileType;
import com.ruoyi.xkt.enums.ProductSizeStatus;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IPictureService;
import com.ruoyi.xkt.service.IStoreProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
    final StoreProductColorPriceMapper storeProdColorPriceMapper;
    final StoreColorMapper storeColorMapper;
    final SysFileMapper fileMapper;
    final StoreProductColorSizeMapper storeProdColorSizeMapper;
    final StoreProductProcessMapper storeProdProcMapper;
    final StoreMapper storeMapper;
    final EsClientWrapper esClientWrapper;
    final SysProductCategoryMapper prodCateMapper;
    final UserFavoritesMapper userFavMapper;
    final DailyProdTagMapper prodTagMapper;
    final StoreProductStockMapper prodStockMapper;
    final StoreCustomerMapper storeCusMapper;
    final StoreCustomerProductDiscountMapper storeCusProdDiscMapper;
    final IPictureService pictureService;
    final StoreProductStatisticsMapper storeProductStatisticsMapper;


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
        // 档口商品颜色尺码列表
        List<StoreProdColorSizeDTO> sizeList = this.storeProdColorSizeMapper.selectListByStoreProdId(storeProdId);
        // 档口颜色价格列表
        List<StoreProdColorPriceSimpleDTO> priceList = this.storeProdColorPriceMapper.selectListByStoreProdId(storeProdId);
        // 档口商品详情
        StoreProductDetail prodDetail = this.storeProdDetailMapper.selectByStoreProdId(storeProdId);
        // 档口服务承诺
        StoreProductService storeProductSvc = this.storeProdSvcMapper.selectByStoreProdId(storeProdId);
        // 档口生产工艺信息
        StoreProductProcess prodProcess = this.storeProdProcMapper.selectByStoreProdId(storeProdId);
        return storeProdResDTO.setFileList(fileResList).setAllColorList(allColorList).setDetail(prodDetail.getDetail())
                .setColorList(colorList).setPriceList(priceList).setSizeList(sizeList)
                .setCateAttr(BeanUtil.toBean(cateAttr, StoreProdCateAttrDTO.class))
                .setSvc(BeanUtil.toBean(storeProductSvc, StoreProdSvcDTO.class))
                .setProcess(BeanUtil.toBean(prodProcess, StoreProdProcessDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public StoreProdPicSpaceResDTO getStoreProductPicSpace(StoreProdPicSpaceDTO spaceDTO) {
        LambdaQueryWrapper<StoreProduct> queryWrapper = new LambdaQueryWrapper<StoreProduct>().eq(StoreProduct::getId, spaceDTO.getStoreId())
                .eq(StoreProduct::getDelFlag, Constants.UNDELETED);
        if (StringUtils.isNotBlank(spaceDTO.getProdArtNum())) {
            queryWrapper.like(StoreProduct::getProdArtNum, spaceDTO.getProdArtNum());
        }
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(queryWrapper);
        List<Long> storeProdIdList = storeProdList.stream().map(StoreProduct::getId).collect(Collectors.toList());
        Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                        .eq(Store::getId, spaceDTO.getStoreId()).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        return StoreProdPicSpaceResDTO.builder().storeId(store.getId()).storeName(store.getStoreName())
                .fileList(this.storeProdFileMapper.selectPicSpaceList(store.getId(), FileType.DOWNLOAD.getValue(), storeProdIdList)).build();
    }

    /**
     * 查询档口商品列表
     *
     * @param storeProduct 档口商品
     * @return 档口商品
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreProduct> selectStoreProductList(StoreProduct storeProduct) {
        return storeProdMapper.selectStoreProductList(storeProduct);
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
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        // 调用Mapper方法查询商店产品分页信息
        List<StoreProdPageResDTO> prodList = storeProdColorMapper.selectStoreProdColorPage(pageDTO);
        // 如果查询结果为空，则直接返回空列表
        if (CollectionUtils.isEmpty(prodList)) {
            return Page.empty(pageDTO.getPageSize(), pageDTO.getPageNum());
        }
        // 提取查询结果中的商店产品ID列表
        List<Long> storeProdIdList = prodList.stream().map(StoreProdPageResDTO::getStoreProdId).collect(Collectors.toList());
        // 查找排名第一个商品主图列表
        List<StoreProdMainPicDTO> mainPicList = this.storeProdFileMapper.selectMainPicByStoreProdIdList(storeProdIdList, FileType.MAIN_PIC.getValue(), 1);
        Map<Long, String> mainPicMap = CollectionUtils.isEmpty(mainPicList) ? new HashMap<>() : mainPicList.stream()
                .collect(Collectors.toMap(StoreProdMainPicDTO::getStoreProdId, StoreProdMainPicDTO::getFileUrl));
        // 查找档口商品的标准尺码
        LambdaQueryWrapper<StoreProductColorSize> queryWrapper = new LambdaQueryWrapper<StoreProductColorSize>().in(StoreProductColorSize::getStoreProdId, storeProdIdList)
                .eq(StoreProductColorSize::getDelFlag, Constants.UNDELETED).eq(StoreProductColorSize::getStandard, ProductSizeStatus.STANDARD.getValue());
        List<StoreProductColorSize> standardSizeList = this.storeProdColorSizeMapper.selectList(queryWrapper);
        // 将标准尺码列表转换为映射，以便后续处理
        Map<Long, List<Integer>> standardSizeMap = CollectionUtils.isEmpty(standardSizeList) ? new HashMap<>() : standardSizeList.stream().collect(Collectors
                .groupingBy(StoreProductColorSize::getStoreProdId, Collectors.mapping(StoreProductColorSize::getSize, Collectors.toList())));
        // 为每个产品设置主图URL和标准尺码列表
        prodList.forEach(x -> x.setMainPicUrl(mainPicMap.get(x.getStoreProdId())).setStandardSizeList(standardSizeMap.get(x.getStoreProdId())));
        // 使用公共方法转换 PageInfo 到 Page
        return Page.convert(new PageInfo<>(prodList), BeanUtil.copyToList(prodList, StoreProdPageResDTO.class));
    }

    @Override
    @Transactional
    public int insertStoreProduct(StoreProdDTO storeProdDTO) throws IOException {


        // TODO 富文本标签过滤
        // TODO 富文本标签过滤
        // TODO 富文本标签过滤
        // TODO 富文本标签过滤
        // TODO 富文本标签过滤
        // TODO 富文本标签过滤
        // TODO 富文本标签过滤


        // 组装StoreProduct数据
        StoreProduct storeProd = BeanUtil.toBean(storeProdDTO, StoreProduct.class).setVoucherDate(DateUtils.getNowDate())
                .setRecommendWeight(0L).setSaleWeight(0L).setPopularityWeight(0L);
        int count = this.storeProdMapper.insert(storeProd);
        // 处理编辑档口商品颜色
        this.handleStoreProdColorList(storeProd, storeProdDTO.getColorList(), storeProd.getId(), storeProd.getStoreId(), Boolean.TRUE);
        // 处理编辑档口商品颜色尺码
        this.handleStoreProdColorSizeList(storeProdDTO.getSizeList(), storeProd.getId(), Boolean.TRUE);
        // 处理StoreProduct其它属性
        this.handleStoreProdProperties(storeProd, storeProdDTO);
        // 向ES索引: product_info 创建文档
        this.createESDoc(storeProd, storeProdDTO);
        // 搜图服务同步
        sync2ImgSearchServer(storeProd.getId(), storeProdDTO.getFileList());
        return count;
    }


    /**
     * 修改档口商品
     *
     * @param storeProdDTO 档口商品
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStoreProduct(final Long storeProdId, StoreProdDTO storeProdDTO) throws IOException {


        // TODO 富文本标签过滤
        // TODO 富文本标签过滤
        // TODO 富文本标签过滤
        // TODO 富文本标签过滤
        // TODO 富文本标签过滤
        // TODO 富文本标签过滤
        // TODO 富文本标签过滤


        StoreProduct storeProd = Optional.ofNullable(this.storeProdMapper.selectOne(new LambdaQueryWrapper<StoreProduct>()
                        .eq(StoreProduct::getId, storeProdId).eq(StoreProduct::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口商品不存在!", HttpStatus.ERROR));
        // 更新档口商品信息
        BeanUtil.copyProperties(storeProdDTO, storeProd);
        int count = this.storeProdMapper.updateById(storeProd);
        // 档口文件（商品主图、主图视频、下载的商品详情）的del_flag置为2
        this.storeProdFileMapper.updateDelFlagByStoreProdId(storeProdId);
        // 档口类目属性列表的 del_flag置为2
        this.storeProdCateAttrMapper.updateDelFlagByStoreProdId(storeProdId);
        // 档口颜色价格列表的del_flag置为2
        this.storeProdColorPriceMapper.updateDelFlagByStoreProdId(storeProdId);
        // 档口详情内容的del_flag置为2
        this.storeProdDetailMapper.updateDelFlagByStoreProdId(storeProdId);
        // 档口服务承诺的del_flag置为2
        this.storeProdSvcMapper.updateDelFlagByStoreProdId(storeProdId);
        // 档口工艺信息的del_flag置为2
        this.storeProdProcMapper.updateDelFlagByStoreProdId(storeProdId);
        // 处理更新逻辑
        this.handleStoreProdProperties(storeProd, storeProdDTO);
        // 处理编辑档口商品颜色
        this.handleStoreProdColorList(storeProd, storeProdDTO.getColorList(), storeProdId, storeProd.getStoreId(), Boolean.FALSE);
        // 处理编辑档口商品颜色尺码
        this.handleStoreProdColorSizeList(storeProdDTO.getSizeList(), storeProdId, Boolean.FALSE);
        // 更新索引: product_info 的文档
        this.updateESDoc(storeProd, storeProdDTO);
        // 搜图服务同步
        sync2ImgSearchServer(storeProd.getId(), storeProdDTO.getFileList());
        return count;
    }


    /**
     * 处理店铺商品颜色尺码列表
     *
     * @param sizeDTOList 店铺商品颜色尺码DTO列表
     * @param storeProdId 店铺商品ID
     * @param isInsert    是否是插入操作
     */
    private void handleStoreProdColorSizeList(List<StoreProdColorSizeDTO> sizeDTOList, Long storeProdId, Boolean isInsert) {
        if (isInsert) {
            // 如果是插入操作，直接插入新的颜色尺码信息
            List<StoreProductColorSize> toAddList = sizeDTOList.stream().filter(x -> ObjectUtils.isEmpty(x.getStoreProdColorSizeId()))
                    .map(x -> BeanUtil.toBean(x, StoreProductColorSize.class).setStoreProdId(storeProdId)).collect(Collectors.toList());
            this.storeProdColorSizeMapper.insert(toAddList);
        } else {
            // 查询当前商品最新的颜色，并与现在做对比
            List<StoreProductColor> dbColorList = this.storeProdColorMapper.selectList(new LambdaQueryWrapper<StoreProductColor>()
                    .eq(StoreProductColor::getStoreProdId, storeProdId).eq(StoreProductColor::getDelFlag, Constants.UNDELETED));
            List<Long> dbColorIdList = dbColorList.stream().map(StoreProductColor::getId).collect(Collectors.toList());
            List<Long> newColorIdList = sizeDTOList.stream().map(StoreProdColorSizeDTO::getStoreColorId).collect(Collectors.toList());
            // 判断这两个list 内容是否完全一致（不考虑顺序，只考虑内容）
            boolean colorEqual = CollectionUtils.isEqualCollection(dbColorIdList, newColorIdList);

            // 如果不是插入操作，首先获取数据库中的颜色尺码信息
            List<StoreProductColorSize> colorSizeList = Optional.ofNullable(this.storeProdColorSizeMapper.selectList(new LambdaQueryWrapper<StoreProductColorSize>()
                            .eq(StoreProductColorSize::getStoreProdId, storeProdId).eq(StoreProductColorSize::getDelFlag, Constants.UNDELETED)))
                    .orElseThrow(() -> new ServiceException("该档口下没有商品及颜色", HttpStatus.ERROR));
            // 标准尺码是否一致
            List<Integer> dbStandardList = colorSizeList.stream().filter(x -> Objects.equals(x.getStandard(), ProductSizeStatus.STANDARD.getValue()))
                    .map(StoreProductColorSize::getSize).distinct().collect(Collectors.toList());
            List<Integer> newStandardList = sizeDTOList.stream().filter(x -> Objects.equals(x.getStandard(), ProductSizeStatus.STANDARD.getValue()))
                    .map(StoreProdColorSizeDTO::getSize).collect(Collectors.toList());
            boolean standardEqual = CollectionUtils.isEqualCollection(dbStandardList, newStandardList);
            // 如果两项都没变动，则不做任何调整
            if (colorEqual && standardEqual) {
                return;
            }
            boolean colorUpdate = Boolean.FALSE;
            // 颜色不相等
            if (!colorEqual) {
                // 新增的颜色
                List<Long> addColorIdList = newColorIdList.stream().filter(x -> !dbColorIdList.contains(x)).collect(Collectors.toList());
                List<StoreProductColorSize> toUpdateList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(addColorIdList)) {
                    Map<Integer, Integer> sizeMap = sizeDTOList.stream().collect(Collectors.toMap(StoreProdColorSizeDTO::getSize, StoreProdColorSizeDTO::getStandard));
                    sizeMap.forEach((size, standard) -> addColorIdList.forEach(storeColorId -> toUpdateList
                            .add(new StoreProductColorSize().setSize(size).setStoreColorId(storeColorId).setStoreProdId(storeProdId).setStandard(standard))));
                }
                // 删除的颜色
                List<Long> deleteColorIdList = dbColorIdList.stream().filter(x -> !newColorIdList.contains(x)).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(deleteColorIdList)) {
                    toUpdateList.addAll(colorSizeList.stream().filter(x -> deleteColorIdList.contains(x.getStoreColorId())).peek(x -> x.setDelFlag(DELETED)).collect(Collectors.toList()));
                }
                if (CollectionUtils.isNotEmpty(toUpdateList)) {
                    this.storeProdColorSizeMapper.insertOrUpdate(toUpdateList);
                }
                colorUpdate = Boolean.TRUE;
            }
            if (!standardEqual) {
                // 如果更新过颜色，则重新从数据库获取数据
                if (colorUpdate) {
                    colorSizeList = Optional.ofNullable(this.storeProdColorSizeMapper.selectList(new LambdaQueryWrapper<StoreProductColorSize>()
                                    .eq(StoreProductColorSize::getStoreProdId, storeProdId).eq(StoreProductColorSize::getDelFlag, Constants.UNDELETED)))
                            .orElseThrow(() -> new ServiceException("该档口下没有商品及颜色", HttpStatus.ERROR));
                }
                // 新增的标准尺码
                List<Integer> createStandardList = newStandardList.stream().filter(item -> !dbStandardList.contains(item)).collect(Collectors.toList());
                List<StoreProductColorSize> toUpdateList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(createStandardList)) {
                    toUpdateList.addAll(colorSizeList.stream().filter(item -> createStandardList.contains(item.getSize())).peek(x -> x.setStandard(ProductSizeStatus.STANDARD.getValue())).collect(Collectors.toList()));
                }
                // 删除的标准尺码
                List<Integer> deleteStandardList = dbStandardList.stream().filter(item -> !newStandardList.contains(item)).collect(Collectors.toList());
                // 将这些标准尺码的standard置为0
                if (CollectionUtils.isNotEmpty(deleteStandardList)) {
                    toUpdateList.addAll(colorSizeList.stream().filter(x -> deleteStandardList.contains(x.getSize())).peek(x -> x.setStandard(ProductSizeStatus.UN_STANDARD.getValue())).collect(Collectors.toList()));
                }
                if (CollectionUtils.isNotEmpty(toUpdateList)) {
                    this.storeProdColorSizeMapper.insertOrUpdate(toUpdateList);
                }
            }
        }
    }

    /**
     * 处理店铺商品颜色列表
     *
     * @param storeProd    档口商品
     * @param colorDTOList 商品颜色DTO列表
     * @param storeProdId  店铺商品ID
     * @param storeId      店铺ID
     * @param isInsert     是否是插入操作
     */
    private void handleStoreProdColorList(StoreProduct storeProd, List<StoreProdColorDTO> colorDTOList, Long storeProdId, Long storeId, Boolean isInsert) {
        if (isInsert) {
            // 过滤出需要添加的商品颜色，并转换为StoreProductColor对象
            List<StoreProductColor> toAddList = colorDTOList.stream().filter(x -> ObjectUtils.isEmpty(x.getStoreProdColorId()))
                    .map(x -> BeanUtil.toBean(x, StoreProductColor.class).setStoreProdId(storeProdId).setStoreId(storeId)).collect(Collectors.toList());
            // 如果是插入操作，直接添加新的商品颜色
            this.storeProdColorMapper.insert(toAddList);
            // 给设置了所有商品优惠的客户 新增优惠
            this.createStoreCusDiscount(toAddList, storeProd);
        } else {
            List<StoreProductColor> dbColorList = Optional.ofNullable(this.storeProdColorMapper.selectList(new LambdaQueryWrapper<StoreProductColor>()
                            .eq(StoreProductColor::getStoreProdId, storeProdId).eq(StoreProductColor::getDelFlag, Constants.UNDELETED)
                            .eq(StoreProductColor::getStoreId, storeId)))
                    .orElseThrow(() -> new ServiceException("该档口下没有商品及颜色", HttpStatus.ERROR));
            final List<Long> dbStoreColorIdList = dbColorList.stream().map(StoreProductColor::getStoreColorId).collect(Collectors.toList());
            final List<Long> newStoreColorIdList = colorDTOList.stream().map(StoreProdColorDTO::getStoreColorId).collect(Collectors.toList());
            // 新增的颜色
            List<StoreProductColor> newColorList = colorDTOList.stream().filter(x -> !dbStoreColorIdList.contains(x.getStoreColorId()))
                    .map(x -> BeanUtil.toBean(x, StoreProductColor.class).setStoreProdId(storeProdId).setStoreId(storeId)).collect(Collectors.toList());
            // 给设置了所有商品优惠的客户 新增优惠
            this.createStoreCusDiscount(newColorList, storeProd);
            // 删除的颜色
            List<StoreProductColor> deleteColorList = dbColorList.stream().filter(x -> !newStoreColorIdList.contains(x.getStoreColorId()))
                    .peek(x -> x.setDelFlag(Constants.DELETED)).collect(Collectors.toList());
            // 将deleteColorList添加到newColorList中 并且是null-safe
            CollectionUtils.addAll(newColorList, deleteColorList);
            if (CollectionUtils.isNotEmpty(newColorList)) {
                this.storeProdColorMapper.insertOrUpdate(newColorList);
            }
        }
    }


    /**
     * 更新档口商品的状态
     * 此方法首先根据提供的商品ID列表查询数据库中的商品信息，然后更新这些商品的状态
     * 如果查询结果为空，则抛出异常；否则，遍历查询到的商品列表，设置新的商品状态，并保存更新
     *
     * @param prodStatusDTO 包含需要更新的商品ID列表和新的商品状态的传输对象
     * @throws ServiceException 如果档口商品不存在，则抛出此异常
     */
    @Override
    @Transactional
    public void updateStoreProductStatus(StoreProdStatusDTO prodStatusDTO) throws IOException {
        // 根据商品ID列表查询数据库中的商品信息
        List<StoreProduct> storeProdList = this.storeProdMapper.selectByIds(prodStatusDTO.getStoreProdIdList());
        // 检查查询结果是否为空，如果为空，则抛出异常
        if (CollectionUtils.isEmpty(storeProdList)) {
            throw new ServiceException("档口商品不存在!", HttpStatus.ERROR);
        }
        List<StoreProduct> reSaleList = new ArrayList<>();
        // 遍历查询到的商品列表，设置新的商品状态
        storeProdList.forEach(x -> {
            // 已下架上的商品重新上架
            if (Objects.equals(x.getProdStatus(), EProductStatus.OFF_SALE.getValue())
                    && Objects.equals(prodStatusDTO.getProdStatus(), EProductStatus.ON_SALE.getValue())) {
                reSaleList.add(x);
            }
            x.setProdStatus(prodStatusDTO.getProdStatus());
        });
        // 保存更新后的商品列表到数据库
        this.storeProdMapper.updateById(storeProdList);
        // 筛选商品状态为已下架，则删除ES文档
        if (Objects.equals(prodStatusDTO.getProdStatus(), EProductStatus.OFF_SALE.getValue())) {
            this.deleteESDoc(prodStatusDTO.getStoreProdIdList());
            // 搜图服务同步
            prodStatusDTO.getStoreProdIdList().forEach(spId -> sync2ImgSearchServer(spId, ListUtil.empty()));
        }
        // 已下架的商品重新上架
        if (CollectionUtils.isNotEmpty(reSaleList)) {
            this.reSaleCreateESDoc(reSaleList);
            // 搜图服务同步
            Map<Long, List<String>> picKeyGroupMap = storeProdFileMapper.selectMainPicByStoreProdIdList(
                    reSaleList.stream().map(StoreProduct::getId).collect(Collectors.toList()),
                    FileType.MAIN_PIC.getValue(), null
            ).stream().collect(Collectors.groupingBy(StoreProdMainPicDTO::getStoreProdId,
                    Collectors.mapping(StoreProdMainPicDTO::getFileUrl, Collectors.toList())));
            prodStatusDTO.getStoreProdIdList()
                    .forEach(spId -> sync2ImgSearchServer(spId, picKeyGroupMap.get(spId), true));
        }
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
    public List<StoreProdFuzzyResDTO> fuzzyQueryList(Long storeId, String prodArtNum) {
        // 初始化查询条件，确保查询的是指定商店且未删除的产品
        LambdaQueryWrapper<StoreProduct> queryWrapper = new LambdaQueryWrapper<StoreProduct>()
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
        Map<Long, List<StoreProdFuzzyResDTO.StoreProdFuzzyColorResDTO>> colorMap = CollectionUtils.isEmpty(colorList) ? new HashMap<>()
                : colorList.stream().collect(Collectors.groupingBy(StoreProductColor::getStoreProdId, Collectors
                .collectingAndThen(Collectors.toList(), list -> list.stream().map(y -> BeanUtil.toBean(y, StoreProdFuzzyResDTO.StoreProdFuzzyColorResDTO.class))
                        .collect(Collectors.toList()))));
        // 将产品列表转换为所需的产品DTO列表，并关联颜色信息
        return storeProdList.stream().map(x -> BeanUtil.toBean(x, StoreProdFuzzyResDTO.class).setStoreProdId(x.getId())
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
        StoreProduct storeProd = Optional.ofNullable(this.storeProdMapper.selectOne(new LambdaQueryWrapper<StoreProduct>()
                        .eq(StoreProduct::getId, storeProdId).eq(StoreProduct::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口商品不存在!", HttpStatus.ERROR));
        StoreProdAppResDTO appResDTO = BeanUtil.toBean(storeProd, StoreProdAppResDTO.class).setStoreProdId(storeProd.getId());
        // 档口文件（商品主图、主图视频、下载的商品详情）
        List<StoreProdFileResDTO> fileResList = this.storeProdFileMapper.selectListByStoreProdId(storeProdId);
        // 档口类目属性
        StoreProductCategoryAttribute cateAttr = this.storeProdCateAttrMapper.selectByStoreProdId(storeProdId);
        // 档口当前商品颜色列表
        List<StoreProdColorDTO> colorList = this.storeProdColorMapper.selectListByStoreProdId(storeProdId);
        // 档口商品颜色尺码列表
        List<StoreProdColorSizeDTO> sizeList = this.storeProdColorSizeMapper.selectListByStoreProdId(storeProdId);
        // 档口颜色价格列表
        List<StoreProdColorPriceSimpleDTO> priceList = this.storeProdColorPriceMapper.selectListByStoreProdId(storeProdId);
        // 档口商品详情
        StoreProductDetail prodDetail = this.storeProdDetailMapper.selectByStoreProdId(storeProdId);
        // 档口服务承诺
        StoreProductService storeProductSvc = this.storeProdSvcMapper.selectByStoreProdId(storeProdId);
        // 是否已收藏
        UserFavorites favorite = this.userFavMapper.selectOne(new LambdaQueryWrapper<UserFavorites>()
                .eq(UserFavorites::getDelFlag, Constants.UNDELETED).eq(UserFavorites::getStoreProdId, storeProdId)
                .eq(UserFavorites::getUserId, SecurityUtils.getUserId()));
        // 获取商品标签
        List<DailyProdTag> tagList = this.prodTagMapper.selectList(new LambdaQueryWrapper<DailyProdTag>()
                .eq(DailyProdTag::getDelFlag, Constants.UNDELETED).eq(DailyProdTag::getStoreProdId, storeProdId)
                .orderByAsc(DailyProdTag::getType));
        return appResDTO.setFileList(fileResList).setCateAttr(BeanUtil.toBean(cateAttr, StoreProdCateAttrDTO.class))
                .setTagList(CollectionUtils.isNotEmpty(tagList) ? tagList.stream().map(DailyProdTag::getTag).distinct().collect(Collectors.toList()) : null)
                .setCollectProd(ObjectUtils.isNotEmpty(favorite) ? Boolean.TRUE : Boolean.FALSE)
                .setSpecification(colorList.size() + "色" + sizeList.stream().filter(x -> Objects.equals(x.getStandard(), ProductSizeStatus.STANDARD.getValue())).count() + "码")
                .setMinPrice(priceList.stream().min(Comparator.comparing(StoreProdColorPriceSimpleDTO::getPrice))
                        .orElseThrow(() -> new ServiceException("获取商品价格失败，请联系客服!", HttpStatus.ERROR)).getPrice())
                .setDetail(ObjectUtils.isEmpty(prodDetail) ? null : prodDetail.getDetail())
                .setSvc(BeanUtil.toBean(storeProductSvc, StoreProdSvcDTO.class));
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
        // 档口商品的sku列表
        List<StoreProdSkuDTO> prodSkuList = this.storeProdMapper.selectSkuList(storeProdId);
        if (CollectionUtils.isEmpty(prodSkuList)) {
            return BeanUtil.toBean(storeProd, StoreProdSkuResDTO.class);
        }
        // 获取当前档口商品的sku列表
        Map<String, Integer> colorSizeStockMap = this.getProdStockList(storeProdId);
        // 组装所有的档口商品sku
        List<StoreProdSkuResDTO.SPColorDTO> colorList = prodSkuList.stream().map(x -> BeanUtil.toBean(x, StoreProdSkuResDTO.SPColorDTO.class)).distinct().collect(Collectors.toList());
        // storeColorId的sku列表
        Map<Long, List<StoreProdSkuDTO>> colorSizeMap = prodSkuList.stream().collect(Collectors.groupingBy(StoreProdSkuDTO::getStoreColorId));
        colorList.forEach(color -> {
            List<StoreProdSkuDTO> sizeList = Optional.ofNullable(colorSizeMap.get(color.getStoreColorId()))
                    .orElseThrow(() -> new ServiceException("获取商品sku失败，请联系客服!", HttpStatus.ERROR));
            color.setSizeStockList(sizeList.stream().map(size -> new StoreProdSkuResDTO.SPSizeStockDTO()
                            .setStoreProdColorSizeId(size.getStoreProdColorSizeId()).setSize(size.getSize()).setStandard(size.getStandard())
                            .setStock(colorSizeStockMap.get(color.getStoreColorId() + ":" + size.getSize())))
                    .collect(Collectors.toList()));
        });
        return BeanUtil.toBean(storeProd, StoreProdSkuResDTO.class).setStoreProdId(storeProdId).setColorList(colorList);
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
        // 档口颜色价格列表
        List<StoreProductColorPrice> priceList = storeProdDTO.getPriceList().stream().map(x -> BeanUtil.toBean(x, StoreProductColorPrice.class)
                .setStoreProdId(storeProd.getId())).collect(Collectors.toList());
        this.storeProdColorPriceMapper.insert(priceList);
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
        // 处理档口所有颜色列表
        List<StoreColor> storeColorList = this.storeColorMapper.selectList(new LambdaQueryWrapper<StoreColor>()
                .eq(StoreColor::getStoreId, storeProdDTO.getStoreId()).eq(StoreColor::getDelFlag, Constants.UNDELETED));
        List<Long> dbStoreColorIdList = storeColorList.stream().map(StoreColor::getId).collect(Collectors.toList());
        // 新增的颜色列表
        List<StoreColor> addColorList = storeProdDTO.getAllColorList().stream().filter(x -> !dbStoreColorIdList.contains(x.getStoreColorId()))
                .map(x -> BeanUtil.toBean(x, StoreColor.class).setStoreId(storeProdDTO.getStoreId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(addColorList)) {
            this.storeColorMapper.insert(addColorList);
        }
    }

    /**
     * 向ES索引新增文档
     *
     * @param storeProd    档口产品
     * @param storeProdDTO 档口产品新增入参
     * @throws IOException
     */
    private void createESDoc(StoreProduct storeProd, StoreProdDTO storeProdDTO) throws IOException {
        ESProductDTO esProductDTO = this.getESDTO(storeProd, storeProdDTO);
        // 向索引中添加数据
        CreateResponse createResponse = esClientWrapper.getEsClient().create(e -> e.index(Constants.ES_IDX_PRODUCT_INFO)
                .id(storeProd.getId().toString()).document(esProductDTO));
        System.out.println("createResponse.result() = " + createResponse.result());
    }

    /**
     * 向ES索引更新文档
     *
     * @param storeProd    档口商品
     * @param storeProdDTO 档口商品更新入参
     * @throws IOException
     */
    private void updateESDoc(StoreProduct storeProd, StoreProdDTO storeProdDTO) throws IOException {
        ESProductDTO esProductDTO = this.getESDTO(storeProd, storeProdDTO);
        UpdateResponse<ESProductDTO> updateResponse = esClientWrapper.getEsClient().update(u -> u
                .index(Constants.ES_IDX_PRODUCT_INFO).doc(esProductDTO).id(storeProd.getId().toString()), ESProductDTO.class);
        System.out.println("deleteResponse.result() = " + updateResponse.result());
    }

    /**
     * 组装ES 入参 DTO
     *
     * @param storeProd    档口商品
     * @param storeProdDTO 档口商品更新入参
     * @return
     */
    private ESProductDTO getESDTO(StoreProduct storeProd, StoreProdDTO storeProdDTO) {
        // 获取第一张主图
        String firstMainPic = storeProdDTO.getFileList().stream().filter(x -> Objects.equals(x.getFileType(), FileType.MAIN_PIC.getValue()))
                .min(Comparator.comparing(StoreProdFileDTO::getOrderNum)).map(StoreProdFileDTO::getFileUrl)
                .orElseThrow(() -> new ServiceException("商品主图不存在!", HttpStatus.ERROR));
        // 获取上一级分类的分类ID 及 分类名称
        ProdCateDTO parCate = this.prodCateMapper.getParentCate(storeProdDTO.getProdCateId());
        // 获取当前商品的最低价格
        BigDecimal minPrice = storeProdDTO.getPriceList().stream().min(Comparator.comparing(StoreProdColorPriceSimpleDTO::getPrice))
                .map(StoreProdColorPriceSimpleDTO::getPrice).orElseThrow(() -> new ServiceException("商品价格不存在!", HttpStatus.ERROR));
        // 获取使用季节
        String season = storeProdDTO.getCateAttr().getSuitableSeason();
        // 获取风格
        String style = storeProdDTO.getCateAttr().getStyle();
        return BeanUtil.toBean(storeProd, ESProductDTO.class)
                .setProdCateName(storeProdDTO.getProdCateName()).setSaleWeight("0").setRecommendWeight("0").setPopularityWeight("0")
                .setCreateTime(DateUtils.getTime()).setStoreName(storeProdDTO.getStoreName()).setMainPic(firstMainPic)
                .setParCateId(parCate.getProdCateId().toString()).setParCateName(parCate.getName()).setProdPrice(minPrice.toString())
                .setSeason(season).setStyle(style).setTags(Collections.singletonList(style));
    }

    /**
     * 删除ES索引文档
     *
     * @param storeProdIdList 文档ID列表
     * @throws IOException
     */
    private void deleteESDoc(List<Long> storeProdIdList) throws IOException {
        List<BulkOperation> list = storeProdIdList.stream().map(x -> new BulkOperation.Builder().delete(
                d -> d.id(String.valueOf(x)).index(Constants.ES_IDX_PRODUCT_INFO)).build()).collect(Collectors.toList());
        // 调用bulk方法执行批量插入操作
        BulkResponse bulkResponse = esClientWrapper.getEsClient().bulk(e -> e.index(Constants.ES_IDX_PRODUCT_INFO).operations(list));
        System.out.println("bulkResponse.items() = " + bulkResponse.items());
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
            ESProductDTO esProductDTO = BeanUtil.toBean(x, ESProductDTO.class).setProdCateName(esDTO.getProdCateName()).setMainPic(esDTO.getMainPic())
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
     * 给设置了所有商品优惠的客户创建优惠
     *
     * @param colorList 档口商品颜色列表
     * @param storeProd 档口商品
     */
    private void createStoreCusDiscount(List<StoreProductColor> colorList, StoreProduct storeProd) {
        if (CollectionUtils.isEmpty(colorList)) {
            return;
        }
        // 档口给那些客户设置了所有商品优惠
        List<StoreCustomer> existCusDiscList = this.storeCusMapper.selectList(new LambdaQueryWrapper<StoreCustomer>()
                .isNotNull(StoreCustomer::getAllProdDiscount).eq(StoreCustomer::getDelFlag, UNDELETED));
        if (CollectionUtils.isNotEmpty(existCusDiscList)) {
            List<StoreCustomerProductDiscount> cusDiscList = new ArrayList<>();
            // 为这些客户绑定商品优惠
            colorList.forEach(color -> existCusDiscList.forEach(storeCus -> cusDiscList.add(new StoreCustomerProductDiscount()
                    .setDiscount(storeCus.getAllProdDiscount()).setStoreId(storeCus.getStoreId()).setStoreProdId(storeProd.getId())
                    .setStoreCusId(storeCus.getId()).setStoreCusName(storeCus.getCusName()).setStoreProdColorId(color.getId()))));
            this.storeCusProdDiscMapper.insert(cusDiscList);
        }
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

}
