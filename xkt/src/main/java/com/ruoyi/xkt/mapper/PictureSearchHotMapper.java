package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.PictureSearchHot;

import java.util.List;

/**
 * 图搜热款Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface PictureSearchHotMapper extends BaseMapper<PictureSearchHot> {
    /**
     * 查询图搜热款
     *
     * @param picSearchHotId 图搜热款主键
     * @return 图搜热款
     */
    public PictureSearchHot selectPictureSearchHotByPicSearchHotId(Long picSearchHotId);

    /**
     * 查询图搜热款列表
     *
     * @param pictureSearchHot 图搜热款
     * @return 图搜热款集合
     */
    public List<PictureSearchHot> selectPictureSearchHotList(PictureSearchHot pictureSearchHot);

    /**
     * 新增图搜热款
     *
     * @param pictureSearchHot 图搜热款
     * @return 结果
     */
    public int insertPictureSearchHot(PictureSearchHot pictureSearchHot);

    /**
     * 修改图搜热款
     *
     * @param pictureSearchHot 图搜热款
     * @return 结果
     */
    public int updatePictureSearchHot(PictureSearchHot pictureSearchHot);

    /**
     * 删除图搜热款
     *
     * @param picSearchHotId 图搜热款主键
     * @return 结果
     */
    public int deletePictureSearchHotByPicSearchHotId(Long picSearchHotId);

    /**
     * 批量删除图搜热款
     *
     * @param picSearchHotIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePictureSearchHotByPicSearchHotIds(Long[] picSearchHotIds);
}
