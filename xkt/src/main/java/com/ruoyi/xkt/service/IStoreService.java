package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.store.*;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 档口Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreService {

    /**
     * 更新档口数据
     *
     * @param storeUpdateDTO 更新数据入参
     * @return int
     */
    int updateStore(StoreUpdateDTO storeUpdateDTO);

    /**
     * 注册时新增档口信息
     *
     * @return int
     */
    int create();

    /**
     * 档口分页数据
     *
     * @param pageDTO 查询入参
     * @return
     */
    Page<StorePageResDTO> page(StorePageDTO pageDTO);

    /**
     * 更新档口启用/停用状态
     *
     * @param delFlagDTO 入参
     * @return
     */
    Integer updateDelFlag(StoreUpdateDelFlagDTO delFlagDTO);

    /**
     * 审核档口
     *
     * @param auditDTO 审核入参
     * @return Integer
     */
    Integer approve(StoreAuditDTO auditDTO);

    /**
     * 获取档口详细信息
     *
     * @param storeId 档口ID
     * @return StoreBasicResDTO
     */
    StoreResDTO getInfo(Long storeId);

    /**
     * 审核时获取档口信息
     *
     * @param storeId 档口ID
     * @return StoreApproveResDTO
     */
    StoreApproveResDTO getApproveInfo(Long storeId);

    /**
     * 获取APP档口基本信息
     *
     * @param storeId 档口ID
     * @return StoreAppResDTO
     */
    StoreAppResDTO getAppInfo(Long storeId);

    /**
     * 管理员审核推广获取档口基本信息
     *
     * @param storeId 档口ID
     * @return StoreAdvertResDTO
     */
    StoreAdvertResDTO getAdvertStoreInfo(Long storeId);

    /**
     * 档口首页数据概览
     *
     * @param overviewDTO 查询入参
     * @return StoreIndexOverviewResDTO
     */
    StoreIndexOverviewResDTO indexOverview(StoreOverviewDTO overviewDTO);

    /**
     * 档口首页销售额
     *
     * @param revenueDTO 查询入参
     * @return StoreIndexSaleRevenueResDTO
     */
    List<StoreIndexSaleRevenueResDTO> indexSaleRevenue(StoreSaleRevenueDTO revenueDTO);

    /**
     * 档口首页今日销售额
     *
     * @param storeId 档口ID
     * @return StoreIndexTodaySaleResDTO
     */
    List<StoreIndexTodaySaleResDTO> indexTodayProdSaleRevenue(Long storeId);

    /**
     * 档口商品销售额前10
     *
     * @param saleTop10DTO 销售额前10入参
     * @return List<StoreIndexSaleTop10ResDTO>
     */
    List<StoreIndexSaleTop10ResDTO> indexTop10Sale(StoreSaleTop10DTO saleTop10DTO);

    /**
     * 档口客户销售额前10
     *
     * @param saleCusTop10DTO 销售额前10入参
     * @return List<StoreIndexCustomerSaleTop10ResDTO>
     */
    List<StoreIndexCusSaleTop10ResDTO> indexTop10SaleCus(StoreSaleCustomerTop10DTO saleCusTop10DTO);

    /**
     * PC 商城 获取档口首页简易数据
     *
     * @param storeId 档口ID
     * @return StoreBasicResDTO
     */
    StoreSimpleResDTO getSimpleInfo(Long storeId);

    /**
     * 获取档口名称
     *
     * @param ids
     * @return
     */
    Map<Long, String> getStoreNameByIds(Collection<Long> ids);

    /**
     * 模糊查询档口名称
     *
     * @param storeName 档口名称
     * @return
     */
    List<StoreNameResDTO> fuzzyQuery(String storeName);

    /**
     * 更新档口权重
     *
     * @param storeWeightUpdateDTO 更新入参
     * @return Integer
     */
    Integer updateStoreWeight(StoreWeightUpdateDTO storeWeightUpdateDTO) throws IOException;

    /**
     * 获取今日客户销售额
     *
     * @param storeId 档口ID
     * @return StoreIndexTodayCusSaleResDTO
     */
    List<StoreIndexTodayCusSaleResDTO> indexTodayCusSaleRevenue(Long storeId);

    /**
     * 获取今日商品销售额前5
     *
     * @param storeId 档口ID
     * @return StoreIndexTodaySaleResDTO
     */
    StoreIndexTodaySaleTop5ResDTO indexTodayProdSaleRevenueTop5(Long storeId);

    /**
     * 档口状态
     *
     * @param storeId
     * @return
     */
    Integer getStoreStatus(Long storeId);

    /**
     * 更新档口库存系统
     *
     * @param stockSysDTO 更新入参
     * @return Integer
     */
    Integer updateStockSys(StoreUpdateStockSysDTO stockSysDTO);

}
