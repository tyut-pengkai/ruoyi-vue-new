package com.ruoyi.common.enums;

/**
 *  数据权限类型
 * 
 * @author duanyangyang
 *
 */
public enum DataPermissionType {

    /**
     * 1=所有数据权限,2=部门数据权限,3=仅本人数据权限
     */

    ALL("1"),
    DEPT("2"),
    SELF("3"),

    ;

    private String code;

    DataPermissionType(String code){
        this.code = code;
    }

    public String getCode(){
        return this.code;
    }
}
