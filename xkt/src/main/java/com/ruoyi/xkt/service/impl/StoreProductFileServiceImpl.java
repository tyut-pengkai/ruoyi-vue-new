package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreProductFile;
import com.ruoyi.xkt.mapper.StoreProductFileMapper;
import com.ruoyi.xkt.service.IStoreProductFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 档口商品文件Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreProductFileServiceImpl implements IStoreProductFileService {
    @Autowired
    private StoreProductFileMapper storeProductFileMapper;

    /**
     * 查询档口商品文件
     *
     * @param storeProdFileId 档口商品文件主键
     * @return 档口商品文件
     */
    @Override
    public StoreProductFile selectStoreProductFileByStoreProdFileId(Long storeProdFileId) {
        return storeProductFileMapper.selectStoreProductFileByStoreProdFileId(storeProdFileId);
    }

    /**
     * 查询档口商品文件列表
     *
     * @param storeProductFile 档口商品文件
     * @return 档口商品文件
     */
    @Override
    public List<StoreProductFile> selectStoreProductFileList(StoreProductFile storeProductFile) {
        return storeProductFileMapper.selectStoreProductFileList(storeProductFile);
    }

    /**
     * 新增档口商品文件
     *
     * @param storeProductFile 档口商品文件
     * @return 结果
     */
    @Override
    public int insertStoreProductFile(StoreProductFile storeProductFile) {
        storeProductFile.setCreateTime(DateUtils.getNowDate());
        return storeProductFileMapper.insertStoreProductFile(storeProductFile);
    }

    /**
     * 修改档口商品文件
     *
     * @param storeProductFile 档口商品文件
     * @return 结果
     */
    @Override
    public int updateStoreProductFile(StoreProductFile storeProductFile) {
        storeProductFile.setUpdateTime(DateUtils.getNowDate());
        return storeProductFileMapper.updateStoreProductFile(storeProductFile);
    }

    /**
     * 批量删除档口商品文件
     *
     * @param storeProdFileIds 需要删除的档口商品文件主键
     * @return 结果
     */
    @Override
    public int deleteStoreProductFileByStoreProdFileIds(Long[] storeProdFileIds) {
        return storeProductFileMapper.deleteStoreProductFileByStoreProdFileIds(storeProdFileIds);
    }

    /**
     * 删除档口商品文件信息
     *
     * @param storeProdFileId 档口商品文件主键
     * @return 结果
     */
    @Override
    public int deleteStoreProductFileByStoreProdFileId(Long storeProdFileId) {
        return storeProductFileMapper.deleteStoreProductFileByStoreProdFileId(storeProdFileId);
    }
}
