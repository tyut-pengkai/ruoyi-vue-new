package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.advert.*;

import java.util.List;

/**
 * 推广营销Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IAdvertService {

    /**
     * 新增推广营销
     *
     * @param createDTO
     * @return
     */
    Integer create(AdvertCreateDTO createDTO);

    /**
     * 获取推广营销详情
     *
     * @param advertId 推广营销ID
     * @return AdvertResDTO
     */
    AdvertResDTO getInfo(Long advertId);

    /**
     * 推广营销分页
     *
     * @param pageDTO 分页查询入参
     * @return Page<AdvertResDTO>
     */
    Page<AdvertResDTO> page(AdvertPageDTO pageDTO);

    /**
     * 更新推广营销
     *
     * @param updateDTO 更新推广营销入参
     * @return Integer
     */
    Integer updateAdvert(AdvertUpdateDTO updateDTO);

    /**
     * 上线 下线推广营销
     *
     * @param changeStatusDTO 入参
     * @return Integer
     */
    Integer changeAdvertStatus(AdvertChangeStatusDTO changeStatusDTO);

    /**
     * 获取初始化平台列表
     *
     * @return List<AdvertGroupResDTO>
     */
    List<AdvertPlatformResDTO> getPlatformList();

    /**
     * 查看当前推广类型 示例图
     *
     * @param advertType 推广类型
     * @return String
     */
    String getDemoPic(Integer advertType);
}
