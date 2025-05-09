package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.SimpleEntity;
import com.ruoyi.common.core.page.PageVO;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.web.controller.xkt.vo.order.*;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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

    @Log(title = "订单", businessType = BusinessType.UPDATE)
    @ApiOperation("修改订单")
    @PostMapping("edit")
    public R<Long> edit(@Valid @RequestBody StoreOrderUpdateReqVO vo) {
        StoreOrderUpdateDTO dto = BeanUtil.toBean(vo, StoreOrderUpdateDTO.class);
        dto.setOrderUserId(SecurityUtils.getUserId());
        StoreOrderExt result = storeOrderService.modifyOrder(dto);
        //TODO 非下单人，不允许修改
        return success(result.getOrder().getId());
    }

    @Log(title = "订单", businessType = BusinessType.OTHER)
    @ApiOperation("支付订单")
    @PostMapping("pay")
    public R<StoreOrderPayRespVO> pay(@Valid @RequestBody StoreOrderPayReqVO vo) {
        EPayChannel payChannel = EPayChannel.of(vo.getPayChannel());
        PaymentManager paymentManager = getPaymentManager(payChannel);
        //订单支付状态->支付中
        StoreOrderExt orderExt = storeOrderService.preparePayOrder(vo.getStoreOrderId(), payChannel);
        //调用支付
        String rtnStr = paymentManager.payStoreOrder(orderExt, EPayPage.of(vo.getPayPage()));
        StoreOrderPayRespVO respVO = new StoreOrderPayRespVO(vo.getStoreOrderId(), rtnStr);
        return success(respVO);
    }

    @Log(title = "订单", businessType = BusinessType.UPDATE)
    @ApiOperation("取消订单")
    @PostMapping("cancel")
    public R cancel(@Valid @RequestBody StoreOrderCancelReqVO vo) {
        StoreOrderCancelDTO dto = StoreOrderCancelDTO.builder()
                .storeOrderId(vo.getStoreOrderId())
                .operatorId(SecurityUtils.getUserId())
                .build();
        storeOrderService.cancelOrder(dto);
        //TODO 非下单人，不允许取消
        return success();
    }

    @ApiOperation(value = "订单详情")
    @GetMapping(value = "/{id}")
    public R<StoreOrderInfoVO> getInfo(@PathVariable("id") Long id) {
        StoreOrderInfoDTO infoDTO = storeOrderService.getInfo(id);
        //TODO 非下单人，非所属档口不允许查询
        return success(BeanUtil.toBean(infoDTO, StoreOrderInfoVO.class));
    }


    @ApiOperation(value = "订单分页查询")
    @PostMapping("/page")
    @ResponseHeader
    public R<PageVO<StoreOrderPageItemVO>> page(@Validated @RequestBody StoreOrderQueryVO vo) {
        StoreOrderQueryDTO queryDTO = BeanUtil.toBean(vo, StoreOrderQueryDTO.class);
        if (1 == vo.getSrcPage()) {
            queryDTO.setOrderUserId(SecurityUtils.getUserId());
        } else {
            queryDTO.setStoreId(SecurityUtils.getStoreId());
        }
        Page<StoreOrderPageItemDTO> pageDTO = storeOrderService.page(queryDTO);
        return success(PageVO.of(pageDTO, StoreOrderPageItemVO.class));
    }

    @Log(title = "订单", businessType = BusinessType.OTHER)
    @ApiOperation("发货-平台物流")
    @PostMapping("ship-platform")
    public R<List<StoreOrderShipRespVO>> shipByPlatform(@Valid @RequestBody StoreOrderShipByPlatformReqVO vo) {
        //TODO 权限
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

    @Log(title = "订单", businessType = BusinessType.OTHER)
    @ApiOperation("发货-档口物流")
    @PostMapping("ship-store")
    public R<List<StoreOrderShipRespVO>> shipByStore(@Valid @RequestBody StoreOrderShipByStoreReqVO vo) {
        //TODO 权限
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

    @Log(title = "订单", businessType = BusinessType.OTHER)
    @ApiOperation("打印面单")
    @PostMapping("print")
    public R<List<StoreOrderPrintRespVO>> print(@Valid @RequestBody StoreOrderPrintReqVO vo) {
        //TODO 权限
        List<ExpressPrintDTO> dtoList = storeOrderService.printOrder(vo.getStoreOrderDetailIds());
        List<StoreOrderPrintRespVO> rtnList = dtoList.stream().map(o -> {
            StoreOrderPrintRespVO rtn = new StoreOrderPrintRespVO();
            rtn.setExpressWaybillNo(o.getWaybillNo());
            rtn.setResult(o.getResult());
            return rtn;
        }).collect(Collectors.toList());
        return success(rtnList);
    }

    @Log(title = "订单", businessType = BusinessType.OTHER)
    @ApiOperation("确认收货")
    @PostMapping("receipt")
    public R receipt(@Valid @RequestBody StoreOrderReceiptReqVO vo) {
        //TODO 权限
        storeOrderService.receiptOrder(vo.getStoreOrderId(), SecurityUtils.getUserId());
        return success();
    }

    @Log(title = "订单", businessType = BusinessType.OTHER)
    @ApiOperation("申请售后（创建售后订单）")
    @PostMapping("refund/apply")
    public R<Long> applyRefund(@Valid @RequestBody StoreOrderAfterSaleReqVO vo) {
        //TODO 权限
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

    @Log(title = "订单", businessType = BusinessType.OTHER)
    @ApiOperation("确认退款")
    @PostMapping("refund/confirm")
    public R confirmRefund(@Valid @RequestBody StoreOrderRefundConfirmVO vo) {
        //TODO 权限
        StoreOrderRefundConfirmDTO dto = BeanUtil.toBean(vo, StoreOrderRefundConfirmDTO.class);
        dto.setOperatorId(SecurityUtils.getUserId());
        //售后状态->售后完成，支付状态->支付中，创建收款单
        StoreOrderRefund storeOrderRefund = storeOrderService.prepareRefundOrder(dto);
        //三方退款
        PaymentManager paymentManager = getPaymentManager(EPayChannel.of(storeOrderRefund.getRefundOrder().getPayChannel()));
        paymentManager.refundStoreOrder(storeOrderRefund);
        //支付状态->已支付，收款单到账
        storeOrderService.refundSuccess(storeOrderRefund.getRefundOrder().getId(),
                storeOrderRefund.getRefundOrderDetails().stream().map(SimpleEntity::getId).collect(Collectors.toList()),
                SecurityUtils.getUserId());
        return success();
    }

    @Log(title = "订单", businessType = BusinessType.OTHER)
    @ApiOperation("拒绝退款")
    @PostMapping("refund/reject")
    public R rejectRefund(@Valid @RequestBody StoreOrderRefundRejectVO vo) {
        //TODO 权限
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
