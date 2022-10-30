package com.ruoyi.sale.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 仪表盘Mapper接口
 *
 * @author zwgu
 * @date 2022-02-21
 */
@Repository
public interface DashboardSaleViewMapper {

    @Select("select IFNULL(sum(actual_fee),0) from sys_sale_order sso where sso.status in ('1', '3', '4')")
    public BigDecimal queryTotalFee();

    @Select("select IFNULL(sum(actual_fee),0) from sys_sale_order sso where create_time BETWEEN #{start} and #{end}")
    public BigDecimal queryTotalFeeAllBetween(@Param("start") String start, @Param("end") String end);

    @Select("select IFNULL(sum(actual_fee),0) from sys_sale_order sso where sso.status in ('1', '3', '4') and create_time BETWEEN #{start} and #{end}")
    public BigDecimal queryTotalFeeBetween(@Param("start") String start, @Param("end") String end);

    @Select("select count(1) from sys_sale_order sso where sso.status in ('1', '3', '4')")
    public int queryTotalTrade();

    @Select("select count(1) from sys_sale_order sso")
    public int queryTotalTradeAll();

    @Select("select count(1) from sys_sale_order sso where create_time BETWEEN #{start} and #{end}")
    public int queryTotalTradeAllBetween(@Param("start") String start, @Param("end") String end);

    @Select("select count(1) from sys_sale_order sso where sso.status in ('1', '3', '4') and create_time BETWEEN #{start} and #{end}")
    public int queryTotalTradeBetween(@Param("start") String start, @Param("end") String end);

    @Select("SELECT a.app_id, IFNULL(sum( ssoi.actual_fee ),0) AS total_fee FROM sys_sale_order sso\n" +
            "\tJOIN sys_sale_order_item ssoi ON sso.order_id = ssoi.order_id AND ssoi.template_type = 1\n" +
            "\tJOIN sys_card_template ct ON ct.template_id = ssoi.template_id\n" +
            "\tJOIN sys_app a ON a.app_id = ct.app_id \n" +
            "WHERE sso.STATUS IN ( '1', '3', '4' )\n" +
            "GROUP BY a.app_id UNION ALL\n" +
            "SELECT a.app_id, IFNULL(sum( ssoi.actual_fee ),0) AS total_fee FROM sys_sale_order sso\n" +
            "\tJOIN sys_sale_order_item ssoi ON sso.order_id = ssoi.order_id AND ssoi.template_type = 2\n" +
            "\tJOIN sys_login_code_template ct ON ct.template_id = ssoi.template_id\n" +
            "\tJOIN sys_app a ON a.app_id = ct.app_id \n" +
            "WHERE sso.STATUS IN ( '1', '3', '4' )\n" +
            "GROUP BY a.app_id")
    public List<Map<String, Object>> queryAppTotalFee();

    @Select("SELECT a.app_id, IFNULL(sum( ssoi.actual_fee ),0) AS total_fee FROM sys_sale_order sso\n" +
            "\tJOIN sys_sale_order_item ssoi ON sso.order_id = ssoi.order_id AND ssoi.template_type = 1\n" +
            "\tJOIN sys_card_template ct ON ct.template_id = ssoi.template_id\n" +
            "\tJOIN sys_app a ON a.app_id = ct.app_id \n" +
            "WHERE sso.STATUS IN ( '1', '3', '4' ) AND sso.create_time BETWEEN #{start} AND #{end} \n" +
            "GROUP BY a.app_id UNION ALL\n" +
            "SELECT a.app_id, IFNULL(sum( ssoi.actual_fee ),0) AS total_fee FROM sys_sale_order sso\n" +
            "\tJOIN sys_sale_order_item ssoi ON sso.order_id = ssoi.order_id AND ssoi.template_type = 2\n" +
            "\tJOIN sys_login_code_template ct ON ct.template_id = ssoi.template_id\n" +
            "\tJOIN sys_app a ON a.app_id = ct.app_id \n" +
            "WHERE sso.STATUS IN ( '1', '3', '4' ) AND sso.create_time BETWEEN #{start} AND #{end} \n" +
            "GROUP BY a.app_id")
    public List<Map<String, Object>> queryAppTotalFeeBetween(@Param("start") String start, @Param("end") String end);

    @Select("SELECT sso.pay_mode, count(pay_mode) as total_count FROM sys_sale_order sso\n" +
            "WHERE sso.STATUS IN ( '1', '3', '4' ) AND sso.create_time BETWEEN #{start} AND #{end} GROUP BY pay_mode")
    public List<Map<String, Object>> queryPayModeBetween(@Param("start") String start, @Param("end") String end);

}
