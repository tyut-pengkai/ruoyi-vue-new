package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreColor;
import com.ruoyi.xkt.dto.storeColor.StoreColorDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 档口所有颜色Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Repository
public interface StoreColorMapper extends BaseMapper<StoreColor> {

    List<StoreColorDTO> selectListByStoreProdId(Long storeId);

}
