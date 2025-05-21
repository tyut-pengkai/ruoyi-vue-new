package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.picture.ProductMatchDTO;
import com.ruoyi.xkt.dto.picture.SearchRequestDTO;

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
     * @return
     */
    List<ProductMatchDTO> searchProductByPic(SearchRequestDTO requestDTO);

}
