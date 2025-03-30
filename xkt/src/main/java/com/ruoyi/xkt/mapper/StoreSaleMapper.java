package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreSale;
import com.ruoyi.xkt.dto.storeSale.StoreSalePageDTO;
import com.ruoyi.xkt.dto.storeSale.StoreSalePageResDTO;

import java.util.List;

/**
 * 档口销售出库Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreSaleMapper extends BaseMapper<StoreSale> {
    /**
     * 查询档口销售出库
     *
     * @param id 档口销售出库主键
     * @return 档口销售出库
     */
    public StoreSale selectStoreSaleByStoreSaleId(Long id);

    /**
     * 查询档口销售出库列表
     *
     * @param storeSale 档口销售出库
     * @return 档口销售出库集合
     */
    public List<StoreSale> selectStoreSaleList(StoreSale storeSale);

    /**
     * 新增档口销售出库
     *
     * @param storeSale 档口销售出库
     * @return 结果
     */
    public int insertStoreSale(StoreSale storeSale);

    /**
     * 修改档口销售出库
     *
     * @param storeSale 档口销售出库
     * @return 结果
     */
    public int updateStoreSale(StoreSale storeSale);

    /**
     * 删除档口销售出库
     *
     * @param id 档口销售出库主键
     * @return 结果
     */
    public int deleteStoreSaleByStoreSaleId(Long id);

    /**
     * 批量删除档口销售出库
     *
     * @param storeSaleIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreSaleByStoreSaleIds(Long[] storeSaleIds);

    List<StoreSalePageResDTO> selectPage(StoreSalePageDTO pageDTO);

}
