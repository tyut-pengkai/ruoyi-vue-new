package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
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
     * 批量删除档口商品需求单
     *
     * @param deleteDTO 需要删除的档口商品需求单主键集合
     * @return 结果
     */
    int deleteByStoreProdDemandIds(StoreProdDemandDeleteDTO deleteDTO);

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

    /**
     * 校验产品需求是否存在
     * 该方法用于验证给定的产品需求信息，在库存中是否存在有效的匹配
     * 主要用于在进行产品需求处理前，确保需求是可满足的或者已存在的
     *
     * @param demandVerifyDTO 包含需求验证信息的DTO对象，用于传递需求校验所需的参数
     * @return 返回一个包含校验结果的DTO对象，包括是否存在以及相关的验证信息
     */
    StoreProdDemandVerifyResDTO verifyDemandExist(StoreProdDemandVerifyDTO demandVerifyDTO);

    /**
     * 导出生产需求单
     *
     * @param exportDTO 导出入参
     * @return List<StoreProdDemandDownloadDTO>
     */
    List<StoreProdDemandDownloadDTO> export(StoreProdDemandExportDTO exportDTO);

    /**
     * 获取需求单各个状态对应的数量
     *
     * @param storeId 档口ID
     * @return StoreProdDemandStatusCountResDTO
     */
    StoreProdDemandStatusCountResDTO getStatusNum(Long storeId);

    /**
     * 全部完成
     *
     * @param finishAllDTO 全部完成
     * @return Integer
     */
    Integer finishAll(StoreProdDemandFinishAllDTO finishAllDTO);
}
