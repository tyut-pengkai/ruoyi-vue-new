package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.BeansUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.dto.storeColor.StoreColorDTO;
import com.ruoyi.xkt.dto.storeProdCateAttr.StoreProdCateAttrDTO;
import com.ruoyi.xkt.dto.storeProdColor.StoreProdColorDTO;
import com.ruoyi.xkt.dto.storeProdColorPrice.StoreProdColorPriceDTO;
import com.ruoyi.xkt.dto.storeProdDetail.StoreProdDetailDTO;
import com.ruoyi.xkt.dto.storeProdSvc.StoreProdSvcDTO;
import com.ruoyi.xkt.dto.storeProduct.*;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdFileResDTO;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IStoreProductService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 档口商品Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
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


    /**
     * 查询档口商品
     *
     * @param storeProdId 档口商品主键
     * @return 档口商品
     */
    @Override
    @Transactional(readOnly = true)
    public StoreProdResDTO selectStoreProductByStoreProdId(Long storeProdId) {
        StoreProduct storeProd = Optional.ofNullable( storeProdMapper.selectById(storeProdId)).orElseThrow(() -> new ServiceException("档口商品不存在!", HttpStatus.ERROR));
        StoreProdResDTO storeProdResDTO = BeansUtils.convertObject(storeProd, StoreProdResDTO.class);
        // 档口文件（商品主图、主图视频、下载的商品详情）
        List<StoreProdFileResDTO> fileResList = this.storeProdFileMapper.selectListByStoreProdId(storeProdId);
        storeProdResDTO.setFileList(CollectionUtils.isEmpty(fileResList) ? new ArrayList<>() : BeansUtils.convertList(fileResList, StoreProdFileResDTO.class));
        // 档口类目属性列表
        List<StoreProdCateAttrDTO> cateAttrList = this.storeProdCateAttrMapper.selectListByStoreProdId(storeProdId);
        storeProdResDTO.setCateAttrList(CollectionUtils.isEmpty(cateAttrList) ? new ArrayList<>() : BeansUtils.convertList(cateAttrList, StoreProdCateAttrDTO.class));
        // 档口所有颜色列表
        List<StoreColorDTO> allColorList = this.storeColorMapper.selectListByStoreProdId(storeProdId);
        storeProdResDTO.setAllColorList(CollectionUtils.isEmpty(allColorList) ? new ArrayList<>() : BeansUtils.convertList(allColorList, StoreColorDTO.class));
        // 档口当前商品颜色列表
        List<StoreProdColorDTO> colorList = this.storeProdColorMapper.selectListByStoreProdId(storeProdId);
        storeProdResDTO.setColorList(CollectionUtils.isEmpty(colorList) ? new ArrayList<>() : BeansUtils.convertList(colorList, StoreProdColorDTO.class));
        // 档口颜色价格列表
        List<StoreProdColorPriceDTO> priceList = this.storeProdColorPriceMapper.selectListByStoreProdId(storeProdId);
        storeProdResDTO.setPriceList(CollectionUtils.isEmpty(priceList) ? new ArrayList<>() : BeansUtils.convertList(priceList, StoreProdColorPriceDTO.class));
        // 档口详情
        StoreProductDetail prodDetail = this.storeProdDetailMapper.selectByStoreProdId(storeProdId);
        storeProdResDTO.setDetail(ObjectUtils.isEmpty(prodDetail) ? null : BeansUtils.convertObject(prodDetail, StoreProdDetailDTO.class));
        // 档口服务承诺
        StoreProductService storeProductSvc = this.storeProdSvcMapper.selectByStoreProdId(storeProdId);
        storeProdResDTO.setSvc(ObjectUtils.isEmpty(storeProductSvc) ? null : BeansUtils.convertObject(storeProductSvc, StoreProdSvcDTO.class));
        return storeProdResDTO;
    }

    /**
     * 查询档口商品列表
     *
     * @param storeProduct 档口商品
     * @return 档口商品
     */
    @Override
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
public List<StoreProdPageResDTO> selectPage(StoreProdPageDTO pageDTO) {
    // 调用Mapper方法查询商店产品分页信息
    List<StoreProdPageResDTO> page = storeProdMapper.selectStoreProdPage(pageDTO);
    // 如果查询结果为空，则直接返回空列表
    if (CollectionUtils.isEmpty(page)) {
        return new ArrayList<>();
    }
    // 提取查询结果中的商店产品ID列表
    List<Long> storeProdIdList = page.stream().map(StoreProdPageResDTO::getStoreProdId).collect(Collectors.toList());
    // 查找排名第一个商品主图列表
    Map<Long, String> mainPicMap = this.storeProdFileMapper.selectMainPicByStoreProdIdList(storeProdIdList, "MAIN_PIC", 1);
    // 查找档口商品的标准尺码
    LambdaQueryWrapper queryWrapper =  new LambdaQueryWrapper<StoreProductColorSize>().in(StoreProductColorSize::getStoreProdId, storeProdIdList)
            .eq(StoreProductColorSize::getDelFlag, "0").eq(StoreProductColorSize::getStandard, "1");
    List<StoreProductColorSize> standardSizeList = this.storeProdColorPriceMapper.selectList(queryWrapper);
    // 将标准尺码列表转换为映射，以便后续处理
    Map<Long, List<Integer>> standardSizeMap = CollectionUtils.isEmpty(standardSizeList) ? new HashMap<>() : standardSizeList.stream().collect(Collectors
            .groupingBy(StoreProductColorSize::getStoreProdId, Collectors.mapping(StoreProductColorSize::getSize, Collectors.toList())));
    // 为每个产品设置主图URL和标准尺码列表
    page.forEach(x -> x.setMainPicUrl(mainPicMap.get(x.getStoreProdId())).setStandardSizeList(standardSizeMap.get(x.getStoreProdId())));
    // 打印查询结果
    System.err.println(page);
    return page;
}



    @Override
    @Transactional
    public int insertStoreProduct(StoreProdDTO storeProdDTO) {
        // 组装StoreProduct数据
        StoreProduct storeProd = BeanUtil.toBean(storeProdDTO, StoreProduct.class)
                .setProdStatus("ON_SALE").setVoucherDate(DateUtils.getNowDate());
        int count = this.storeProdMapper.insert(storeProd);
        // 将文件插入到SysFile表中
        List<SysFile> fileList = BeanUtil.copyToList(storeProdDTO.getFileList(), SysFile.class);
        this.fileMapper.insert(fileList);
        // 将文件名称和文件ID映射到Map中
        Map<String, Long> fileMap = fileList.stream().collect(Collectors.toMap(SysFile::getFileName, SysFile::getFileId));
        // 档口文件（商品主图、主图视频、下载的商品详情）
        List<StoreProductFile> prodFileList = fileList.stream()
                .map(x -> BeanUtil.toBean(x, StoreProductFile.class).setFileId(fileMap.get(x.getFileName()))
                        .setStoreProdId(storeProd.getStoreProdId()))
                .collect(Collectors.toList());
        this.storeProdFileMapper.insert(prodFileList);
        // 档口类目属性列表
        List<StoreProductCategoryAttribute> cateAttrList =  storeProdDTO.getCateAttrList().stream()
                .map(x -> BeanUtil.toBean(x, StoreProductCategoryAttribute.class)
                        .setStoreProdId(storeProd.getStoreProdId()))
                .collect(Collectors.toList());
        this.storeProdCateAttrMapper.insert(cateAttrList);
        // 档口颜色列表
        List<StoreProductColor> colorList = storeProdDTO.getColorList().stream()
                .map(x -> BeanUtil.toBean(x, StoreProductColor.class)
                        .setStoreProdId(storeProd.getStoreProdId()))
                .collect(Collectors.toList());
        this.storeProdColorMapper.insert(colorList);
        // 档口颜色价格列表
        List<StoreProductColorPrice> priceList = storeProdDTO.getPriceList().stream()
                .map(x -> BeanUtil.toBean(x, StoreProductColorPrice.class)
                        .setStoreProdId(storeProd.getStoreProdId()))
                .collect(Collectors.toList());
        this.storeProdColorPriceMapper.insert(priceList);
        // 档口详情内容
        StoreProductDetail storeProdDetail = BeanUtil.toBean(storeProdDTO.getDetail(), StoreProductDetail.class)
                .setStoreProdId(storeProd.getStoreProdId());
        this.storeProdDetailMapper.insert(storeProdDetail);
        // 档口服务承诺
        if (ObjectUtils.isNotEmpty(storeProdDTO.getSvc())) {
            this.storeProdSvcMapper.insert(BeanUtil.toBean(storeProdDTO.getSvc(), StoreProductService.class)
                    .setStoreProdId(storeProd.getStoreProdId()));
        }
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
    public int updateStoreProduct(StoreProdDTO storeProdDTO) {
        StoreProduct storeProd = Optional.ofNullable(this.storeProdMapper.selectById(storeProdDTO.getStoreProdId()))
                .orElseThrow(() -> new ServiceException("档口商品不存在!", HttpStatus.ERROR));
        // 将档口商品的del_flag置为2
        storeProd.setDelFlag("2");
        storeProd.setUpdateTime(DateUtils.getNowDate());
        this.storeProdMapper.updateById(storeProd);
        // 档口文件（商品主图、主图视频、下载的商品详情）的del_flag置为2
        this.storeProdFileMapper.updateDelFlagByStoreProdId(storeProdDTO.getStoreProdId());
        // 档口类目属性列表的 del_flag置为2
        this.storeProdCateAttrMapper.updateDelFlagByStoreProdId(storeProdDTO.getStoreProdId());
        // 档口颜色列表的del_flag置为2
        this.storeProdColorMapper.updateDelFlagByStoreProdId(storeProdDTO.getStoreProdId());
        // 档口颜色价格列表的del_flag置为2
        this.storeProdColorPriceMapper.updateDelFlagByStoreProdId(storeProdDTO.getStoreProdId());
        // 档口详情内容的del_flag置为2
        this.storeProdDetailMapper.updateDelFlagByStoreProdId(storeProdDTO.getStoreProdId());
        // 档口服务承诺的del_flag置为2
        this.storeProdSvcMapper.updateDelFlagByStoreProdId(storeProdDTO.getStoreProdId());
        // 重新执行插入数据操作
        return this.insertStoreProduct(storeProdDTO);
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
    public void updateStoreProductStatus(StoreProdStatusDTO prodStatusDTO) {
        // 根据商品ID列表查询数据库中的商品信息
        List<StoreProduct> storeProdList = this.storeProdMapper.selectByIds(prodStatusDTO.getStoreProdIdList());
        // 检查查询结果是否为空，如果为空，则抛出异常
        if (CollectionUtils.isEmpty(storeProdList)) {
            throw new ServiceException("档口商品不存在!", HttpStatus.ERROR);
        }
        // 遍历查询到的商品列表，设置新的商品状态
        storeProdList.forEach(x -> x.setProdStatus(prodStatusDTO.getProdStatus()));
        // 保存更新后的商品列表到数据库
        this.storeProdMapper.updateById(storeProdList);
    }

    /**
     * 批量删除档口商品
     *
     * @param storeProdIds 需要删除的档口商品主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteStoreProductByStoreProdIds(Long[] storeProdIds) {
        return storeProdMapper.deleteStoreProductByStoreProdIds(storeProdIds);
    }

    /**
     * 删除档口商品信息
     *
     * @param storeProdId 档口商品主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteStoreProductByStoreProdId(Long storeProdId) {
        return storeProdMapper.deleteStoreProductByStoreProdId(storeProdId);
    }
}
