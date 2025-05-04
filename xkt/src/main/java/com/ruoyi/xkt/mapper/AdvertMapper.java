package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.Advert;
import com.ruoyi.xkt.dto.advert.AdvertPageDTO;
import com.ruoyi.xkt.dto.advert.AdvertResDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 推广营销Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Repository
public interface AdvertMapper extends BaseMapper<Advert> {

    /**
     * 查询推广营销分页列表
     * @param pageDTO 查询入参
     * @return
     */
//    List<AdvertResDTO> selectAdvertPage(AdvertPageDTO pageDTO);

}
