package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.dto.storeColor.StoreColorDTO;
import com.ruoyi.xkt.dto.storeProdCateAttr.StoreProdCateAttrDTO;
import com.ruoyi.xkt.dto.storeProdColor.StoreProdColorDTO;
import com.ruoyi.xkt.dto.storeProdColorPrice.StoreProdColorPriceDTO;
import com.ruoyi.xkt.dto.storeProdDetail.StoreProdDetailDTO;
import com.ruoyi.xkt.dto.storeProdProcess.StoreProdProcessDTO;
import com.ruoyi.xkt.dto.storeProdSvc.StoreProdSvcDTO;
import com.ruoyi.xkt.dto.storeProduct.*;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdFileDTO;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdFileResDTO;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdMainPicDTO;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IStoreProductService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
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
    final StoreProductColorSizeMapper storeProdColorSizeMapper;
    final StoreProductProcessMapper storeProdProcMapper;
    final StoreMapper storeMapper;

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
                        .eq(StoreProduct::getId, storeProdId).eq(StoreProduct::getDelFlag, "0")))
                .orElseThrow(() -> new ServiceException("档口商品不存在!", HttpStatus.ERROR));
        StoreProdResDTO storeProdResDTO = BeanUtil.toBean(storeProd, StoreProdResDTO.class).setStoreProdId(storeProd.getId());
        // 档口文件（商品主图、主图视频、下载的商品详情）
        List<StoreProdFileResDTO> fileResList = this.storeProdFileMapper.selectListByStoreProdId(storeProdId);
        storeProdResDTO.setFileList(CollectionUtils.isEmpty(fileResList) ? new ArrayList<>() : BeanUtil.copyToList(fileResList, StoreProdFileResDTO.class));
        // 档口类目属性列表
        List<StoreProdCateAttrDTO> cateAttrList = this.storeProdCateAttrMapper.selectListByStoreProdId(storeProdId);
        storeProdResDTO.setCateAttrList(CollectionUtils.isEmpty(cateAttrList) ? new ArrayList<>() : BeanUtil.copyToList(cateAttrList, StoreProdCateAttrDTO.class));
        // 档口所有颜色列表
        List<StoreColorDTO> allColorList = this.storeColorMapper.selectListByStoreProdId(storeProd.getStoreId());
        storeProdResDTO.setAllColorList(CollectionUtils.isEmpty(allColorList) ? new ArrayList<>() : BeanUtil.copyToList(allColorList, StoreColorDTO.class));
        // 档口当前商品颜色列表
        List<StoreProdColorDTO> colorList = this.storeProdColorMapper.selectListByStoreProdId(storeProdId);
        storeProdResDTO.setColorList(CollectionUtils.isEmpty(colorList) ? new ArrayList<>() : BeanUtil.copyToList(colorList, StoreProdColorDTO.class));
        // 档口颜色价格列表
        List<StoreProdColorPriceDTO> priceList = this.storeProdColorPriceMapper.selectListByStoreProdId(storeProdId);
        storeProdResDTO.setPriceList(CollectionUtils.isEmpty(priceList) ? new ArrayList<>() : BeanUtil.copyToList(priceList, StoreProdColorPriceDTO.class));
        // 档口商品详情
        StoreProductDetail prodDetail = this.storeProdDetailMapper.selectByStoreProdId(storeProdId);
        storeProdResDTO.setDetail(ObjectUtils.isEmpty(prodDetail) ? null : BeanUtil.toBean(prodDetail, StoreProdDetailDTO.class));
        // 档口服务承诺
        StoreProductService storeProductSvc = this.storeProdSvcMapper.selectByStoreProdId(storeProdId);
        storeProdResDTO.setSvc(ObjectUtils.isEmpty(storeProductSvc) ? null : BeanUtil.toBean(storeProductSvc, StoreProdSvcDTO.class));
        // 档口生产工艺信息
        StoreProductProcess prodProcess = this.storeProdProcMapper.selectByStoreProdId(storeProdId);
        storeProdResDTO.setProcess(ObjectUtils.isEmpty(prodProcess) ? null : BeanUtil.toBean(prodProcess, StoreProdProcessDTO.class));
        return storeProdResDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public StoreProdPicSpaceResDTO getStoreProductPicSpace(Long storeId) {
        Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                        .eq(Store::getId, storeId).eq(Store::getDelFlag, "0")))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        return StoreProdPicSpaceResDTO.builder().storeId(storeId).storeName(store.getStoreName())
                .fileList(this.storeProdFileMapper.selectPicSpaceList(storeId, "DOWNLOAD")).build();
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
    public List<StoreProdPageResDTO> selectPage(StoreProdPageDTO pageDTO) {
        // 调用Mapper方法查询商店产品分页信息
        List<StoreProdPageResDTO> page = storeProdColorMapper.selectStoreProdColorPage(pageDTO);
        // 如果查询结果为空，则直接返回空列表
        if (CollectionUtils.isEmpty(page)) {
            return new ArrayList<>();
        }
        // 提取查询结果中的商店产品ID列表
        List<Long> storeProdIdList = page.stream().map(StoreProdPageResDTO::getStoreProdId).collect(Collectors.toList());
        // 查找排名第一个商品主图列表
        List<StoreProdMainPicDTO> mainPicList = this.storeProdFileMapper.selectMainPicByStoreProdIdList(storeProdIdList, "MAIN_PIC", 1);
        Map<Long, String> mainPicMap = CollectionUtils.isEmpty(mainPicList) ? new HashMap<>() : mainPicList.stream()
                .collect(Collectors.toMap(StoreProdMainPicDTO::getStoreProdId, StoreProdMainPicDTO::getFileUrl));
        // 查找档口商品的标准尺码
        LambdaQueryWrapper<StoreProductColorSize> queryWrapper = new LambdaQueryWrapper<StoreProductColorSize>().in(StoreProductColorSize::getStoreProdId, storeProdIdList)
                .eq(StoreProductColorSize::getDelFlag, "0").eq(StoreProductColorSize::getStandard, "1");
        List<StoreProductColorSize> standardSizeList = this.storeProdColorSizeMapper.selectList(queryWrapper);
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
        StoreProduct storeProd = BeanUtil.toBean(storeProdDTO, StoreProduct.class).setVoucherDate(DateUtils.getNowDate());
        int count = this.storeProdMapper.insert(storeProd);
        // 上传的文件列表
        final List<StoreProdFileDTO> fileDTOList = storeProdDTO.getFileList();
        // 将文件插入到SysFile表中
        List<SysFile> fileList = BeanUtil.copyToList(fileDTOList, SysFile.class);
        this.fileMapper.insert(fileList);
        // 将文件名称和文件ID映射到Map中
        Map<String, Long> fileMap = fileList.stream().collect(Collectors.toMap(SysFile::getFileName, SysFile::getId));
        // 档口文件（商品主图、主图视频、下载的商品详情）
        List<StoreProductFile> prodFileList = fileDTOList.stream()
                .map(x -> BeanUtil.toBean(x, StoreProductFile.class).setFileId(fileMap.get(x.getFileName()))
                        .setStoreProdId(storeProd.getId()).setStoreId(storeProdDTO.getStoreId()))
                .collect(Collectors.toList());
        this.storeProdFileMapper.insert(prodFileList);
        // 档口类目属性列表
        List<StoreProductCategoryAttribute> cateAttrList = storeProdDTO.getCateAttrList().stream()
                .map(x -> BeanUtil.toBean(x, StoreProductCategoryAttribute.class)
                        .setStoreProdId(storeProd.getId()))
                .collect(Collectors.toList());
        this.storeProdCateAttrMapper.insert(cateAttrList);
        // 档口颜色列表
        List<StoreProductColor> colorList = storeProdDTO.getColorList().stream()
                .map(x -> BeanUtil.toBean(x, StoreProductColor.class)
                        .setStoreProdId(storeProd.getId()).setStoreId(storeProdDTO.getStoreId()))
                .collect(Collectors.toList());
        this.storeProdColorMapper.insert(colorList);
        // 档口颜色尺码列表
        List<StoreProductColorSize> sizeList = storeProdDTO.getSizeList().stream()
                .map(x -> BeanUtil.toBean(x, StoreProductColorSize.class)
                        .setStoreProdId(storeProd.getId()))
                .collect(Collectors.toList());
        this.storeProdColorSizeMapper.insert(sizeList);
        // 档口颜色价格列表
        List<StoreProductColorPrice> priceList = storeProdDTO.getPriceList().stream()
                .map(x -> BeanUtil.toBean(x, StoreProductColorPrice.class)
                        .setStoreProdId(storeProd.getId()))
                .collect(Collectors.toList());
        this.storeProdColorPriceMapper.insert(priceList);
        // 档口详情内容
        StoreProductDetail storeProdDetail = BeanUtil.toBean(storeProdDTO.getDetail(), StoreProductDetail.class)
                .setStoreProdId(storeProd.getId());
        this.storeProdDetailMapper.insert(storeProdDetail);
        // 档口服务承诺
        if (ObjectUtils.isNotEmpty(storeProdDTO.getSvc())) {
            this.storeProdSvcMapper.insert(BeanUtil.toBean(storeProdDTO.getSvc(), StoreProductService.class)
                    .setStoreProdId(storeProd.getId()));
        }
        // 档口生产工艺信息
        if (ObjectUtils.isNotEmpty(storeProdDTO.getProcess())) {
            this.storeProdProcMapper.insert(BeanUtil.toBean(storeProdDTO.getProcess(), StoreProductProcess.class)
                    .setStoreProdId(storeProd.getId()));
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
    public int updateStoreProduct(final Long storeProdId, StoreProdDTO storeProdDTO) {
        StoreProduct storeProd = Optional.ofNullable(this.storeProdMapper.selectOne(new LambdaQueryWrapper<StoreProduct>()
                        .eq(StoreProduct::getId, storeProdId).eq(StoreProduct::getDelFlag, "0")))
                .orElseThrow(() -> new ServiceException("档口商品不存在!", HttpStatus.ERROR));
        // 将档口商品的del_flag置为2
        storeProd.setDelFlag("2");
        this.storeProdMapper.updateById(storeProd);
        // 档口文件（商品主图、主图视频、下载的商品详情）的del_flag置为2
        this.storeProdFileMapper.updateDelFlagByStoreProdId(storeProdId);
        // 档口类目属性列表的 del_flag置为2
        this.storeProdCateAttrMapper.updateDelFlagByStoreProdId(storeProdId);
        // 档口颜色列表的del_flag置为2
        this.storeProdColorMapper.updateDelFlagByStoreProdId(storeProdId);
        // 档口颜色尺码列表的del_flag置为2
        this.storeProdColorSizeMapper.updateDelFlagByStoreProdId(storeProdId);
        // 档口颜色价格列表的del_flag置为2
        this.storeProdColorPriceMapper.updateDelFlagByStoreProdId(storeProdId);
        // 档口详情内容的del_flag置为2
        this.storeProdDetailMapper.updateDelFlagByStoreProdId(storeProdId);
        // 档口服务承诺的del_flag置为2
        this.storeProdSvcMapper.updateDelFlagByStoreProdId(storeProdId);
        // 档口工艺信息的del_flag置为2
        this.storeProdProcMapper.updateDelFlagByStoreProdId(storeProdId);
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
    @Transactional
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
     * @param storeId 商店ID，用于查询特定商店的产品
     * @param prodArtNum 产品货号，用于模糊匹配产品
     * @return 返回一个包含模糊查询结果的列表，每个元素包含产品信息和颜色信息
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreProdFuzzyResDTO> fuzzyQueryList(Long storeId, String prodArtNum) {
        // 初始化查询条件，确保查询的是指定商店且未删除的产品
        LambdaQueryWrapper<StoreProduct> queryWrapper = new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getStoreId, storeId).eq(StoreProduct::getDelFlag, "0");
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
                .in(StoreProductColor::getStoreProdId, storeProdIdList).eq(StoreProductColor::getDelFlag, "0"));
        // 将颜色信息按产品ID分组，并转换为所需的颜色DTO列表
        Map<Long, List<StoreProdFuzzyResDTO.StoreProdFuzzyColorResDTO>> colorMap = CollectionUtils.isEmpty(colorList) ? new HashMap<>()
                : colorList.stream().collect(Collectors.groupingBy(StoreProductColor::getStoreProdId, Collectors
                .collectingAndThen(Collectors.toList(), list -> list.stream().map(y -> BeanUtil.toBean(y, StoreProdFuzzyResDTO.StoreProdFuzzyColorResDTO.class))
                        .collect(Collectors.toList()))));
        // 将产品列表转换为所需的产品DTO列表，并关联颜色信息
        return storeProdList.stream().map(x -> BeanUtil.toBean(x, StoreProdFuzzyResDTO.class).setStoreProdId(x.getId())
                .setColorList(colorMap.getOrDefault(x.getId(), new ArrayList<>()))).collect(Collectors.toList());
    }

}
