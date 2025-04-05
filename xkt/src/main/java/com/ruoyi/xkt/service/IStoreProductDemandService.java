package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.domain.StoreProductDemand;
import com.ruoyi.xkt.dto.storeProductDemand.*;

import java.util.List;

/**
 * 档口商品需求单Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreProductDemandService {
    /**
     * 查询档口商品需求单
     *
     * @param storeProdDemandId 档口商品需求单主键
     * @return 档口商品需求单
     */
    public StoreProductDemand selectStoreProductDemandByStoreProdDemandId(Long storeProdDemandId);

    /**
     * 查询档口商品需求单列表
     *
     * @param storeProductDemand 档口商品需求单
     * @return 档口商品需求单集合
     */
    public List<StoreProductDemand> selectStoreProductDemandList(StoreProductDemand storeProductDemand);

    /**
     * 新增档口商品需求单
     *
     * @param storeProductDemand 档口商品需求单
     * @return 结果
     */
    public int insertStoreProductDemand(StoreProductDemand storeProductDemand);

    /**
     * 修改档口商品需求单
     *
     * @param storeProductDemand 档口商品需求单
     * @return 结果
     */
    public int updateStoreProductDemand(StoreProductDemand storeProductDemand);

    /**
     * 批量删除档口商品需求单
     *
     * @param storeProdDemandIds 需要删除的档口商品需求单主键集合
     * @return 结果
     */
    public int deleteStoreProductDemandByStoreProdDemandIds(Long[] storeProdDemandIds);

    /**
     * 删除档口商品需求单信息
     *
     * @param storeProdDemandId 档口商品需求单主键
     * @return 结果
     */
    public int deleteStoreProductDemandByStoreProdDemandId(Long storeProdDemandId);

    /**
     * 获取指定门店及商品的库存和生产数量
     * 此方法用于查询特定门店中特定商品的库存和生产数量信息，帮助进行库存管理和生产计划制定
     *
     * @param storeId     门店ID，用于指定查询的门店
     * @param storeProdId 门店商品ID，用于指定查询的商品
     * @return 返回一个列表，包含门店商品的库存和生产数量信息的DTO对象
     */
    List<StoreProdDemandQuantityDTO> getStockAndProduceQuantity(Long storeId, Long storeProdId);


    /**
     * 创建需求订单
     *
     * @param demandDTO 商店产品需求信息，包含需求订单的相关数据，如产品ID、需求数量等
     * @return 返回一个字符串，通常包含需求订单的唯一标识或创建状态
     */
    Integer createDemand(StoreProdDemandDTO demandDTO);

    /**
     * 根据需求选择页面
     * 此方法用于根据提供的页面查询条件，返回相应的页面数据
     * 主要用于处理分页查询请求，以便在界面上展示特定的需求信息
     *
     * @param demandPageDTO 包含页面查询条件的DTO对象，如页码、每页条数等
     * @return Page<StoreProdDemandPageResDTO>
     */
    Page<StoreProdDemandPageResDTO> selectPage(StoreProdDemandPageDTO demandPageDTO);

    /**
     * 更新产品的生产状态
     * 此方法通过接收一个包含产品生产信息的DTO对象来更新数据库中对应产品的生产状态
     * 主要用于在生产流程中更新产品当前的加工状态或者生产阶段
     *
     * @param workingDTO 包含产品生产信息的数据传输对象用于更新产品生产状态
     * @return
     */
    Integer updateWorkingStatus(StoreProdDemandWorkingDTO workingDTO);

}
