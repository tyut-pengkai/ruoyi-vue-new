package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.picture.SearchRequestDTO;
import com.ruoyi.xkt.dto.picture.TopProductMatchDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdViewDTO;

import java.util.List;

/**
 * 以图搜款Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IPictureSearchService {
    /**
     * 图片搜索商品
     *
     * @param requestDTO
     * @return List<StoreProdViewDTO>
     */
    List<StoreProdViewDTO> searchProductByPic(SearchRequestDTO requestDTO);

    /**
     * 图搜热款列表
     *
     * @return List<TopProductMatchDTO>
     */
    List<StoreProdViewDTO> listImgSearchTopProduct();

    /**
     * 缓存图搜热款
     */
    List<StoreProdViewDTO> cacheImgSearchTopProduct();
}
