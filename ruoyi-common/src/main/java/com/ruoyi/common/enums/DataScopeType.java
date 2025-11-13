package com.ruoyi.common.enums;

/**
 *  数据权限类型
 * 
 * @author duanyangyang
 *
 */
public enum DataScopeType {

    /**
     * 1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限 5:本人数据权限
     */

    ALL("1"),
    CUSTOM("2"),
    DEPT("3"),
    DEPT_AND_CHILD("4"),
    SELF("5"),

    ;

    private String code;

    DataScopeType(String code){
        this.code = code;
    }

    public String getCode(){
        return this.code;
    }
}
