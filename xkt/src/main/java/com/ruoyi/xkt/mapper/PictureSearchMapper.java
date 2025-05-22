package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.PictureSearch;
import com.ruoyi.xkt.dto.picture.ProductImgSearchCountDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 以图搜款Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Repository
public interface PictureSearchMapper extends BaseMapper<PictureSearch> {

    List<ProductImgSearchCountDTO> listProductImgSearchCount(@Param("beginDate") Date beginDate,
                                                             @Param("endDate") Date endDate);
}
