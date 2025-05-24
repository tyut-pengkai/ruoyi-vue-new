package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.store.*;

import java.util.List;

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
     * @param createDTO 新增DTO
     * @return int
     */
    int create(StoreCreateDTO createDTO);

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
     * 获取档口基本信息
     *
     * @param storeId 档口ID
     * @return StoreBasicResDTO
     */
    StoreBasicResDTO getInfo(Long storeId);

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
    StoreIndexTodaySaleResDTO indexTodaySaleRevenue(Long storeId);

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

}
