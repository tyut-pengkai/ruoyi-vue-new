package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.domain.StoreFactory;
import com.ruoyi.xkt.dto.storeFactory.StoreFactoryDTO;
import com.ruoyi.xkt.dto.storeFactory.StoreFactoryPageDTO;
import com.ruoyi.xkt.dto.storeFactory.StoreFactoryResDTO;

import java.util.List;

/**
 * 档口合作工厂Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreFactoryService {
    /**
     * 查询档口合作工厂
     *
     * @param storeId    档口ID
     * @param storeFacId 档口合作工厂主键
     * @return 档口合作工厂
     */
    public StoreFactoryResDTO selectByStoreFacId(Long storeId, Long storeFacId);

    /**
     * 查询档口合作工厂列表
     *
     * @param storeFactory 档口合作工厂
     * @return 档口合作工厂集合
     */
    public List<StoreFactory> selectStoreFactoryList(StoreFactory storeFactory);

    /**
     * 新增档口合作工厂
     *
     * @param storeFactoryDTO 档口合作工厂
     * @return 结果
     */
    public int insertStoreFactory(StoreFactoryDTO storeFactoryDTO);

    /**
     * 修改档口合作工厂
     *
     * @param storeFactoryDTO 档口合作工厂
     * @return 结果
     */
    public int updateStoreFactory(StoreFactoryDTO storeFactoryDTO);

    /**
     * 批量删除档口合作工厂
     *
     * @param storeFacIds 需要删除的档口合作工厂主键集合
     * @return 结果
     */
    public int deleteStoreFactoryByStoreFacIds(Long[] storeFacIds);

    /**
     * 删除档口合作工厂信息
     *
     * @param storeFacId 档口合作工厂主键
     * @return 结果
     */
    public int deleteStoreFactoryByStoreFacId(Long storeFacId);

    /**
     * 根据条件分页查询工厂信息
     *
     * @param pageDTO 包含分页查询条件的DTO对象，如页码、每页记录数等
     * @return 返回一个分页对象，包含查询结果中的工厂信息
     */
    Page<StoreFactoryResDTO> selectFactoryPage(StoreFactoryPageDTO pageDTO);

    /**
     * 获取所有的工厂列表
     *
     * @param storeId 档口ID
     * @return 生产需求管理 工厂下拉列表
     */
    List<StoreFactoryResDTO> getList(Long storeId);

}
