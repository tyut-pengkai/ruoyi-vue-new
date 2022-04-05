package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.SysAppUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 软件用户Mapper接口
 * 
 * @author zwgu
 * @date 2021-11-09
 */
@Repository
public interface SysAppUserMapper 
{
    /**
     * 查询软件用户
     * 
     * @param appUserId 软件用户主键
     * @return 软件用户
     */
    public SysAppUser selectSysAppUserByAppUserId(Long appUserId);

    /**
     * 查询软件用户列表
     * 
     * @param sysAppUser 软件用户
     * @return 软件用户集合
     */
    public List<SysAppUser> selectSysAppUserList(SysAppUser sysAppUser);

    /**
     * 新增软件用户
     * 
     * @param sysAppUser 软件用户
     * @return 结果
     */
    public int insertSysAppUser(SysAppUser sysAppUser);

    /**
     * 修改软件用户
     * 
     * @param sysAppUser 软件用户
     * @return 结果
     */
    public int updateSysAppUser(SysAppUser sysAppUser);

    /**
     * 删除软件用户
     * 
     * @param appUserId 软件用户主键
     * @return 结果
     */
    public int deleteSysAppUserByAppUserId(Long appUserId);

    /**
     * 批量删除软件用户
     * 
     * @param appUserIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysAppUserByAppUserIds(Long[] appUserIds);

    /**
     * 查询软件用户
     *
     * @param appId 软件主键
     * @param userId 账号主键
     * @return 软件用户
     */
    public SysAppUser selectSysAppUserByAppIdAndUserId(@Param("appId") Long appId, @Param("userId") Long userId);

    /**
     * 查询软件用户
     *
     * @param appId     软件主键
     * @param loginCode 单码
     * @return 软件用户
     */
    SysAppUser selectSysAppUserByAppIdAndLoginCode(@Param("appId") Long appId, @Param("loginCode") String loginCode);

    /**
     * 查询各个软件的用户数量
     *
     * @return
     */
    @Select("SELECT\n" +
            "\tapp_id,\n" +
            "\tcount(1) as total_user\n" +
            "FROM\n" +
            "\tsys_app_user au \n" +
            "GROUP BY\n" +
            "\tapp_id")
    List<Map<String, Object>> queryAppUser();

    @Select("SELECT\n" +
            "\tcount( 1 ) \n" +
            "FROM\n" +
            "\tsys_app_user au \n" +
            "WHERE\n" +
            "\tau.create_time BETWEEN #{start} AND #{end}")
    int queryAppUserTotalBetween(String start, String end);

    @Select("SELECT app_id, count( 1 ) as total_user\n" +
            "FROM sys_app_user au \n" +
            "WHERE au.create_time BETWEEN #{start} AND #{end}\n" +
            "GROUP BY app_id")
    List<Map<String, Object>> queryAppUserBetween(String start, String end);

    @Select("SELECT\n" +
            "\tcount( 1 ) \n" +
            "FROM\n" +
            "\tsys_app_user au \n" +
            "WHERE\n" +
            "\tau.last_login_time BETWEEN #{start} AND #{end}")
    int queryLoginAppUserTotalBetween(String start, String end);

    @Select("SELECT (\n" +
            "\tSELECT count( 1 ) FROM sys_app_user au\n" +
            "\t\tJOIN sys_app a ON a.app_id = au.app_id \n" +
            "\tWHERE a.bill_type = 0 AND au.expire_time >= NOW()) \n" +
            "\t\t+ (SELECT count( 1 ) FROM sys_app_user au\n" +
            "\t\tJOIN sys_app a ON a.app_id = au.app_id \n" +
            "\tWHERE a.bill_type = 1 AND au.point > 0) \n" +
            "FROM DUAL")
    int queryAppUserVipTotal();

    @Select("SELECT (\n" +
            "\tSELECT count( 1 ) FROM sys_app_user au\n" +
            "\t\tJOIN sys_app a ON a.app_id = au.app_id \n" +
            "\tWHERE a.bill_type = 0 AND au.expire_time >= NOW() \n" +
            "\t\tAND au.create_time BETWEEN #{start} AND #{end} )\n" +
            "\t\t+ (SELECT count( 1 ) FROM sys_app_user au\n" +
            "\t\tJOIN sys_app a ON a.app_id = au.app_id \n" +
            "\tWHERE a.bill_type = 1 AND au.point > 0 \n" +
            "\t\tAND au.create_time BETWEEN #{start} AND #{end} ) \n" +
            "FROM DUAL")
    int queryAppUserVipTotalBetween(String start, String end);

    @Select("SELECT au.app_id, count( 1 ) AS total_user \n" +
            "FROM sys_app_user au JOIN sys_app a ON a.app_id = au.app_id \n" +
            "WHERE a.bill_type = 0 AND au.expire_time >= NOW() \n" +
            "GROUP BY au.app_id UNION ALL\n" +
            "SELECT au.app_id, count( 1 ) AS total_user \n" +
            "FROM sys_app_user au JOIN sys_app a ON a.app_id = au.app_id \n" +
            "WHERE a.bill_type = 1 AND au.point > 0 \n" +
            "GROUP BY au.app_id")
    List<Map<String, Object>> queryAppUserVip();


}
