
package com.coordsoft.hysdk.vo;

import lombok.Data;

@Data
public class RequestResult {
    private String msg = "";
    private int code;
    private String data = "";
    private String timestamp = "";
    private String vstr = "";
    private String detail = "";
}
