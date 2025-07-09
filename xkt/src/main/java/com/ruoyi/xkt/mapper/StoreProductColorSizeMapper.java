package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProductColorSize;
import com.ruoyi.xkt.dto.storeProdColorSize.StoreProdColorSizeDTO;
import com.ruoyi.xkt.dto.storeProdColorSize.StoreProdSizeDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 档口商品颜色的尺码Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Repository
public interface StoreProductColorSizeMapper extends BaseMapper<StoreProductColorSize> {

    void updateDelFlagByStoreProdId(Long storeProdId);

    /**
     * 根据商品ID查询商品尺码列表
     *
     * @param storeProdId 档口商品ID
     * @return List<StoreProdColorSizeDTO>
     */
    List<StoreProdSizeDTO> selectListByStoreProdId(Long storeProdId);

}
