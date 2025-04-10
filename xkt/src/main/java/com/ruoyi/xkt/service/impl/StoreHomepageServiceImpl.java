package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.dto.storeHomepage.StoreHomeDecorationDTO;
import com.ruoyi.xkt.dto.storeHomepage.StoreHomeDecorationResDTO;
import com.ruoyi.xkt.dto.storeHomepage.StoreHomeProdResDTO;
import com.ruoyi.xkt.dto.storeHomepage.StoreHomeResDTO;
import com.ruoyi.xkt.dto.storeProdCateAttr.StoreProdCateAttrDTO;
import com.ruoyi.xkt.dto.storeProdDetail.StoreProdDetailDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdStatusCountDTO;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdFileResDTO;
import com.ruoyi.xkt.enums.FileType;
import com.ruoyi.xkt.enums.HomepageJumpType;
import com.ruoyi.xkt.enums.HomepageType;
import com.ruoyi.xkt.enums.ProductSizeStatus;
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
     * @param storeId     档口ID
     * @param templateNum 使用的模板No
     * @param homepageDTO 新增档口首页各部分图
     * @return Integer
     */
    @Override
    @Transactional
    public Integer insert(Long storeId, Integer templateNum, StoreHomeDecorationDTO homepageDTO) {
        List<StoreHomepage> homepageList = this.insertToHomepage(storeId, homepageDTO);
        // 当前档口首页各部分总的文件大小
        BigDecimal totalSize = homepageDTO.getBigBannerList().stream().map(x -> ObjectUtils.defaultIfNull(x.getFileSize(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>().eq(Store::getId, storeId).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        store.setTemplateNum(templateNum);
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
        Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                        .eq(Store::getId, storeId).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        List<StoreHomepage> homeList = this.storeHomeMapper.selectList(new LambdaQueryWrapper<StoreHomepage>()
                .eq(StoreHomepage::getStoreId, storeId).eq(StoreHomepage::getDelFlag, Constants.UNDELETED));
        List<SysFile> fileList = Optional.ofNullable(this.fileMapper.selectList(new LambdaQueryWrapper<SysFile>()
                        .in(SysFile::getId, homeList.stream().map(StoreHomepage::getFileId).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList()))
                        .eq(SysFile::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("文件不存在", HttpStatus.ERROR));
        Map<Long, SysFile> fileMap = fileList.stream().collect(Collectors.toMap(SysFile::getId, Function.identity()));
        // 所有的档口商品ID
        List<StoreProduct> storeProdList = Optional.ofNullable(this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                        .eq(StoreProduct::getStoreId, storeId).in(StoreProduct::getId, homeList.stream()
                                .filter(x -> Objects.equals(x.getJumpType(), HomepageJumpType.JUMP_PRODUCT.getValue())).map(StoreHomepage::getBizId).collect(Collectors.toList()))
                        .eq(StoreProduct::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口商品不存在", HttpStatus.ERROR));
        Map<Long, StoreProduct> storeProdMap = storeProdList.stream().collect(Collectors.toMap(StoreProduct::getId, Function.identity()));
        // 轮播图
        List<StoreHomeDecorationResDTO.DecorationDTO> bigBannerList = homeList.stream().filter(x -> Objects.equals(x.getFileType(), HomepageType.SLIDING_PICTURE.getValue()))
                .map(x -> {
                    StoreHomeDecorationResDTO.DecorationDTO decorationDTO = BeanUtil.toBean(x, StoreHomeDecorationResDTO.DecorationDTO.class)
                            .setBizName((Objects.equals(x.getJumpType(), HomepageJumpType.JUMP_PRODUCT.getValue()))
                                    ? (storeProdMap.containsKey(x.getBizId()) ? storeProdMap.get(x.getBizId()).getProdName() : null)
                                    : (ObjectUtils.isEmpty(x.getBizId()) ? null : store.getStoreName()));
                    if (fileMap.containsKey(x.getFileId())) {
                        decorationDTO.setFileType(x.getFileType()).setFileName(fileMap.get(x.getFileId()).getFileName())
                                .setFileUrl(fileMap.get(x.getFileId()).getFileUrl()).setFileSize(fileMap.get(x.getFileId()).getFileSize());
                    }
                    return decorationDTO;
                }).collect(Collectors.toList());
        // 其它图部分
        List<StoreHomeDecorationResDTO.DecorationDTO> decorList = homeList.stream().filter(x -> !Objects.equals(x.getFileType(), HomepageType.SLIDING_PICTURE.getValue()))
                .map(x -> {
                    StoreHomeDecorationResDTO.DecorationDTO decorationDTO = BeanUtil.toBean(x, StoreHomeDecorationResDTO.DecorationDTO.class)
                            .setBizName(storeProdMap.containsKey(x.getBizId()) ? storeProdMap.get(x.getBizId()).getProdName() : null);
                    if (fileMap.containsKey(x.getFileId())) {
                        decorationDTO.setFileType(x.getFileType()).setFileName(fileMap.get(x.getFileId()).getFileName())
                                .setFileUrl(fileMap.get(x.getFileId()).getFileUrl()).setFileSize(fileMap.get(x.getFileId()).getFileSize());
                    }
                    return decorationDTO;
                })
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
     * @param storeId     档口ID
     * @param templateNum 选择的模板Num
     * @param homepageDTO 更新的dto
     * @return Integer
     */
    @Override
    @Transactional
    public Integer updateStoreHomepage(Long storeId, Integer templateNum, StoreHomeDecorationDTO homepageDTO) {
        // 先将所有的档口模板的文件都删除掉
        List<StoreHomepage> oldHomeList = this.storeHomeMapper.selectList(new LambdaQueryWrapper<StoreHomepage>()
                .eq(StoreHomepage::getStoreId, storeId).eq(StoreHomepage::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isNotEmpty(oldHomeList)) {
            oldHomeList.forEach(x -> x.setDelFlag(Constants.DELETED));
            this.storeHomeMapper.updateById(oldHomeList);
        }
        // 新增档口首页各个部分的图信息
        List<StoreHomepage> homepageList = this.insertToHomepage(storeId, homepageDTO);
        Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                        .eq(Store::getId, storeId).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        store.setTemplateNum(templateNum);
        this.storeMapper.updateById(store);
        return homepageList.size();
    }

    /**
     * 获取档口首页数据
     *
     * @param storeId 档口ID
     * @return StoreHomeResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreHomeResDTO getHomepageInfo(Long storeId) {
        // 档口基本信息
        Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                        .eq(Store::getId, storeId).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        // 档口各个状态的数量
        StoreProdStatusCountDTO statusCountDTO = this.storeProdMapper.selectStatusCount(storeId);

        // TODO 根据模板Num返回具体的模板数据
        // TODO 根据模板Num返回具体的模板数据
        // TODO 根据模板Num返回具体的模板数据


        return new StoreHomeResDTO() {{
            setStore(BeanUtil.toBean(store, StoreBasicDTO.class).setStoreId(storeId));
            setStoreProdStatusCount(statusCountDTO);
        }};
    }

    /**
     * 首页查询档口商品详情
     *
     * @param storeId     档口ID
     * @param storeProdId 档口商品详情ID
     * @return StoreHomeProdResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreHomeProdResDTO getStoreProdInfo(Long storeId, Long storeProdId) {

        // TODO 获取当前登录者，判断是否关注当前档口是否收藏当前商品
        // TODO 获取当前登录者，判断是否关注当前档口是否收藏当前商品
        // TODO 获取当前登录者，判断是否关注当前档口是否收藏当前商品

        // 档口基本信息
        Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                        .eq(Store::getId, storeId).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        // 档口认证
        StoreCertificate storeCert = this.storeCertMapper.selectOne(new LambdaQueryWrapper<StoreCertificate>()
                .eq(StoreCertificate::getStoreId, storeId).eq(StoreCertificate::getDelFlag, Constants.UNDELETED));
        // 获取商品基本信息
        StoreProduct storeProd = Optional.ofNullable(this.storeProdMapper.selectOne(new LambdaQueryWrapper<StoreProduct>()
                        .eq(StoreProduct::getId, storeProdId).eq(StoreProduct::getDelFlag, Constants.UNDELETED)
                        .eq(StoreProduct::getStoreId, storeId)))
                .orElseThrow(() -> new ServiceException("档口商品不存在!", HttpStatus.ERROR));
        // 档口各个状态的数量
        StoreProdStatusCountDTO statusCountDTO = this.storeProdMapper.selectStatusCount(storeId);
        // 获取商品详情内容
        StoreProductDetail detail = this.prodDetailMapper.selectOne(new LambdaQueryWrapper<StoreProductDetail>()
                .eq(StoreProductDetail::getStoreProdId, storeProdId).eq(StoreProductDetail::getDelFlag, Constants.UNDELETED));
        // 获取商品颜色列表
        List<StoreProductColor> colorList = this.prodColorMapper.selectList(new LambdaQueryWrapper<StoreProductColor>()
                .eq(StoreProductColor::getStoreProdId, storeProdId).eq(StoreProductColor::getDelFlag, Constants.UNDELETED));
        // 档口商品主图列表
        List<StoreProdFileResDTO> mainPicDTOList = this.prodFileMapper.selectTotalMainPicList(storeProdId, storeId, FileType.MAIN_PIC.getValue());
        // 档口商品颜色价格列表
        List<StoreProductColorPrice> colorPriceList = this.prodColorPriceMapper.selectList(new LambdaQueryWrapper<StoreProductColorPrice>()
                .eq(StoreProductColorPrice::getStoreProdId, storeProdId).eq(StoreProductColorPrice::getDelFlag, Constants.UNDELETED));
        Map<String, StoreProductColorPrice> colorPriceMap = colorPriceList.stream().collect(Collectors
                .toMap(x -> x.getStoreProdId().toString() + x.getStoreColorId().toString(), Function.identity()));
        // 档口商品属性
        List<StoreProductCategoryAttribute> prodAttrList = this.prodCateAttrMapper.selectList(new LambdaQueryWrapper<StoreProductCategoryAttribute>()
                .eq(StoreProductCategoryAttribute::getStoreProdId, storeProdId).eq(StoreProductCategoryAttribute::getDelFlag, Constants.UNDELETED));
        // 档口商品标准尺码
        List<StoreProductColorSize> standardSizeList = this.prodColorSizeMapper.selectList(new LambdaQueryWrapper<StoreProductColorSize>()
                .eq(StoreProductColorSize::getStoreProdId, storeProdId).eq(StoreProductColorSize::getDelFlag, Constants.UNDELETED)
                .eq(StoreProductColorSize::getStandard, ProductSizeStatus.STANDARD.getValue()));
        Map<String, List<StoreProductColorSize>> colorSizeMap = standardSizeList.stream().collect(Collectors
                .groupingBy(x -> x.getStoreProdId().toString() + x.getStoreColorId().toString()));
        // 根据标准尺码去找对应尺码的库存数量
        List<StoreProductStock> prodStockList = this.prodStockMapper.selectList(new LambdaQueryWrapper<StoreProductStock>()
                .eq(StoreProductStock::getStoreProdId, storeProdId)
                .in(StoreProductStock::getStoreProdColorId, colorList.stream().map(StoreProductColor::getId).distinct().collect(Collectors.toList()))
                .eq(StoreProductStock::getDelFlag, Constants.UNDELETED));
        // 获取档口颜色尺码的库存数量
        Map<String, List<StoreHomeProdResDTO.StoreProdSizeStockDTO>> colorSizeStockMap = this.convertSizeStock(prodStockList, standardSizeList);
        List<StoreHomeProdResDTO.StoreProdColorDTO> colorSizeStockList = colorList.stream()
                .map(color -> BeanUtil.toBean(color, StoreHomeProdResDTO.StoreProdColorDTO.class)
                        .setStoreProdColorId(color.getId())
                        // 获取颜色设定的价格
                        .setPrice(colorPriceMap.containsKey(color.getStoreProdId().toString() + color.getStoreColorId().toString())
                                ? colorPriceMap.get(color.getStoreProdId().toString() + color.getStoreColorId().toString()).getPrice() : null)
                        // 设定库存
                        .setSizeStockList(colorSizeMap.containsKey(color.getStoreProdId().toString() + color.getStoreColorId().toString())
                                ? colorSizeStockMap.get(color.getStoreProdId().toString() + color.getStoreColorId().toString()) : null))
                .collect(Collectors.toList());
        // 商品基础信息
        StoreHomeProdResDTO.StoreProdInfoDTO storeProdDTO = BeanUtil.toBean(storeProd, StoreHomeProdResDTO.StoreProdInfoDTO.class)
                .setStoreProdId(storeProdId).setMainPicList(mainPicDTOList)
                .setDetail(BeanUtil.toBean(detail, StoreProdDetailDTO.class))
                .setCateAttrList(BeanUtil.copyToList(prodAttrList, StoreProdCateAttrDTO.class))
                .setColorList(colorSizeStockList);
        // 档口推荐图片
        List<StoreHomepage> storeRecommendedList = this.storeHomeMapper.selectList(new LambdaQueryWrapper<StoreHomepage>()
                .eq(StoreHomepage::getStoreId, storeId).eq(StoreHomepage::getDelFlag, Constants.UNDELETED)
                // 查询店铺推荐图片
                .eq(StoreHomepage::getFileType, HomepageType.STORE_RECOMMENDED.getValue()));
        List<Long> fileIdList = storeRecommendedList.stream().map(StoreHomepage::getFileId).filter(Objects::nonNull).collect(Collectors.toList());
        Map<Long, SysFile> fileMap = CollectionUtils.isEmpty(fileIdList) ? new HashMap<>()
                : this.fileMapper.selectList(new LambdaQueryWrapper<SysFile>().in(SysFile::getId, fileIdList).eq(SysFile::getDelFlag, Constants.UNDELETED))
                .stream().collect(Collectors.toMap(SysFile::getId, Function.identity()));
        // 其它图部分
        List<StoreHomeProdResDTO.DecorationVO> recommendedList = storeRecommendedList.stream().map(x -> {
            StoreHomeProdResDTO.DecorationVO decorationDTO = BeanUtil.toBean(x, StoreHomeProdResDTO.DecorationVO.class).setBizName(storeProd.getProdName());
            return fileMap.containsKey(x.getFileId()) ? decorationDTO.setFileType(x.getFileType()).setFileName(fileMap.get(x.getFileId()).getFileName())
                    .setFileUrl(fileMap.get(x.getFileId()).getFileUrl()).setFileSize(fileMap.get(x.getFileId()).getFileSize()) : decorationDTO;
        }).collect(Collectors.toList());
        return new StoreHomeProdResDTO() {{
            setStore(BeanUtil.toBean(store, StoreBasicDTO.class).setStoreId(storeId)
                    .setLicenseName(ObjectUtils.isNotEmpty(storeCert) ? storeCert.getLicenseName() : null));
            setStoreProd(storeProdDTO);
            setStoreProdStatusCount(statusCountDTO);
            setRecommendedList(recommendedList);

            // TODO 还有关注的档口及收藏的商品
            // TODO 还有关注的档口及收藏的商品
            // TODO 还有关注的档口及收藏的商品
            // TODO 还有关注的档口及收藏的商品

        }};
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
     * @param storeId     档口ID
     * @param homepageDTO 新增档口首页入参
     * @return
     */
    private List<StoreHomepage> insertToHomepage(Long storeId, StoreHomeDecorationDTO homepageDTO) {
        // 新增的首页轮播大图部分
        List<SysFile> bigBannerFileList = homepageDTO.getBigBannerList().stream().filter(x -> StringUtils.isNotBlank(x.getFileUrl())
                        && StringUtils.isNotBlank(x.getFileName()) && ObjectUtils.isNotEmpty(x.getFileSize()) && ObjectUtils.isNotEmpty(x.getOrderNum()))
                .map(x -> BeanUtil.toBean(x, SysFile.class)).collect(Collectors.toList());
        List<StoreHomepage> homePageList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(bigBannerFileList)) {
            this.fileMapper.insert(bigBannerFileList);
            Map<String, SysFile> bigBannerMap = bigBannerFileList.stream().collect(Collectors.toMap(SysFile::getFileName, Function.identity()));
            homePageList.addAll(homepageDTO.getBigBannerList().stream().map(x -> BeanUtil.toBean(x, StoreHomepage.class).setStoreId(storeId)
                            .setFileId(bigBannerMap.containsKey(x.getFileName()) ? bigBannerMap.get(x.getFileName()).getId() : null))
                    .collect(Collectors.toList()));
        }
        if (CollectionUtils.isNotEmpty(homepageDTO.getDecorList())) {
            homePageList.addAll(homepageDTO.getDecorList().stream().map(x -> BeanUtil.toBean(x, StoreHomepage.class).setStoreId(storeId))
                    .collect(Collectors.toList()));
        }
        if (CollectionUtils.isNotEmpty(homePageList)) {
            this.storeHomeMapper.insert(homePageList);
        }
        return homePageList;
    }


}
