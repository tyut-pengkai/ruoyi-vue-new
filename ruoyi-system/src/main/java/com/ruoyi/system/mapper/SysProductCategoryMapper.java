package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.core.domain.entity.SysProductCategory;
import com.ruoyi.system.domain.dto.productCategory.ProdCateDTO;

/**
 * 商品分类 数据层
 *
 * @author ruoyi
 */
public interface SysProductCategoryMapper extends BaseMapper<SysProductCategory> {

    /**
     * 获取当前分类的父类
     *
     * @param prodCateId 当前分类
     * @return ProdCateDTO
     */
    ProdCateDTO getParentCate(Long prodCateId);

}
