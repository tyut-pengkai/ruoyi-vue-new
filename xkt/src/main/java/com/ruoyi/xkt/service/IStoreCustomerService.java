package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.storeCustomer.*;

import java.util.List;

/**
 * 档口客户Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreCustomerService {
    /**
     * 查询档口客户
     *
     * @param storeCusId 档口客户主键
     * @return 档口客户
     */
    StoreCusDTO selectByStoreCusId(Long storeCusId);

    /**
     * 修改档口客户
     *
     * @param storeCusDTO 档口客户
     * @return 结果
     */
    int updateStoreCus(StoreCusDTO storeCusDTO);

    /**
     * 新增档口客户
     *
     * @param storeCusDTO 新增入参
     * @return
     */
    int create(StoreCusDTO storeCusDTO);

    /**
     * 删除档口客户
     *
     * @param storeCusId 档口客户ID
     */
    void deleteStoreCus(Long storeCusId);

    /**
     * 查询档口客户分页
     *
     * @param storeCusPageDTO
     * @return
     */
    Page<StoreCusPageResDTO> selectPage(StoreCusPageDTO storeCusPageDTO);

    /**
     * 模糊查询客户名称列表
     *
     * @param storeId 档口ID
     * @param cusName 客户名称
     * @return List<StoreCusFuzzyResDTO>
     */
    List<StoreCusFuzzyResDTO> fuzzyQueryList(Long storeId, String cusName);

}
