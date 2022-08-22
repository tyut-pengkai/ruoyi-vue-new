package com.ruoyi.api.v1.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Resp {

    private DataType dataType;
    private String description;
    private RespItem[] items;

    public enum DataType {
        string, integer, number, bool, object, array
    }
}
