package com.ruoyi.web.controller.sale;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.SaleOrderStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.framework.web.service.PermissionService;
import com.ruoyi.sale.domain.SysSaleOrder;
import com.ruoyi.sale.domain.SysSaleOrderItem;
import com.ruoyi.sale.domain.SysSaleOrderItemGoods;
import com.ruoyi.sale.domain.vo.SysCardVo;
import com.ruoyi.sale.domain.vo.SysLoginCodeVo;
import com.ruoyi.sale.service.ISysSaleOrderItemGoodsService;
import com.ruoyi.sale.service.ISysSaleOrderService;
import com.ruoyi.sale.service.ISysSaleShopService;
import com.ruoyi.system.domain.SysCard;
import com.ruoyi.system.domain.SysLoginCode;
import com.ruoyi.system.service.ISysCardService;
import com.ruoyi.system.service.ISysLoginCodeService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.utils.poi.ExcelUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 销售订单Controller
 *
 * @author zwgu
 * @date 2022-02-21
 */
@RestController
@RequestMapping("/sale/saleOrder")
public class SysSaleOrderController extends BaseController {
    @Resource
    private ISysSaleOrderService sysSaleOrderService;
    @Resource
    private ISysSaleOrderItemGoodsService sysSaleOrderItemGoodsService;
    @Resource
    private ISysCardService sysCardService;
    @Resource
    private ISysLoginCodeService sysLoginCodeService;
    @Resource
    private ISysSaleShopService sysSaleShopService;
    @Resource
    private ISysUserService userService;
    @Resource
    private PermissionService permissionService;

    /**
     * 查询销售订单列表
     */
    @GetMapping("/self/list")
    public TableDataInfo listSelf(SysSaleOrder sysSaleOrder) {
        startPage();
        sysSaleOrder.setUserId(getUserId());
        List<SysSaleOrder> list = sysSaleOrderService.selectSysSaleOrderList(sysSaleOrder);
        return getDataTable(list);
    }

    /**
     * 获取销售订单详细信息
     */
    @GetMapping(value = "/self/{orderId}")
    public AjaxResult getInfoSelf(@PathVariable("orderId") Long orderId) {
        SysSaleOrder sso = sysSaleOrderService.selectSysSaleOrderByOrderId(orderId);
        if (Objects.equals(sso.getUserId(), getUserId())) {
            return getInfo(orderId);
        } else {
            throw new ServiceException("您没有查看该数据的权限");
        }
    }

    /**
     * 查询销售订单列表
     */
    @PreAuthorize("@ss.hasPermi('sale:saleOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysSaleOrder sysSaleOrder) {
        startPage();
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            sysSaleOrder.setUserId(getUserId());
        }
        List<SysSaleOrder> list = sysSaleOrderService.selectSysSaleOrderList(sysSaleOrder);
        return getDataTable(list);
    }

    /**
     * 导出销售订单列表
     */
    @PreAuthorize("@ss.hasPermi('sale:saleOrder:export')")
    @Log(title = "销售订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysSaleOrder sysSaleOrder) {
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            sysSaleOrder.setUserId(getUserId());
        }
        List<SysSaleOrder> list = sysSaleOrderService.selectSysSaleOrderList(sysSaleOrder);
        ExcelUtil<SysSaleOrder> util = new ExcelUtil<SysSaleOrder>(SysSaleOrder.class);
        util.exportExcel(response, list, "销售订单数据");
    }

    /**
     * 获取销售订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('sale:saleOrder:query')")
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable("orderId") Long orderId) {
        SysSaleOrder sso = sysSaleOrderService.selectSysSaleOrderByOrderId(orderId);
        if (sso.getUserId() != null) {
            SysUser user = userService.selectUserById(sso.getUserId());
            sso.setUser(user);
        }
        List<SysSaleOrderItem> itemList = sso.getSysSaleOrderItemList();
        for (SysSaleOrderItem item : itemList) {
            item.setGoodsList(new ArrayList<>());
            List<SysSaleOrderItemGoods> goodsList = sysSaleOrderItemGoodsService.selectSysSaleOrderItemGoodsByItemId(item.getItemId());
            if (goodsList != null && goodsList.size() > 0) {
                Long[] ids = goodsList.stream().map(SysSaleOrderItemGoods::getCardId).toArray(Long[]::new);
                if ("1".equals(item.getTemplateType())) { // 充值卡
                    List<SysCard> cardList = sysCardService.selectSysCardByCardIds(ids);
                    for (SysCard card : cardList) {
                        item.getGoodsList().add(new SysCardVo(card));
                    }
                } else if ("2".equals(item.getTemplateType())) { // 登录码
                    List<SysLoginCode> cardList = sysLoginCodeService.selectSysLoginCodeByCardIds(ids);
                    for (SysLoginCode card : cardList) {
                        item.getGoodsList().add(new SysLoginCodeVo(card));
                    }
                }
            }
        }
        return AjaxResult.success(sso);
    }

    /**
     * 新增销售订单
     */
    @PreAuthorize("@ss.hasPermi('sale:saleOrder:add')")
    @Log(title = "销售订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysSaleOrder sysSaleOrder) {
        return toAjax(sysSaleOrderService.insertSysSaleOrder(sysSaleOrder));
    }

    /**
     * 修改销售订单
     */
    @PreAuthorize("@ss.hasPermi('sale:saleOrder:edit')")
    @Log(title = "销售订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysSaleOrder sysSaleOrder) {
        return toAjax(sysSaleOrderService.updateSysSaleOrder(sysSaleOrder));
    }

    /**
     * 删除销售订单
     */
    @PreAuthorize("@ss.hasPermi('sale:saleOrder:remove')")
    @Log(title = "销售订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{orderIds}")
    public AjaxResult remove(@PathVariable Long[] orderIds) {
        return toAjax(sysSaleOrderService.deleteSysSaleOrderByOrderIds(orderIds));
    }

    /**
     * 手动发货
     */
    @PreAuthorize("@ss.hasPermi('sale:saleOrder:query')")
    @GetMapping(value = "/manualDelivery")
    public AjaxResult manualDelivery(String orderNo) {
        if (orderNo == null) {
            throw new ServiceException("订单不存在", 400);
        }
        SysSaleOrder sso = sysSaleOrderService.selectSysSaleOrderByOrderNo(orderNo);
        if (sso == null) {
            throw new ServiceException("订单不存在", 400);
        }
        SaleOrderStatus status = sso.getStatus();
        if (status != SaleOrderStatus.WAIT_PAY && status != SaleOrderStatus.PAID && status != SaleOrderStatus.TRADE_CLOSED) {
            throw new ServiceException("该订单已自动发货或交易已结束，无法再次发货");
        }
        try {
            sso.setStatus(SaleOrderStatus.PAID);
            sysSaleShopService.deliveryGoods(sso);
//            if (StringUtils.isNotBlank(sso.getPayMode())) {
//                sso.setRemark("该订单手工发货，原支付方式：" + sso.getPayMode());
//            }
            sso.setManualDelivery('1'); // 手动发货
            sysSaleOrderService.updateSysSaleOrder(sso);
        } catch (Exception e) {
            sso.setStatus(status);
            sysSaleOrderService.updateSysSaleOrder(sso);
            throw e;
        }
        return AjaxResult.success();
    }
}
