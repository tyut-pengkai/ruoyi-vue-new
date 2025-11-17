package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.xkt.domain.StoreProductDemandTemplate;
import com.ruoyi.xkt.dto.storeProductDemandTemplate.StoreDemandTemplateResDTO;
import com.ruoyi.xkt.dto.storeProductDemandTemplate.StoreDemandTemplateUpdateDTO;
import com.ruoyi.xkt.mapper.StoreProductDemandTemplateMapper;
import com.ruoyi.xkt.service.IStoreProductDemandTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.ruoyi.common.constant.Constants.SELECTED;
import static com.ruoyi.common.constant.Constants.UNSELECTED;

/**
 * 档口需求下载模板Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class StoreProductDemandTemplateServiceImpl implements IStoreProductDemandTemplateService {

    final StoreProductDemandTemplateMapper templateMapper;


    /**
     * 获取当前档口设置的模板
     *
     * @param storeId 档口ID
     * @return StoreDemandTemplateResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreDemandTemplateResDTO getTemplate(Long storeId) {
        StoreProductDemandTemplate template = this.templateMapper.selectOne(new LambdaQueryWrapper<StoreProductDemandTemplate>()
                .eq(StoreProductDemandTemplate::getStoreId, storeId).eq(StoreProductDemandTemplate::getDelFlag, Constants.UNDELETED));
        return BeanUtil.toBean(template, StoreDemandTemplateResDTO.class);
    }

    /**
     * 初始化模板
     *
     * @param storeId 档口ID
     * @return 影响行数
     */
    @Override
    @Transactional
    public Integer initTemplate(Long storeId) {
        StoreProductDemandTemplate template = Optional.ofNullable(this.templateMapper.selectOne(new LambdaQueryWrapper<StoreProductDemandTemplate>()
                        .eq(StoreProductDemandTemplate::getStoreId, storeId).eq(StoreProductDemandTemplate::getDelFlag, Constants.UNDELETED)))
                .orElse(new StoreProductDemandTemplate());
        template.setStoreId(storeId);
        // 设置初始化值为未选中
        template.setSelectSize30(UNSELECTED).setSelectSize31(UNSELECTED).setSelectSize32(UNSELECTED).setSelectSize33(UNSELECTED)
                .setSelectSize41(UNSELECTED).setSelectSize42(UNSELECTED).setSelectSize43(UNSELECTED)
                .setSelectPartnerName(UNSELECTED).setSelectTrademark(UNSELECTED).setSelectShoeType(UNSELECTED)
                .setSelectShoeSize(UNSELECTED).setSelectMainSkin(UNSELECTED).setSelectMainSkinUsage(UNSELECTED)
                .setSelectMatchSkin(UNSELECTED).setSelectMatchSkinUsage(UNSELECTED).setSelectNeckline(UNSELECTED)
                .setSelectInsole(UNSELECTED).setSelectFastener(UNSELECTED).setSelectShoeAccessories(UNSELECTED)
                .setSelectToeCap(UNSELECTED).setSelectEdgeBinding(UNSELECTED).setSelectMidOutsole(UNSELECTED)
                .setSelectPlatformSole(UNSELECTED).setSelectMidsoleFactoryCode(UNSELECTED)
                .setSelectOutsoleFactoryCode(UNSELECTED).setSelectHeelFactoryCode(UNSELECTED)
                .setSelectComponents(UNSELECTED).setSelectSecondSoleMaterial(UNSELECTED)
                .setSelectSecondUpperMaterial(UNSELECTED).setSelectDemandCode(UNSELECTED);
        // 设置初始化值
        template.setSelectSize34(SELECTED).setSelectSize35(SELECTED).setSelectSize36(SELECTED).setSelectSize37(SELECTED).setSelectSize38(SELECTED)
                .setSelectSize39(SELECTED).setSelectSize40(SELECTED).setSelectFacName(SELECTED).setSelectMakeTime(SELECTED)
                .setSelectFactoryArtNum(SELECTED).setSelectProdArtNum(SELECTED).setSelectColorName(SELECTED).setSelectShoeUpperLiningMaterial(SELECTED)
                .setSelectShaftMaterial(SELECTED).setSelectDemandStatus(SELECTED).setSelectEmergency(SELECTED).setSelectQuantity(SELECTED);
        this.templateMapper.insertOrUpdate(template);
        return 1;
    }

    /**
     * 更新需求下载模板
     *
     * @param updateDTO 更新入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer updateTemplate(StoreDemandTemplateUpdateDTO updateDTO) {
        StoreProductDemandTemplate template = Optional.ofNullable(this.templateMapper.selectOne(new LambdaQueryWrapper<StoreProductDemandTemplate>()
                        .eq(StoreProductDemandTemplate::getStoreId, updateDTO.getStoreId()).eq(StoreProductDemandTemplate::getDelFlag, Constants.UNDELETED)))
                .orElse(new StoreProductDemandTemplate());
        BeanUtil.copyProperties(updateDTO, template);
        this.templateMapper.insertOrUpdate(template);
        return 1;
    }


}
