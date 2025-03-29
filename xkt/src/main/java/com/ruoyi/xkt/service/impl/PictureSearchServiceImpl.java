package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.PictureSearch;
import com.ruoyi.xkt.mapper.PictureSearchMapper;
import com.ruoyi.xkt.service.IPictureSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 以图搜款Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class PictureSearchServiceImpl implements IPictureSearchService {
    @Autowired
    private PictureSearchMapper pictureSearchMapper;

    /**
     * 查询以图搜款
     *
     * @param picSearchId 以图搜款主键
     * @return 以图搜款
     */
    @Override
    @Transactional(readOnly = true)
    public PictureSearch selectPictureSearchByPicSearchId(Long picSearchId) {
        return pictureSearchMapper.selectPictureSearchByPicSearchId(picSearchId);
    }

    /**
     * 查询以图搜款列表
     *
     * @param pictureSearch 以图搜款
     * @return 以图搜款
     */
    @Override
    @Transactional(readOnly = true)
    public List<PictureSearch> selectPictureSearchList(PictureSearch pictureSearch) {
        return pictureSearchMapper.selectPictureSearchList(pictureSearch);
    }

    /**
     * 新增以图搜款
     *
     * @param pictureSearch 以图搜款
     * @return 结果
     */
    @Override
    @Transactional
    public int insertPictureSearch(PictureSearch pictureSearch) {
        pictureSearch.setCreateTime(DateUtils.getNowDate());
        return pictureSearchMapper.insertPictureSearch(pictureSearch);
    }

    /**
     * 修改以图搜款
     *
     * @param pictureSearch 以图搜款
     * @return 结果
     */
    @Override
    @Transactional
    public int updatePictureSearch(PictureSearch pictureSearch) {
        pictureSearch.setUpdateTime(DateUtils.getNowDate());
        return pictureSearchMapper.updatePictureSearch(pictureSearch);
    }

    /**
     * 批量删除以图搜款
     *
     * @param picSearchIds 需要删除的以图搜款主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deletePictureSearchByPicSearchIds(Long[] picSearchIds) {
        return pictureSearchMapper.deletePictureSearchByPicSearchIds(picSearchIds);
    }

    /**
     * 删除以图搜款信息
     *
     * @param picSearchId 以图搜款主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deletePictureSearchByPicSearchId(Long picSearchId) {
        return pictureSearchMapper.deletePictureSearchByPicSearchId(picSearchId);
    }
}
