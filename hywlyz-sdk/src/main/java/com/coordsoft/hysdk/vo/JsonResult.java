package com.coordsoft.hysdk.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class JsonResult {
    private boolean success;
    private JSONObject data;
    private String errorMsg;
}
