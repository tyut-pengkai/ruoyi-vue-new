package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.PictureSearchResult;

import java.util.List;

/**
 * 以图搜款结果Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface PictureSearchResultMapper extends BaseMapper<PictureSearchResult> {
    /**
     * 查询以图搜款结果
     *
     * @param id 以图搜款结果主键
     * @return 以图搜款结果
     */
    public PictureSearchResult selectPictureSearchResultByPicSearchResId(Long id);

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
     * 删除以图搜款结果
     *
     * @param id 以图搜款结果主键
     * @return 结果
     */
    public int deletePictureSearchResultByPicSearchResId(Long id);

    /**
     * 批量删除以图搜款结果
     *
     * @param picSearchResIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePictureSearchResultByPicSearchResIds(Long[] picSearchResIds);
}
