package com.ruoyi.api.v1.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Param {
    private String name;
    private boolean necessary;
    private String description;
}
