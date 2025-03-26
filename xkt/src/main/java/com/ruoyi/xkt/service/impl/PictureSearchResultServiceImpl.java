package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.PictureSearchResult;
import com.ruoyi.xkt.mapper.PictureSearchResultMapper;
import com.ruoyi.xkt.service.IPictureSearchResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 以图搜款结果Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class PictureSearchResultServiceImpl implements IPictureSearchResultService {
    @Autowired
    private PictureSearchResultMapper pictureSearchResultMapper;

    /**
     * 查询以图搜款结果
     *
     * @param picSearchResId 以图搜款结果主键
     * @return 以图搜款结果
     */
    @Override
    public PictureSearchResult selectPictureSearchResultByPicSearchResId(Long picSearchResId) {
        return pictureSearchResultMapper.selectPictureSearchResultByPicSearchResId(picSearchResId);
    }

    /**
     * 查询以图搜款结果列表
     *
     * @param pictureSearchResult 以图搜款结果
     * @return 以图搜款结果
     */
    @Override
    public List<PictureSearchResult> selectPictureSearchResultList(PictureSearchResult pictureSearchResult) {
        return pictureSearchResultMapper.selectPictureSearchResultList(pictureSearchResult);
    }

    /**
     * 新增以图搜款结果
     *
     * @param pictureSearchResult 以图搜款结果
     * @return 结果
     */
    @Override
    public int insertPictureSearchResult(PictureSearchResult pictureSearchResult) {
        pictureSearchResult.setCreateTime(DateUtils.getNowDate());
        return pictureSearchResultMapper.insertPictureSearchResult(pictureSearchResult);
    }

    /**
     * 修改以图搜款结果
     *
     * @param pictureSearchResult 以图搜款结果
     * @return 结果
     */
    @Override
    public int updatePictureSearchResult(PictureSearchResult pictureSearchResult) {
        pictureSearchResult.setUpdateTime(DateUtils.getNowDate());
        return pictureSearchResultMapper.updatePictureSearchResult(pictureSearchResult);
    }

    /**
     * 批量删除以图搜款结果
     *
     * @param picSearchResIds 需要删除的以图搜款结果主键
     * @return 结果
     */
    @Override
    public int deletePictureSearchResultByPicSearchResIds(Long[] picSearchResIds) {
        return pictureSearchResultMapper.deletePictureSearchResultByPicSearchResIds(picSearchResIds);
    }

    /**
     * 删除以图搜款结果信息
     *
     * @param picSearchResId 以图搜款结果主键
     * @return 结果
     */
    @Override
    public int deletePictureSearchResultByPicSearchResId(Long picSearchResId) {
        return pictureSearchResultMapper.deletePictureSearchResultByPicSearchResId(picSearchResId);
    }
}
