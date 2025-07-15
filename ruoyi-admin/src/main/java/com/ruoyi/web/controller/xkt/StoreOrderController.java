package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.Page;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.SimpleEntity;
import com.ruoyi.common.core.page.PageVO;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.notice.fs.FsNotice;
import com.ruoyi.web.controller.xkt.vo.order.*;
import com.ruoyi.xkt.domain.StoreOrder;
import com.ruoyi.xkt.domain.StoreOrderDetail;
import com.ruoyi.xkt.dto.express.ExpressCancelReqDTO;
import com.ruoyi.xkt.dto.express.ExpressInterceptReqDTO;
import com.ruoyi.xkt.dto.express.ExpressPrintDTO;
import com.ruoyi.xkt.dto.order.*;
import com.ruoyi.xkt.enums.EExpressStatus;
import com.ruoyi.xkt.enums.EPayChannel;
import com.ruoyi.xkt.enums.EPayPage;
import com.ruoyi.xkt.manager.ExpressManager;
import com.ruoyi.xkt.manager.PaymentManager;
import com.ruoyi.xkt.service.IExpressService;
import com.ruoyi.xkt.service.IStoreOrderService;
import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ResponseHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author liangyq
 * @date 2025-04-03 14:18
 */
@Api(tags = "订单")
@RestController
@RequestMapping("/rest/v1/order")
public class StoreOrderController extends XktBaseController {

    @Autowired
    private IStoreOrderService storeOrderService;
    @Autowired
    private IExpressService expressService;
    @Autowired
    private List<PaymentManager> paymentManagers;
    @Autowired
    private FsNotice fsNotice;
    @Autowired
    private RedisCache redisCache;

    @PreAuthorize("@ss.hasRole('seller')")
    @Log(title = "订单", businessType = BusinessType.INSERT)
    @ApiOperation("创建订单")
    @PostMapping("create")
    public R<StoreOrderPayRespVO> create(@Valid @RequestBody StoreOrderAddReqVO vo) {
        StoreOrderAddDTO dto = BeanUtil.toBean(vo, StoreOrderAddDTO.class);
        dto.setOrderUserId(SecurityUtils.getUserId());
        //创建订单并根据参数决定是否发起支付
        StoreOrderAddResult result = storeOrderService.createOrder(dto, vo.getBeginPay(),
                EPayChannel.of(vo.getPayChannel()), EPayPage.of(vo.getPayPage()));
        //返回信息
        StoreOrderPayRespVO respVO = new StoreOrderPayRespVO(result.getOrderExt().getOrder().getId(),
                result.getPayRtnStr());
        return success(respVO);
    }

    @PreAuthorize("@ss.hasRole('seller')")
    @Log(title = "订单", businessType = BusinessType.UPDATE)
    @ApiOperation("修改订单")
    @PostMapping("edit")
    public R<Long> edit(@Valid @RequestBody StoreOrderUpdateReqVO vo) {
        StoreOrderUpdateDTO dto = BeanUtil.toBean(vo, StoreOrderUpdateDTO.class);
        dto.setOrderUserId(SecurityUtils.getUserId());
        //仅本人可修改
        storeOrderService.checkOrderUser(dto.getId(), dto.getOrderUserId());
        StoreOrderExt result = storeOrderService.modifyOrder(dto);
        return success(result.getOrder().getId());
    }

    @PreAuthorize("@ss.hasRole('seller')")
    @Log(title = "订单", businessType = BusinessType.OTHER)
    @ApiOperation("支付订单")
    @PostMapping("pay")
    public R<StoreOrderPayRespVO> pay(@Valid @RequestBody StoreOrderPayReqVO vo) {
        //仅本人可支付
        storeOrderService.checkOrderUser(vo.getStoreOrderId(), SecurityUtils.getUserId());
        EPayChannel payChannel = EPayChannel.of(vo.getPayChannel());
        PaymentManager paymentManager = getPaymentManager(payChannel);
        //订单支付状态->支付中
        StoreOrderExt orderExt = storeOrderService.preparePayOrder(vo.getStoreOrderId(), payChannel);
        //调用支付
        String rtnStr = paymentManager.payStoreOrder(orderExt, EPayPage.of(vo.getPayPage()));
        StoreOrderPayRespVO respVO = new StoreOrderPayRespVO(vo.getStoreOrderId(), rtnStr);
        return success(respVO);
    }

    @PreAuthorize("@ss.hasRole('seller')")
    @Log(title = "订单", businessType = BusinessType.UPDATE)
    @ApiOperation("取消订单")
    @PostMapping("cancel")
    public R cancel(@Valid @RequestBody StoreOrderCancelReqVO vo) {
        StoreOrderCancelDTO dto = StoreOrderCancelDTO.builder()
                .storeOrderId(vo.getStoreOrderId())
                .operatorId(SecurityUtils.getUserId())
                .build();
        //仅本人可取消
        storeOrderService.checkOrderUser(dto.getStoreOrderId(), dto.getOperatorId());
        storeOrderService.cancelOrder(dto);
        return success();
    }

    @ApiOperation(value = "订单详情")
    @GetMapping(value = "/{id}")
    public R<StoreOrderInfoVO> getInfo(@PathVariable("id") Long id) {
        StoreOrderInfoDTO infoDTO = storeOrderService.getInfo(id);
        if (SecurityUtils.isAdmin()
                || Objects.equals(infoDTO.getOrderUserId(), SecurityUtils.getUserId())
                || Objects.equals(infoDTO.getStoreId(), SecurityUtils.getStoreId())) {
            return success(BeanUtil.toBean(infoDTO, StoreOrderInfoVO.class));
        }
        //没有订单权限
        return success();
    }


    @PreAuthorize("@ss.hasAnyRoles('store,seller')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "订单分页查询")
    @PostMapping("/page")
    @ResponseHeader
    public R<PageVO<StoreOrderPageItemVO>> page(@Validated @RequestBody StoreOrderQueryVO vo) {
        StoreOrderQueryDTO queryDTO = BeanUtil.toBean(vo, StoreOrderQueryDTO.class);
        if (1 == vo.getSrcPage()) {
            queryDTO.setOrderUserId(SecurityUtils.getUserId());
        } else {
            Long storeId = SecurityUtils.getStoreId();
            if (storeId == null && !SecurityUtils.isAdmin()) {
                //没有权限
                return success();
            }
            queryDTO.setStoreId(storeId);
        }
        Page<StoreOrderPageItemDTO> pageDTO = storeOrderService.page(queryDTO);
        return success(PageVO.of(pageDTO, StoreOrderPageItemVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @Log(title = "订单", businessType = BusinessType.OTHER)
    @ApiOperation("发货-平台物流")
    @PostMapping("ship/platform")
    public R<List<StoreOrderShipRespVO>> shipByPlatform(@Valid @RequestBody StoreOrderShipByPlatformReqVO vo) {
        if (!SecurityUtils.isAdmin()) {
            //仅档口可操作
            storeOrderService.checkOrderStore(vo.getStoreOrderId(), SecurityUtils.getStoreId());
        }
        StoreOrderExt orderExt = storeOrderService.shipOrderByPlatform(vo.getStoreOrderId(),
                vo.getStoreOrderDetailIds(), vo.getExpressId(), SecurityUtils.getUserId());
        List<StoreOrderShipRespVO> respList = new ArrayList<>(vo.getStoreOrderDetailIds().size());
        for (StoreOrderDetail detail : orderExt.getOrderDetails()) {
            if (vo.getStoreOrderDetailIds().contains(detail.getId())) {
                respList.add(new StoreOrderShipRespVO(detail.getId(), detail.getExpressWaybillNo()));
            }
        }
        return success(respList);
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @Log(title = "订单", businessType = BusinessType.OTHER)
    @ApiOperation("发货-档口物流")
    @PostMapping("ship/store")
    public R<List<StoreOrderShipRespVO>> shipByStore(@Valid @RequestBody StoreOrderShipByStoreReqVO vo) {
        if (!SecurityUtils.isAdmin()) {
            //仅档口可操作
            storeOrderService.checkOrderStore(vo.getStoreOrderId(), SecurityUtils.getStoreId());
        }
        StoreOrderExt orderExt = storeOrderService.shipOrderByStore(vo.getStoreOrderId(),
                vo.getStoreOrderDetailIds(), vo.getExpressId(), vo.getExpressWaybillNo(), SecurityUtils.getUserId());
        List<StoreOrderShipRespVO> respList = new ArrayList<>(vo.getStoreOrderDetailIds().size());
        for (StoreOrderDetail detail : orderExt.getOrderDetails()) {
            if (vo.getStoreOrderDetailIds().contains(detail.getId())) {
                respList.add(new StoreOrderShipRespVO(detail.getId(), detail.getExpressWaybillNo()));
            }
        }
        return success(respList);
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @Log(title = "订单", businessType = BusinessType.OTHER)
    @ApiOperation("打印面单")
    @PostMapping("ship/print")
    public R<List<StoreOrderPrintRespVO>> print(@Valid @RequestBody StoreOrderPrintReqVO vo) {
        if (!SecurityUtils.isAdmin()) {
            //仅档口可操作
            storeOrderService.checkOrderStore(vo.getStoreOrderDetailIds(), SecurityUtils.getStoreId());
        }
        List<ExpressPrintDTO> dtoList = storeOrderService.printOrder(vo.getStoreOrderDetailIds());
        List<StoreOrderPrintRespVO> rtnList = dtoList.stream().map(o -> {
            StoreOrderPrintRespVO rtn = new StoreOrderPrintRespVO();
            rtn.setExpressWaybillNo(o.getWaybillNo());
            rtn.setResult(o.getResult());
            return rtn;
        }).collect(Collectors.toList());
        return success(rtnList);
    }

    @PreAuthorize("@ss.hasAnyRoles('seller')")
    @Log(title = "订单", businessType = BusinessType.OTHER)
    @ApiOperation("确认收货")
    @PostMapping("receipt")
    public R receipt(@Valid @RequestBody StoreOrderReceiptReqVO vo) {
        //仅本人可操作
        storeOrderService.checkOrderUser(vo.getStoreOrderId(), SecurityUtils.getUserId());
        storeOrderService.receiptOrder(vo.getStoreOrderId(), SecurityUtils.getUserId());
        return success();
    }

    @PreAuthorize("@ss.hasAnyRoles('seller')")
    @Log(title = "订单", businessType = BusinessType.OTHER)
    @ApiOperation("申请售后（创建售后订单）")
    @PostMapping("refund/apply")
    public R<Long> applyRefund(@Valid @RequestBody StoreOrderAfterSaleReqVO vo) {
        //仅本人可操作
        storeOrderService.checkOrderUser(vo.getStoreOrderId(), SecurityUtils.getUserId());
        StoreOrderAfterSaleDTO dto = BeanUtil.toBean(vo, StoreOrderAfterSaleDTO.class);
        dto.setOperatorId(SecurityUtils.getUserId());
        //创建售后订单
        AfterSaleApplyResultDTO afterSaleApplyResult = storeOrderService.createAfterSaleOrder(dto);
        //取消/拦截物流
        //TODO 调用失败如何补偿？
        Set<ExpressSimpleDTO> stopExpresses = afterSaleApplyResult.getNeedStopExpresses();
        for (ExpressSimpleDTO express : stopExpresses) {
            boolean cancelSuccess = true;
            ExpressManager expressManager = expressService.getExpressManager(express.getExpressId());
            if (EExpressStatus.PLACING.getValue().equals(express.getExpressStatus())
                    || EExpressStatus.PLACED.getValue().equals(express.getExpressStatus())) {
                cancelSuccess = expressManager.cancelShip(new ExpressCancelReqDTO(
                        express.getExpressReqNo(),
                        express.getExpressWaybillNo()
                ));
                logger.info("物流订单取消：waybillNo={},result={}", express.getExpressWaybillNo(), cancelSuccess);
            }
            if (!cancelSuccess ||
                    EExpressStatus.PICKED_UP.getValue().equals(express.getExpressStatus())) {
                boolean interceptSuccess = expressManager.interceptShip(new ExpressInterceptReqDTO(
                        express.getExpressReqNo(),
                        express.getExpressWaybillNo()
                ));
                logger.info("物流订单拦截：waybillNo={},result={}", express.getExpressWaybillNo(), interceptSuccess);
            }
        }
        return success(afterSaleApplyResult.getStoreOrderId());
    }

    @PreAuthorize("@ss.hasAnyRoles('seller,store')||@ss.hasSupplierSubRole()")
    @Log(title = "订单", businessType = BusinessType.OTHER)
    @ApiOperation("申请平台介入")
    @PostMapping("platform-involve/apply")
    public R<Long> applyPlatformInvolve(@Valid @RequestBody PlatformInvolveApplyReqVO vo) {
        Long orderId = vo.getStoreOrderId();
        Long userId = SecurityUtils.getUserId();
        Long storeId = SecurityUtils.getStoreId();
        StoreOrder order = storeOrderService.getById(orderId);
        Assert.notNull(order, "订单不存在");
        if (Objects.equals(order.getStoreId(), storeId)
                || Objects.equals(order.getOrderUserId(), userId)) {
            storeOrderService.applyPlatformInvolve(orderId, vo.getPlatformInvolveReason());
            return R.ok(orderId);
        }
        return R.fail("无权限");
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @Log(title = "订单", businessType = BusinessType.OTHER)
    @ApiOperation("平台介入完成（管理员）")
    @PostMapping("platform-involve/complete")
    public R<Long> completePlatformInvolve(@Valid @RequestBody PlatformInvolveCompleteVO vo) {
        storeOrderService.completePlatformInvolve(vo.getStoreOrderId(), vo.getPlatformInvolveResult());
        return R.ok(vo.getStoreOrderId());
    }

    @PreAuthorize("@ss.hasAnyRoles('seller')")
    @Log(title = "订单", businessType = BusinessType.OTHER)
    @ApiOperation("售后完成（用户）")
    @PostMapping("refund/complete")
    public R<Long> completeRefundByUser(@Valid @RequestBody RefundCompleteVO vo) {
        storeOrderService.completeRefundByUser(vo.getStoreOrderId());
        return R.ok(vo.getStoreOrderId());
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @Log(title = "订单", businessType = BusinessType.OTHER)
    @ApiOperation("确认退款")
    @PostMapping("refund/confirm")
    public R confirmRefund(@Valid @RequestBody StoreOrderRefundConfirmVO vo) {
        if (!SecurityUtils.isAdmin()) {
            //仅档口可操作
            storeOrderService.checkOrderStore(vo.getStoreOrderId(), SecurityUtils.getStoreId());
        }
        StoreOrderRefundConfirmDTO dto = BeanUtil.toBean(vo, StoreOrderRefundConfirmDTO.class);
        dto.setOperatorId(SecurityUtils.getUserId());
        //支付宝接口要求：同一笔交易的退款至少间隔3s后发起
        String markKey = CacheConstants.STORE_ORDER_REFUND_PROCESSING_MARK + vo.getStoreOrderId();
        boolean less3s = redisCache.hasKey(markKey);
        if (less3s) {
            throw new ServiceException("同一订单确认退款操作至少间隔3s后发起");
        }
        //售后状态->售后完成，支付状态->支付中，创建收款单
        StoreOrderRefund storeOrderRefund = storeOrderService.prepareRefundOrder(dto);
        //三方退款
        PaymentManager paymentManager = getPaymentManager(EPayChannel.of(storeOrderRefund.getRefundOrder().getPayChannel()));
        boolean success = paymentManager.refundStoreOrder(storeOrderRefund);
        //标记
        redisCache.setCacheObject(markKey, 1, 3, TimeUnit.SECONDS);
        if (success) {
            //支付状态->已支付，收款单到账
            storeOrderService.refundSuccess(storeOrderRefund.getRefundOrder().getId(),
                    storeOrderRefund.getRefundOrderDetails().stream().map(SimpleEntity::getId).collect(Collectors.toList()),
                    SecurityUtils.getUserId());
        } else {
            fsNotice.sendMsg2DefaultChat("确认退款失败", "参数: " + JSON.toJSONString(storeOrderRefund));
        }
        return success();
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @Log(title = "订单", businessType = BusinessType.OTHER)
    @ApiOperation("拒绝退款")
    @PostMapping("refund/reject")
    public R rejectRefund(@Valid @RequestBody StoreOrderRefundRejectVO vo) {
        if (!SecurityUtils.isAdmin()) {
            //仅档口可操作
            storeOrderService.checkOrderStore(vo.getStoreOrderId(), SecurityUtils.getStoreId());
        }
        StoreOrderRefundRejectDTO dto = BeanUtil.toBean(vo, StoreOrderRefundRejectDTO.class);
        dto.setOperatorId(SecurityUtils.getUserId());
        storeOrderService.rejectRefundOrder(dto);
        return success();
    }

    /**
     * 根据支付渠道匹配支付类
     *
     * @param payChannel
     * @return
     */
    private PaymentManager getPaymentManager(EPayChannel payChannel) {
        Assert.notNull(payChannel);
        for (PaymentManager paymentManager : paymentManagers) {
            if (paymentManager.channel() == payChannel) {
                return paymentManager;
            }
        }
        throw new ServiceException("未知支付渠道");
    }
}
