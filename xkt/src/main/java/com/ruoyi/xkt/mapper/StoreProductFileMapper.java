package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProductFile;

import java.util.List;

/**
 * 档口商品文件Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreProductFileMapper extends BaseMapper<StoreProductFile> {
    /**
     * 查询档口商品文件
     *
     * @param storeProdFileId 档口商品文件主键
     * @return 档口商品文件
     */
    public StoreProductFile selectStoreProductFileByStoreProdFileId(Long storeProdFileId);

    /**
     * 查询档口商品文件列表
     *
     * @param storeProductFile 档口商品文件
     * @return 档口商品文件集合
     */
    public List<StoreProductFile> selectStoreProductFileList(StoreProductFile storeProductFile);

    /**
     * 新增档口商品文件
     *
     * @param storeProductFile 档口商品文件
     * @return 结果
     */
    public int insertStoreProductFile(StoreProductFile storeProductFile);

    /**
     * 修改档口商品文件
     *
     * @param storeProductFile 档口商品文件
     * @return 结果
     */
    public int updateStoreProductFile(StoreProductFile storeProductFile);

    /**
     * 删除档口商品文件
     *
     * @param storeProdFileId 档口商品文件主键
     * @return 结果
     */
    public int deleteStoreProductFileByStoreProdFileId(Long storeProdFileId);

    /**
     * 批量删除档口商品文件
     *
     * @param storeProdFileIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreProductFileByStoreProdFileIds(Long[] storeProdFileIds);
}
