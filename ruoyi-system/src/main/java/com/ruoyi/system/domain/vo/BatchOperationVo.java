package com.ruoyi.system.domain.vo;

import com.ruoyi.common.enums.BatchOperationObject;
import com.ruoyi.common.enums.BatchOperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchOperationVo {

    private String content;
    private Long appId;
    private BatchOperationType operationType;
    private BatchOperationObject operationObject;
    private String operationValue;

}
