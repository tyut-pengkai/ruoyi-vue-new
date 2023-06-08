package com.ruoyi.system.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 仪表盘Mapper接口
 *
 * @author zwgu
 * @date 2022-04-20
 */
@Repository
public interface DashboardAppViewMapper {
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
    int queryAppUserTotalBetween(@Param("start") String start, @Param("end") String end);

    @Select("SELECT app_id, count( 1 ) as total_user\n" +
            "FROM sys_app_user au \n" +
            "WHERE au.create_time BETWEEN #{start} AND #{end}\n" +
            "GROUP BY app_id")
    List<Map<String, Object>> queryAppUserBetween(@Param("start") String start, @Param("end") String end);

    @Select("SELECT\n" +
            "\tcount( 1 ) \n" +
            "FROM\n" +
            "\tsys_app_user au \n" +
            "WHERE\n" +
            "\tau.last_login_time BETWEEN #{start} AND #{end}")
    int queryLoginAppUserTotalBetween(@Param("start") String start, @Param("end") String end);

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
    int queryAppUserVipTotalBetween(@Param("start") String start, @Param("end") String end);

    @Select("SELECT au.app_id, count( 1 ) AS total_user \n" +
            "FROM sys_app_user au JOIN sys_app a ON a.app_id = au.app_id \n" +
            "WHERE a.bill_type = 0 AND au.expire_time >= NOW() \n" +
            "GROUP BY au.app_id UNION ALL\n" +
            "SELECT au.app_id, count( 1 ) AS total_user \n" +
            "FROM sys_app_user au JOIN sys_app a ON a.app_id = au.app_id \n" +
            "WHERE a.bill_type = 1 AND au.point > 0 \n" +
            "GROUP BY au.app_id")
    List<Map<String, Object>> queryAppUserVip();

    @Select("SELECT\n" +
            "\tcount( 1 ) \n" +
            "FROM\n" +
            "\tsys_card c \n" +
            "WHERE\n" +
            "\tc.create_time BETWEEN #{start} AND #{end}")
    int queryCardTotalBetween(@Param("start") String start, @Param("end") String end);

    @Select("SELECT\n" +
            "\tcount( 1 ) \n" +
            "FROM\n" +
            "\tsys_card c \n" +
            "WHERE\n" +
            "\tc.charge_time BETWEEN #{start} AND #{end} and c.is_charged = 'Y'")
    int queryCardActiveBetween(@Param("start") String start, @Param("end") String end);

    @Select("SELECT\n" +
            "\tcount( 1 ) \n" +
            "FROM\n" +
            "\tsys_card c \n" +
            "WHERE\n" +
            "\tc.create_time BETWEEN #{start} AND #{end} and c.is_charged = 'N'")
    int queryCardNoActiveBetween(@Param("start") String start, @Param("end") String end);

    @Select("SELECT\n" +
            "\tcount( 1 ) \n" +
            "FROM\n" +
            "\tsys_login_code c \n" +
            "WHERE\n" +
            "\tc.create_time BETWEEN #{start} AND #{end}")
    int queryLoginCodeTotalBetween(@Param("start") String start, @Param("end") String end);

    @Select("SELECT\n" +
            "\tcount( 1 ) \n" +
            "FROM\n" +
            "\tsys_login_code c \n" +
            "WHERE\n" +
            "\tc.charge_time BETWEEN #{start} AND #{end} and c.is_charged = 'Y'")
    int queryLoginCodeActiveBetween(@Param("start") String start, @Param("end") String end);

    @Select("SELECT\n" +
            "\tcount( 1 ) \n" +
            "FROM\n" +
            "\tsys_login_code c \n" +
            "WHERE\n" +
            "\tc.create_time BETWEEN #{start} AND #{end} and c.is_charged = 'N'")
    int queryLoginCodeNoActiveBetween(@Param("start") String start, @Param("end") String end);

    @Select("SELECT c.app_id, a.app_name, count( 1 ) AS total_count FROM sys_card c join sys_app a on a.app_id = c.app_id\n" +
            "WHERE c.is_charged = 'Y' AND c.charge_time BETWEEN #{start} AND #{end} GROUP BY c.app_id, a.app_name")
    List<Map<String, Object>> queryAppCardActiveBetween(@Param("start") String start, @Param("end") String end);

    @Select("SELECT c.template_id, a.app_name, c.app_id, ct.card_name, count( 1 ) AS total_count FROM sys_card c join sys_app a on a.app_id = c.app_id join sys_card_template ct on ct.template_id = c.template_id\n" +
            "WHERE c.is_charged = 'Y' AND c.charge_time BETWEEN #{start} AND #{end} GROUP BY c.template_id, a.app_name, c.app_id, ct.card_name")
    List<Map<String, Object>> queryTemplateCardActiveBetween(@Param("start") String start, @Param("end") String end);

    @Select("SELECT\n" +
            "\tapp_id,\n" +
            "\tcount( 1 ) AS total_user \n" +
            "FROM\n" +
            "\tsys_app_user au \n" +
            "WHERE\n" +
            "\tau.last_login_time BETWEEN #{start} AND #{end} \n" +
            "GROUP BY app_id")
    List<Map<String, Object>> queryAppLoginAppUserTotalBetween(@Param("start") String start, @Param("end") String end);
}
