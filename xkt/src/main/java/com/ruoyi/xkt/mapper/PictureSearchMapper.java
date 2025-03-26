package com.ruoyi.xkt.mapper;

import com.ruoyi.xkt.domain.PictureSearch;

import java.util.List;

/**
 * 以图搜款Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface PictureSearchMapper {
    /**
     * 查询以图搜款
     *
     * @param picSearchId 以图搜款主键
     * @return 以图搜款
     */
    public PictureSearch selectPictureSearchByPicSearchId(Long picSearchId);

    /**
     * 查询以图搜款列表
     *
     * @param pictureSearch 以图搜款
     * @return 以图搜款集合
     */
    public List<PictureSearch> selectPictureSearchList(PictureSearch pictureSearch);

    /**
     * 新增以图搜款
     *
     * @param pictureSearch 以图搜款
     * @return 结果
     */
    public int insertPictureSearch(PictureSearch pictureSearch);

    /**
     * 修改以图搜款
     *
     * @param pictureSearch 以图搜款
     * @return 结果
     */
    public int updatePictureSearch(PictureSearch pictureSearch);

    /**
     * 删除以图搜款
     *
     * @param picSearchId 以图搜款主键
     * @return 结果
     */
    public int deletePictureSearchByPicSearchId(Long picSearchId);

    /**
     * 批量删除以图搜款
     *
     * @param picSearchIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePictureSearchByPicSearchIds(Long[] picSearchIds);
}
