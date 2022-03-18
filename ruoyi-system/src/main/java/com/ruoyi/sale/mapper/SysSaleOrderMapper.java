package com.ruoyi.sale.mapper;

import com.ruoyi.sale.domain.SysSaleOrder;
import com.ruoyi.sale.domain.SysSaleOrderItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 销售订单Mapper接口
 *
 * @author zwgu
 * @date 2022-02-21
 */
@Repository
public interface SysSaleOrderMapper {
    /**
     * 查询销售订单
     *
     * @param orderId 销售订单主键
     * @return 销售订单
     */
    public SysSaleOrder selectSysSaleOrderByOrderId(Long orderId);

    /**
     * 查询销售订单
     *
     * @param orderNo 销售订单主键
     * @return 销售订单
     */
    public SysSaleOrder selectSysSaleOrderByOrderNo(String orderNo);

    /**
     * 查询销售订单列表
     *
     * @param sysSaleOrder 销售订单
     * @return 销售订单集合
     */
    public List<SysSaleOrder> selectSysSaleOrderList(SysSaleOrder sysSaleOrder);

    /**
     * 查询销售订单列表
     *
     * @param sysSaleOrder 销售订单
     * @return 销售订单集合
     */
    public List<SysSaleOrder> selectSysSaleOrderQueryLimit5(SysSaleOrder sysSaleOrder);

    /**
     * 新增销售订单
     *
     * @param sysSaleOrder 销售订单
     * @return 结果
     */
    public int insertSysSaleOrder(SysSaleOrder sysSaleOrder);

    /**
     * 修改销售订单
     *
     * @param sysSaleOrder 销售订单
     * @return 结果
     */
    public int updateSysSaleOrder(SysSaleOrder sysSaleOrder);

    /**
     * 删除销售订单
     *
     * @param orderId 销售订单主键
     * @return 结果
     */
    public int deleteSysSaleOrderByOrderId(Long orderId);

    /**
     * 批量删除销售订单
     *
     * @param orderIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysSaleOrderByOrderIds(Long[] orderIds);

    /**
     * 批量删除销售订单详情
     *
     * @param orderIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysSaleOrderItemByOrderIds(Long[] orderIds);

    /**
     * 批量新增销售订单详情
     *
     * @param sysSaleOrderItemList 销售订单详情列表
     * @return 结果
     */
    public int batchSysSaleOrderItem(List<SysSaleOrderItem> sysSaleOrderItemList);

    /**
     * 通过销售订单主键删除销售订单详情信息
     *
     * @param orderId 销售订单ID
     * @return 结果
     */
    public int deleteSysSaleOrderItemByOrderId(Long orderId);

    @Select("select sum(actual_fee) from sys_sale_order sso where sso.status in ('1', '3', '4')")
    public BigDecimal queryTotalFee();

    @Select("select sum(actual_fee) from sys_sale_order sso where create_time BETWEEN #{start} and #{end}")
    public BigDecimal queryTotalFeeAllBetween(@Param("start") String start, @Param("end") String end);

    @Select("select sum(actual_fee) from sys_sale_order sso where sso.status in ('1', '3', '4') and create_time BETWEEN #{start} and #{end}")
    public BigDecimal queryTotalFeeBetween(@Param("start") String start, @Param("end") String end);

    @Select("select count(1) from sys_sale_order sso where sso.status in ('1', '3', '4')")
    public int queryTotalTrade();

    @Select("select count(1) from sys_sale_order sso")
    public int queryTotalTradeAll();

    @Select("select count(1) from sys_sale_order sso where create_time BETWEEN #{start} and #{end}")
    public int queryTotalTradeAllBetween(@Param("start") String start, @Param("end") String end);

    @Select("select count(1) from sys_sale_order sso where sso.status in ('1', '3', '4') and create_time BETWEEN #{start} and #{end}")
    public int queryTotalTradeBetween(@Param("start") String start, @Param("end") String end);

    @Select("SELECT a.app_id, sum( ssoi.actual_fee ) AS total_fee FROM sys_sale_order sso\n" +
            "\tJOIN sys_sale_order_item ssoi ON sso.order_id = ssoi.order_id AND ssoi.template_type = 1\n" +
            "\tJOIN sys_card_template ct ON ct.template_id = ssoi.template_id\n" +
            "\tJOIN sys_app a ON a.app_id = ct.app_id \n" +
            "WHERE sso.STATUS IN ( '1', '3', '4' )\n" +
            "GROUP BY a.app_id UNION ALL\n" +
            "SELECT a.app_id, sum( ssoi.actual_fee ) AS total_fee FROM sys_sale_order sso\n" +
            "\tJOIN sys_sale_order_item ssoi ON sso.order_id = ssoi.order_id AND ssoi.template_type = 2\n" +
            "\tJOIN sys_login_code_template ct ON ct.template_id = ssoi.template_id\n" +
            "\tJOIN sys_app a ON a.app_id = ct.app_id \n" +
            "WHERE sso.STATUS IN ( '1', '3', '4' )\n" +
            "GROUP BY a.app_id")
    public List<Map<String, Object>> queryAppTotalFee();

    @Select("SELECT a.app_id, sum( ssoi.actual_fee ) AS total_fee FROM sys_sale_order sso\n" +
            "\tJOIN sys_sale_order_item ssoi ON sso.order_id = ssoi.order_id AND ssoi.template_type = 1\n" +
            "\tJOIN sys_card_template ct ON ct.template_id = ssoi.template_id\n" +
            "\tJOIN sys_app a ON a.app_id = ct.app_id \n" +
            "WHERE sso.STATUS IN ( '1', '3', '4' ) AND sso.create_time BETWEEN #{start} AND #{end} \n" +
            "GROUP BY a.app_id UNION ALL\n" +
            "SELECT a.app_id, sum( ssoi.actual_fee ) AS total_fee FROM sys_sale_order sso\n" +
            "\tJOIN sys_sale_order_item ssoi ON sso.order_id = ssoi.order_id AND ssoi.template_type = 2\n" +
            "\tJOIN sys_login_code_template ct ON ct.template_id = ssoi.template_id\n" +
            "\tJOIN sys_app a ON a.app_id = ct.app_id \n" +
            "WHERE sso.STATUS IN ( '1', '3', '4' ) AND sso.create_time BETWEEN #{start} AND #{end} \n" +
            "GROUP BY a.app_id")
    public List<Map<String, Object>> queryAppTotalFeeBetween(@Param("start") String start, @Param("end") String end);

}
