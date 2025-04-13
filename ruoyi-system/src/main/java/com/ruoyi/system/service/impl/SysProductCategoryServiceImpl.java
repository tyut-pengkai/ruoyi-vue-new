package com.ruoyi.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.entity.SysProductCategory;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.dto.productCategory.ProdCateDTO;
import com.ruoyi.system.domain.dto.productCategory.ProdCateListDTO;
import com.ruoyi.system.domain.dto.productCategory.ProdCateListResDTO;
import com.ruoyi.system.mapper.SysProductCategoryMapper;
import com.ruoyi.system.service.ISysProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ruoyi.common.utils.SecurityUtils.getUsername;

/**
 * 字典 业务层处理
 *
 * @author ruoyi
 */
@Service
@RequiredArgsConstructor
public class SysProductCategoryServiceImpl implements ISysProductCategoryService {

    final SysProductCategoryMapper prodCateMapper;

    /**
     * 新增商品分类
     *
     * @param cateDTO 新增商品分类入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer create(ProdCateDTO cateDTO) {
        if (ObjectUtils.isNotEmpty(cateDTO.getProdCateId())) {
            throw new ServiceException("新增商品分类 prodCateId必须为空!", HttpStatus.ERROR);
        }
        return this.prodCateMapper.insert(BeanUtil.toBean(cateDTO, SysProductCategory.class));
    }

    /**
     * 编辑商品分类
     *
     * @param cateDTO 编辑商品分类入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer update(ProdCateDTO cateDTO) {
        SysProductCategory prodCate = Optional.ofNullable(this.prodCateMapper.selectOne(new LambdaQueryWrapper<SysProductCategory>()
                        .eq(SysProductCategory::getId, cateDTO.getProdCateId()).eq(SysProductCategory::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("商品分类不存在!", HttpStatus.ERROR));
        prodCate.setUpdateBy(getUsername());
        BeanUtil.copyProperties(cateDTO, prodCate);
        return this.prodCateMapper.updateById(prodCate);
    }

    /**
     * 删除商品分类
     *
     * @param prodCateId 商品分类ID
     * @return Integer
     */
    @Override
    @Transactional
    public Integer delete(Long prodCateId) {
        SysProductCategory prodCate = Optional.ofNullable(this.prodCateMapper.selectOne(new LambdaQueryWrapper<SysProductCategory>()
                        .eq(SysProductCategory::getId, prodCateId).eq(SysProductCategory::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("商品分类不存在!", HttpStatus.ERROR));
        prodCate.setDelFlag(Constants.DELETED);
        return this.prodCateMapper.updateById(prodCate);
    }

    /**
     * 获取商品分类详情
     *
     * @param prodCateId 商品分类ID
     * @return ProdCateDTO
     */
    @Override
    @Transactional(readOnly = true)
    public ProdCateDTO selectById(Long prodCateId) {
        SysProductCategory prodCate = Optional.ofNullable(this.prodCateMapper.selectOne(new LambdaQueryWrapper<SysProductCategory>()
                        .eq(SysProductCategory::getId, prodCateId).eq(SysProductCategory::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("商品分类不存在!", HttpStatus.ERROR));
        return BeanUtil.toBean(prodCate, ProdCateDTO.class);
    }

    /**
     * 获取商品分类列表
     *
     * @param listDTO 查询入参
     * @return List<ProdCateListDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProdCateListResDTO> selectList(ProdCateListDTO listDTO) {
        LambdaQueryWrapper<SysProductCategory> queryWrapper = new LambdaQueryWrapper<SysProductCategory>()
                .eq(SysProductCategory::getDelFlag, Constants.UNDELETED)
                .orderByAsc(SysProductCategory::getOrderNum);
        if (StringUtils.isNotBlank(listDTO.getName())) {
            queryWrapper.like(SysProductCategory::getName, listDTO.getName());
        }
        if (StringUtils.isNotBlank(listDTO.getStatus())) {
            queryWrapper.eq(SysProductCategory::getStatus, listDTO.getStatus());
        }
        List<SysProductCategory> prodCateList = this.prodCateMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(prodCateList)) {
            return new ArrayList<>();
        }
        List<ProdCateListResDTO> resList = BeanUtil.copyToList(prodCateList, ProdCateListResDTO.class);
        return this.buildCateTree(resList);
    }


    /**
     * 组装商品分类树
     *
     * @param cateList 商品分类列表
     * @return List<ProdCateListResDTO>
     */
    private List<ProdCateListResDTO> buildCateTree(List<ProdCateListResDTO> cateList) {
        List<ProdCateListResDTO> returnList = new ArrayList<>();
        List<Long> tempList = cateList.stream().map(ProdCateListResDTO::getId).collect(Collectors.toList());
        for (Iterator<ProdCateListResDTO> iterator = cateList.iterator(); iterator.hasNext(); ) {
            ProdCateListResDTO category = iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(category.getParentId())) {
                recursionFn(cateList, category);
                returnList.add(category);
            }
        }
        if (returnList.isEmpty()) {
            returnList = cateList;
        }
        return returnList;
    }


    /**
     * 递归列表
     *
     * @param cateList 分类表
     * @param cate     子节点
     */
    private void recursionFn(List<ProdCateListResDTO> cateList, ProdCateListResDTO cate) {
        // 得到子节点列表
        List<ProdCateListResDTO> childList = getChildList(cateList, cate);
        cate.setChildren(childList);
        for (ProdCateListResDTO tChild : childList) {
            if (hasChild(cateList, tChild)) {
                recursionFn(cateList, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<ProdCateListResDTO> getChildList(List<ProdCateListResDTO> cateList, ProdCateListResDTO cate) {
        List<ProdCateListResDTO> tlist = new ArrayList<>();
        Iterator<ProdCateListResDTO> it = cateList.iterator();
        while (it.hasNext()) {
            ProdCateListResDTO n = it.next();
            if (n.getParentId().longValue() == cate.getId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<ProdCateListResDTO> list, ProdCateListResDTO cate) {
        return getChildList(list, cate).size() > 0;
    }

}
