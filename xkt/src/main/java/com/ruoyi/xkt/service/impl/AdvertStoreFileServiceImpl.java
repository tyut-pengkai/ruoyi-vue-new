package com.ruoyi.xkt.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.AdType;
import com.ruoyi.xkt.dto.advertStoreFile.AdvertStoreFilePageDTO;
import com.ruoyi.xkt.dto.advertStoreFile.AdvertStoreFileResDTO;
import com.ruoyi.xkt.mapper.AdvertStoreFileMapper;
import com.ruoyi.xkt.service.IAdvertStoreFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 推广营销Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class AdvertStoreFileServiceImpl implements IAdvertStoreFileService {

    final AdvertStoreFileMapper advertStoreFileMapper;

    /**
     * 查询推广营销图片管理列表
     *
     * @param pageDTO 查询参数
     * @return 推广营销图片管理列表
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AdvertStoreFileResDTO> page(AdvertStoreFilePageDTO pageDTO) {
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<AdvertStoreFileResDTO> list = this.advertStoreFileMapper.selectFilePage(pageDTO);
        list.forEach(x -> x.setTypeName(AdType.of(x.getTypeId()).getLabel()));
        return Page.convert(new PageInfo<>(list));
    }

}
