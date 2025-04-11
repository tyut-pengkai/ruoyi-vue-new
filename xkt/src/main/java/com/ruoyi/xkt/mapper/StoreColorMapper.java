package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreColor;
import com.ruoyi.xkt.dto.storeColor.StoreColorDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 档口所有颜色Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Repository
public interface StoreColorMapper extends BaseMapper<StoreColor> {
    /**
     * 查询档口所有颜色
     *
     * @param id 档口所有颜色主键
     * @return 档口所有颜色
     */
    public StoreColor selectStoreColorByStoreColorId(Long id);

    /**
     * 查询档口所有颜色列表
     *
     * @param storeColor 档口所有颜色
     * @return 档口所有颜色集合
     */
    public List<StoreColor> selectStoreColorList(StoreColor storeColor);

    /**
     * 新增档口所有颜色
     *
     * @param storeColor 档口所有颜色
     * @return 结果
     */
    public int insertStoreColor(StoreColor storeColor);

    /**
     * 修改档口所有颜色
     *
     * @param storeColor 档口所有颜色
     * @return 结果
     */
    public int updateStoreColor(StoreColor storeColor);

    /**
     * 删除档口所有颜色
     *
     * @param id 档口所有颜色主键
     * @return 结果
     */
    public int deleteStoreColorByStoreColorId(Long id);

    /**
     * 批量删除档口所有颜色
     *
     * @param storeColorIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreColorByStoreColorIds(Long[] storeColorIds);

    List<StoreColorDTO> selectListByStoreProdId(Long storeId);

}
