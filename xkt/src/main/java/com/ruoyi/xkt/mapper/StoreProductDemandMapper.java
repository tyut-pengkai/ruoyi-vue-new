package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProductDemand;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 档口商品需求单Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreProductDemandMapper extends BaseMapper<StoreProductDemand> {

    /**
     * 修改状态为已完成
     *
     * @param demandIdList 待更新的需求单ID列表
     */
    void updateStatusByIds(@Param("demandIdList") List<Long> demandIdList);

    /**
     * 删除指定的需求列表
     * <p>
     * 通过此方法可以批量删除需求，每个需求通过其唯一的ID来标识
     * 此方法接收一个需求ID列表，将根据这些ID删除对应的需求
     *
     * @param deleteDemandIdList 需要删除的需求ID列表，每个元素都是一个长整型数字
     */
    void deleteDemandList(@Param("deleteDemandIdList") List<Long> deleteDemandIdList);

}
