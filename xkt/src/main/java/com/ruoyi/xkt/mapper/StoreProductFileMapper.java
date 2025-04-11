package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProductFile;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdFilePicSpaceResDTO;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdFileResDTO;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdMainPicDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 档口商品文件Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Repository
public interface StoreProductFileMapper extends BaseMapper<StoreProductFile> {
    /**
     * 查询档口商品文件
     *
     * @param id 档口商品文件主键
     * @return 档口商品文件
     */
    public StoreProductFile selectStoreProductFileByStoreProdFileId(Long id);

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
     * @param id 档口商品文件主键
     * @return 结果
     */
    public int deleteStoreProductFileByStoreProdFileId(Long id);

    /**
     * 批量删除档口商品文件
     *
     * @param storeProdFileIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreProductFileByStoreProdFileIds(Long[] storeProdFileIds);

    void updateDelFlagByStoreProdId(Long storeProdId);

    /**
     * 根据档口商品ID获取所有的文件
     *
     * @param storeProdId 档口商品ID
     * @return List<StoreProdFileResDTO>
     */
    List<StoreProdFileResDTO> selectListByStoreProdId(Long storeProdId);

    /**
     * 根据商品ID列表选择主图
     *
     * @param storeProdIdList 商品ID列表，用于查询主图
     * @param fileType        文件类型，用于过滤查询结果
     * @param orderNum        排序编号，用于获取特定排序的主图
     * @return 返回匹配条件的主图DTO列表
     */
    List<StoreProdMainPicDTO> selectMainPicByStoreProdIdList(@Param("storeProdIdList") List<Long> storeProdIdList,
                                                             @Param("fileType") Integer fileType,
                                                             @Param("orderNum") Integer orderNum);

    /**
     * 获取档口图片空间的下载包
     *
     * @param storeId  档口ID
     * @param fileType 文件类型
     * @return List<StoreProdFilePicSpaceResDTO>
     */
    List<StoreProdFilePicSpaceResDTO> selectPicSpaceList(@Param("storeId") Long storeId, @Param("fileType") Integer fileType);

    /**
     * 查询所有的商品主图
     *
     * @param storeProdId 档口商品ID
     * @param storeId     档口ID
     * @param fileType    文件类型
     * @return
     */
    List<StoreProdFileResDTO> selectTotalMainPicList(@Param("storeProdId") Long storeProdId,
                                                     @Param("storeId") Long storeId,
                                                     @Param("fileType") Integer fileType);

}
