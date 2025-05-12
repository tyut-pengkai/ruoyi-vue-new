package com.easycode.cloud.strategy;


import com.easycode.cloud.domain.dto.RetTaskDto;

/**
 * 退货任务策略接口
 * @author bcp
 */

public interface IRetTaskStrategy {

    /**
     * pda退货任务提交,更新退货单据
     * @param retTaskDto 退货任务
     * @return 结果
     */
    int updateRetOrder(RetTaskDto retTaskDto);
}
