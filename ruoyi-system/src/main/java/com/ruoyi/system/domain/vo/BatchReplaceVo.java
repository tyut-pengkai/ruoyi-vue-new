package com.ruoyi.system.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchReplaceVo {

    private String content;
    private Long appId;
    private String changeMode; // 是否换整卡 Y/N
    private String remark;

}
