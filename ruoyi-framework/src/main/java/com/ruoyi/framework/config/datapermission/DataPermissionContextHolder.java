package com.ruoyi.framework.config.datapermission;


import com.ruoyi.common.core.domain.model.DataPermissionModel;

/**
 * @Description: 数据权限上下文
 * @Author DuanYangYang
 * @Date 2024/5/23/023 11:44
 **/
public class DataPermissionContextHolder {

    private static final ThreadLocal<DataPermissionModel> contextHolder = new ThreadLocal<>();

    public static DataPermissionModel getContext()
    {
        return contextHolder.get();
    }

    public static void setContext(DataPermissionModel context)
    {
        contextHolder.set(context);
    }

    public static void clearContext()
    {
        contextHolder.remove();
    }

}
