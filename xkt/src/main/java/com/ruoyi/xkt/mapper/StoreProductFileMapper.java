package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProductFile;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdFileLatestFourProdDTO;
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
    List<StoreProdFilePicSpaceResDTO> selectPicSpaceList(@Param("storeId") Long storeId, @Param("fileType") Integer fileType,
                                                         @Param("storeProdIdList") List<Long> storeProdIdList);

    /**
     * 根据storeProdIdList 查询所有主图
     *
     * @param storeProdIdList 商品ID列表
     * @return List<StoreProdFileResDTO>
     */
    List<StoreProdFileResDTO> selectMainPic(@Param("storeProdIdList") List<String> storeProdIdList);

    /**
     * 筛选档口最新的4个商品及主图
     *
     * @return List<StoreProdFileLatestFourProdDTO>
     */
    List<StoreProdFileLatestFourProdDTO> selectLatestFourProdList();

    /**
     * 根据商品ID获取商品主图和视频
     *
     * @param storeProdId 商品ID
     * @return List<StoreProdFileResDTO>
     */
    List<StoreProdFileResDTO> selectVideoAndMainPicList(Long storeProdId);

}
