package com.ruoyi.common.enums;

/**
 * @Description: 通用枚举
 * @Author DuanYangYang
 * @Date 2024/5/17/017 16:15
 **/
public enum CommonEnum {

    //0：否，1：是
    NO(0),
    YES(1),
    ;

    private Integer code;

    CommonEnum(Integer code){
        this.code = code;
    }

    public Integer getCode(){
        return this.code;
    }

}
