package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreColor;
import com.ruoyi.xkt.mapper.StoreColorMapper;
import com.ruoyi.xkt.service.IStoreColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 档口所有颜色Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreColorServiceImpl implements IStoreColorService {
    @Autowired
    private StoreColorMapper storeColorMapper;

    /**
     * 查询档口所有颜色
     *
     * @param storeColorId 档口所有颜色主键
     * @return 档口所有颜色
     */
    @Override
    @Transactional(readOnly = true)
    public StoreColor selectStoreColorByStoreColorId(Long storeColorId) {
        return storeColorMapper.selectStoreColorByStoreColorId(storeColorId);
    }

    /**
     * 查询档口所有颜色列表
     *
     * @param storeColor 档口所有颜色
     * @return 档口所有颜色
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreColor> selectStoreColorList(StoreColor storeColor) {
        return storeColorMapper.selectStoreColorList(storeColor);
    }

    /**
     * 新增档口所有颜色
     *
     * @param storeColor 档口所有颜色
     * @return 结果
     */
    @Override
    @Transactional
    public int insertStoreColor(StoreColor storeColor) {
        storeColor.setCreateTime(DateUtils.getNowDate());
        return storeColorMapper.insertStoreColor(storeColor);
    }

    /**
     * 修改档口所有颜色
     *
     * @param storeColor 档口所有颜色
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStoreColor(StoreColor storeColor) {
        storeColor.setUpdateTime(DateUtils.getNowDate());
        return storeColorMapper.updateStoreColor(storeColor);
    }

    /**
     * 批量删除档口所有颜色
     *
     * @param storeColorIds 需要删除的档口所有颜色主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteStoreColorByStoreColorIds(Long[] storeColorIds) {
        return storeColorMapper.deleteStoreColorByStoreColorIds(storeColorIds);
    }

    /**
     * 删除档口所有颜色信息
     *
     * @param storeColorId 档口所有颜色主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteStoreColorByStoreColorId(Long storeColorId) {
        return storeColorMapper.deleteStoreColorByStoreColorId(storeColorId);
    }
}
