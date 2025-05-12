package com.easycode.cloud.mapper;


import com.weifu.cloud.domain.StockInItemBom;
import com.weifu.cloud.domain.vo.StockInItemBomVo;
import com.weifu.cloud.domian.InventoryUnopenOpenDetails;
import com.weifu.cloud.domian.vo.InventoryUnopenOpenDetailsVo;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 标准入库单Mapper接口
 *
 * @author weifu
 * @date 2022-12-07
 */
@Repository
public interface StockInItemBomMapper
{

    /**
     * bom信息列表查询
     * @param stockInItemBomVo
     * @return
     */
    List<StockInItemBom> selectStockInItemBomList(StockInItemBomVo stockInItemBomVo);

    /**
     * 新增bom信息
     * @param stockInItemBom
     * @return
     */
    int insertStockInItemBomDetails(StockInItemBom stockInItemBom);


    /**
     * 删除bom信息
     * @param stdOrderId
     * @return
     */
    int deleteStockInItemBomDetails(Long stdOrderId);

}
