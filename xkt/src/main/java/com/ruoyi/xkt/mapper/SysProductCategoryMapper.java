package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.dto.productCategory.ProdCateDTO;
import com.ruoyi.xkt.domain.SysProductCategory;

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
