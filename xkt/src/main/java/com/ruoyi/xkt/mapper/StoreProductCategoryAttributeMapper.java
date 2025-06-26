package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProductCategoryAttribute;
import com.ruoyi.xkt.dto.storeProdCateAttr.StoreProdCateAttrDTO;

import java.util.List;

/**
 * 档口商品类目信息Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreProductCategoryAttributeMapper extends BaseMapper<StoreProductCategoryAttribute> {

    void updateDelFlagByStoreProdId(Long storeProdId);

    StoreProductCategoryAttribute selectByStoreProdId(Long storeProdId);

}
