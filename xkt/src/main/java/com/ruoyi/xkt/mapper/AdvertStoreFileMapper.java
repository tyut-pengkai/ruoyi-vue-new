package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.AdvertStoreFile;
import com.ruoyi.xkt.dto.advertStoreFile.AdvertStoreFilePageDTO;
import com.ruoyi.xkt.dto.advertStoreFile.AdvertStoreFileResDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 推广营销档口上传的推广图 Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Repository
public interface AdvertStoreFileMapper extends BaseMapper<AdvertStoreFile> {

    /**
     * 查询推广营销图片管理列表
     *
     * @param pageDTO 查询条件
     * @return 推广营销图片管理列表
     */
    List<AdvertStoreFileResDTO> selectFilePage(AdvertStoreFilePageDTO pageDTO);

}
