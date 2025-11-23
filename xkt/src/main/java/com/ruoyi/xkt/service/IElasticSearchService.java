package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.es.ESProductDTO;
import com.ruoyi.xkt.dto.website.IndexSearchDTO;

import java.io.IOException;

/**
 * 档口商品Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IElasticSearchService {

    /**
     * 批量往ES新增商品数据
     *
     * @return Integer
     */
    Integer batchCreate();

    /**
     * 网站首页搜索
     *
     * @param searchDTO 查询入参
     */
    Page<ESProductDTO> search(IndexSearchDTO searchDTO) throws IOException;

}
