package com.easycode.cloud.strategy;


import com.easycode.cloud.domain.dto.RetTaskDto;
import com.weifu.cloud.domian.vo.StoragePositionVo;

/**
 * 出库任务策略接口
 *
 * @author bcp
 */
public interface StockInSubmitStrategy {


    Boolean checkType(String type);

    /**
     * 根据任务类型执行PDA提交策略
     * @param retTaskDto 入库任务
     * @return 结果
     */
    int submit(RetTaskDto retTaskDto, StoragePositionVo storagePositionVo) throws Exception;


}
