package com.ruoyi.api.v1.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RespItem {
    private Resp.DataType dataType;
    private String name;
    private String description;
    private RespItem[] items;

    public RespItem(Resp.DataType dataType, String name, String description) {
        this(dataType, name, description, null);
    }
}
