package com.zzyl.nursing.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("树形结构Vo")
public class TreeVo {

    private String value;
    private String label;
    private List<TreeVo> children;
}
