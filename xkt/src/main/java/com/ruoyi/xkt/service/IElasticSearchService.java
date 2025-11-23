package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.elasticSearch.EsProdBatchCreateDTO;
import com.ruoyi.xkt.dto.elasticSearch.EsProdBatchDeleteDTO;
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
     * @param storeId 档口ID
     * @return Integer
     */
    void batchCreate(Long storeId);

    /**
     * 批量删除商品数据
     *
     * @param storeId 档口ID
     */
    void batchDelete(Long storeId);

    /**
     * 网站首页搜索
     *
     * @param searchDTO 查询入参
     */
    Page<ESProductDTO> search(IndexSearchDTO searchDTO) throws IOException;

    /**
     * 批量新增商品数据
     *
     * @param createProdDTO 新增商品入参
     */
    void batchCreateProd(EsProdBatchCreateDTO createProdDTO);

    /**
     * 批量删除商品数据
     *
     * @param deleteDTO 删除商品入参
     */
    void batchDeleteProd(EsProdBatchDeleteDTO deleteDTO);
}
