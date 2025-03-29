package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.PictureSearchHot;
import com.ruoyi.xkt.mapper.PictureSearchHotMapper;
import com.ruoyi.xkt.service.IPictureSearchHotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 图搜热款Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class PictureSearchHotServiceImpl implements IPictureSearchHotService {
    @Autowired
    private PictureSearchHotMapper pictureSearchHotMapper;

    /**
     * 查询图搜热款
     *
     * @param picSearchHotId 图搜热款主键
     * @return 图搜热款
     */
    @Override
    @Transactional(readOnly = true)
    public PictureSearchHot selectPictureSearchHotByPicSearchHotId(Long picSearchHotId) {
        return pictureSearchHotMapper.selectPictureSearchHotByPicSearchHotId(picSearchHotId);
    }

    /**
     * 查询图搜热款列表
     *
     * @param pictureSearchHot 图搜热款
     * @return 图搜热款
     */
    @Override
    @Transactional(readOnly = true)
    public List<PictureSearchHot> selectPictureSearchHotList(PictureSearchHot pictureSearchHot) {
        return pictureSearchHotMapper.selectPictureSearchHotList(pictureSearchHot);
    }

    /**
     * 新增图搜热款
     *
     * @param pictureSearchHot 图搜热款
     * @return 结果
     */
    @Override
    @Transactional
    public int insertPictureSearchHot(PictureSearchHot pictureSearchHot) {
        pictureSearchHot.setCreateTime(DateUtils.getNowDate());
        return pictureSearchHotMapper.insertPictureSearchHot(pictureSearchHot);
    }

    /**
     * 修改图搜热款
     *
     * @param pictureSearchHot 图搜热款
     * @return 结果
     */
    @Override
    @Transactional
    public int updatePictureSearchHot(PictureSearchHot pictureSearchHot) {
        pictureSearchHot.setUpdateTime(DateUtils.getNowDate());
        return pictureSearchHotMapper.updatePictureSearchHot(pictureSearchHot);
    }

    /**
     * 批量删除图搜热款
     *
     * @param picSearchHotIds 需要删除的图搜热款主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deletePictureSearchHotByPicSearchHotIds(Long[] picSearchHotIds) {
        return pictureSearchHotMapper.deletePictureSearchHotByPicSearchHotIds(picSearchHotIds);
    }

    /**
     * 删除图搜热款信息
     *
     * @param picSearchHotId 图搜热款主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deletePictureSearchHotByPicSearchHotId(Long picSearchHotId) {
        return pictureSearchHotMapper.deletePictureSearchHotByPicSearchHotId(picSearchHotId);
    }
}
