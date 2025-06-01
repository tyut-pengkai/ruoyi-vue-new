package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreHomepage;
import com.ruoyi.xkt.dto.storeHomepage.StoreHomeTemplateOneResDTO;
import com.ruoyi.xkt.dto.storeHomepage.StoreHomeTopBannerResDTO;
import com.ruoyi.xkt.dto.storeHomepage.StoreRecommendResDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 档口首页Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreHomepageMapper extends BaseMapper<StoreHomepage> {

    /**
     * 获取档口首页顶部轮播图
     *
     * @param storeId 档口ID
     * @return List<StoreHomeTemplateOneResDTO.SHTOTopBannerDTO>
     */
    List<StoreHomeTopBannerResDTO> selectTopLeftList(Long storeId);

}
