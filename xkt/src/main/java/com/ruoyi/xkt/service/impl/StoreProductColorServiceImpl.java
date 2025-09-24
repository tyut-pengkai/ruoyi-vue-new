package com.ruoyi.xkt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.xkt.domain.StoreProduct;
import com.ruoyi.xkt.domain.StoreProductColor;
import com.ruoyi.xkt.domain.StoreProductColorSize;
import com.ruoyi.xkt.dto.storeProdColor.StoreProdColorResDTO;
import com.ruoyi.xkt.dto.storeProdColor.StoreProdColorSnResDTO;
import com.ruoyi.xkt.dto.storeProdColorPrice.StoreProdColorPricePageDTO;
import com.ruoyi.xkt.dto.storeProdColorPrice.StoreProdColorPriceResDTO;
import com.ruoyi.xkt.mapper.StoreProductColorMapper;
import com.ruoyi.xkt.mapper.StoreProductColorSizeMapper;
import com.ruoyi.xkt.mapper.StoreProductMapper;
import com.ruoyi.xkt.service.IStoreProductColorService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 档口当前商品颜色Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class StoreProductColorServiceImpl implements IStoreProductColorService {

    final StoreProductColorMapper storeProdColorMapper;
    final StoreProductMapper storeProdMapper;
    final StoreProductColorSizeMapper prodColorSizeMapper;

    /**
     * 根据商店ID和产品款式编号模糊查询颜色列表
     *
     * @param storeId    商店ID，用于限定查询范围
     * @param prodArtNum 产品款式编号，用于模糊匹配产品
     * @return 返回一个列表，包含匹配的产品颜色信息
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreProdColorResDTO> fuzzyQueryColorList(Long storeId, String prodArtNum) {
        return storeProdColorMapper.fuzzyQueryColorList(storeId, prodArtNum);
    }

    /**
     * 获取档口商品所有颜色及价格等
     *
     * @param storeId     档口ID
     * @param storeProdId 档口商品ID
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreProdColorPriceResDTO> getColorPriceByStoreProdId(Long storeId, Long storeProdId) {
        return this.storeProdColorMapper.selectListByStoreProdIdAndStoreId(storeProdId, storeId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StoreProdColorPriceResDTO> page(StoreProdColorPricePageDTO pageDTO) {
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<StoreProdColorPriceResDTO> list = this.storeProdColorMapper.selectColorPricePage(pageDTO);
        return CollectionUtils.isEmpty(list) ? Page.empty(pageDTO.getPageSize(), pageDTO.getPageNum()) : Page.convert(new PageInfo<>(list));
    }

    /**
     * 获取商品颜色及已设置颜色条码
     *
     * @param storeId     档口ID
     * @param storeProdId 档口商品ID
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreProdColorSnResDTO> queryColorAndSetSnList(Long storeId, Long storeProdId) {
        StoreProduct storeProd = Optional.ofNullable(this.storeProdMapper.selectById(storeProdId))
                .orElseThrow(() -> new ServiceException("该商品不存在", HttpStatus.ERROR));
        List<StoreProductColor> prodColorList = this.storeProdColorMapper.selectList(new LambdaQueryWrapper<StoreProductColor>()
                .eq(StoreProductColor::getStoreProdId, storeProdId).eq(StoreProductColor::getStoreId, storeId)
                .eq(StoreProductColor::getDelFlag, Constants.UNDELETED));
        // 已设置条码的颜色
        List<StoreProductColorSize> prodColorSizeList = this.prodColorSizeMapper.selectList(new LambdaQueryWrapper<StoreProductColorSize>()
                .eq(StoreProductColorSize::getStoreProdId, storeProdId).eq(StoreProductColorSize::getDelFlag, Constants.UNDELETED)
                .isNotNull(StoreProductColorSize::getOtherSnPrefix));
        // 颜色ID对应的其他系统商品颜色条码
        Map<Long, String> prodColorOtherSnMap = prodColorSizeList.stream().collect(Collectors
                .toMap(StoreProductColorSize::getStoreColorId, StoreProductColorSize::getOtherSnPrefix, (v1, v2) -> v2));
        return prodColorList.stream().map(x -> new StoreProdColorSnResDTO().setStoreProdColorId(x.getId()).setStoreId(storeId).setStoreProdId(storeProdId)
                        .setStoreColorId(x.getStoreColorId()).setProdArtNum(storeProd.getProdArtNum()).setColorName(x.getColorName())
                        .setOrderNum(x.getOrderNum()).setSn(prodColorOtherSnMap.getOrDefault(x.getStoreColorId(), "")))
                .collect(Collectors.toList());
    }


}
