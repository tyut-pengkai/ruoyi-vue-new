package com.ruoyi.system.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScriptResultVo {

    private Integer exitCode;
    private String result;
    private String error;

}
