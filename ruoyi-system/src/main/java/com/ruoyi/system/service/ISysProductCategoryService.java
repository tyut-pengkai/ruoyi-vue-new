package com.ruoyi.system.service;

import com.ruoyi.system.domain.dto.productCategory.AppHomeProdCateListResDTO;
import com.ruoyi.system.domain.dto.productCategory.ProdCateDTO;
import com.ruoyi.system.domain.dto.productCategory.ProdCateListDTO;
import com.ruoyi.system.domain.dto.productCategory.ProdCateListResDTO;

import java.util.List;

/**
 * 商品分类
 *
 * @author ruoyi
 */
public interface ISysProductCategoryService {

    /**
     * 新增商品分类
     *
     * @param cateDTO 新增商品分类入参
     * @return Integer
     */
    Integer create(ProdCateDTO cateDTO);

    /**
     * 编辑商品分类
     *
     * @param cateDTO 编辑商品分类入参
     * @return Integer
     */
    Integer update(ProdCateDTO cateDTO);

    /**
     * 删除商品分类
     *
     * @param prodCateId 商品分类ID
     * @return Integer
     */
    Integer delete(Long prodCateId);

    /**
     * 获取商品分类详情
     *
     * @param prodCateId 商品分类ID
     * @return ProdCateDTO
     */
    ProdCateDTO selectById(Long prodCateId);

    /**
     * 获取商品分类列表
     *
     * @param listDTO 查询入参
     * @return List<ProdCateListResDTO>
     */
    List<ProdCateListResDTO> selectList(ProdCateListDTO listDTO);

    /**
     * 获取APP首页商品分类
     *
     * @return List<AppHomeProdCateListResDTO>
     */
    List<AppHomeProdCateListResDTO> selectAppHomeCate();

    /**
     * 获取APP分类页
     *
     * @return List<AppHomeProdCateListResDTO>
     */
    List<AppHomeProdCateListResDTO> appCate();

}
