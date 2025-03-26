package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.PictureSearchResult;

import java.util.List;

/**
 * 以图搜款结果Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IPictureSearchResultService {
    /**
     * 查询以图搜款结果
     *
     * @param picSearchResId 以图搜款结果主键
     * @return 以图搜款结果
     */
    public PictureSearchResult selectPictureSearchResultByPicSearchResId(Long picSearchResId);

    /**
     * 查询以图搜款结果列表
     *
     * @param pictureSearchResult 以图搜款结果
     * @return 以图搜款结果集合
     */
    public List<PictureSearchResult> selectPictureSearchResultList(PictureSearchResult pictureSearchResult);

    /**
     * 新增以图搜款结果
     *
     * @param pictureSearchResult 以图搜款结果
     * @return 结果
     */
    public int insertPictureSearchResult(PictureSearchResult pictureSearchResult);

    /**
     * 修改以图搜款结果
     *
     * @param pictureSearchResult 以图搜款结果
     * @return 结果
     */
    public int updatePictureSearchResult(PictureSearchResult pictureSearchResult);

    /**
     * 批量删除以图搜款结果
     *
     * @param picSearchResIds 需要删除的以图搜款结果主键集合
     * @return 结果
     */
    public int deletePictureSearchResultByPicSearchResIds(Long[] picSearchResIds);

    /**
     * 删除以图搜款结果信息
     *
     * @param picSearchResId 以图搜款结果主键
     * @return 结果
     */
    public int deletePictureSearchResultByPicSearchResId(Long picSearchResId);
}
