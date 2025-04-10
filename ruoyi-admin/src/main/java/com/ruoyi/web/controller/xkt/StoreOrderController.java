package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.web.controller.xkt.vo.order.StoreOrderAddReqVO;
import com.ruoyi.web.controller.xkt.vo.order.StoreOrderPayReqVO;
import com.ruoyi.web.controller.xkt.vo.order.StoreOrderPayRespVO;
import com.ruoyi.web.controller.xkt.vo.order.StoreOrderUpdateReqVO;
import com.ruoyi.xkt.dto.order.StoreOrderAddDTO;
import com.ruoyi.xkt.dto.order.StoreOrderAddResult;
import com.ruoyi.xkt.dto.order.StoreOrderInfo;
import com.ruoyi.xkt.dto.order.StoreOrderUpdateDTO;
import com.ruoyi.xkt.enums.EPayChannel;
import com.ruoyi.xkt.enums.EPayPage;
import com.ruoyi.xkt.manager.PaymentManager;
import com.ruoyi.xkt.service.IStoreOrderService;
import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

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
    private List<PaymentManager> paymentManagers;

    @PreAuthorize("@ss.hasPermi('system:order:add')")
    @Log(title = "订单", businessType = BusinessType.INSERT)
    @ApiOperation("创建订单")
    @PostMapping("create")
    public R<StoreOrderPayRespVO> create(@Valid @RequestBody StoreOrderAddReqVO vo) {
        StoreOrderAddDTO dto = BeanUtil.toBean(vo, StoreOrderAddDTO.class);
        dto.setOrderUserId(SecurityUtils.getUserId());
        //创建订单并根据参数决定是否发起支付
        StoreOrderAddResult result = storeOrderService.createOrder(dto, vo.getBeginPay(),
                EPayChannel.of(vo.getPayChannel()), EPayPage.of(vo.getPayFrom()));
        //返回信息
        StoreOrderPayRespVO respVO = new StoreOrderPayRespVO(result.getOrderInfo().getOrder().getId(),
                result.getPayRtnStr());
        return success(respVO);
    }

    @PreAuthorize("@ss.hasPermi('system:order:add')")
    @Log(title = "订单", businessType = BusinessType.UPDATE)
    @ApiOperation("修改订单")
    @PostMapping("modify")
    public R<Long> modify(@Valid @RequestBody StoreOrderUpdateReqVO vo) {
        StoreOrderUpdateDTO dto = BeanUtil.toBean(vo, StoreOrderUpdateDTO.class);
        dto.setOrderUserId(SecurityUtils.getUserId());
        StoreOrderInfo result = storeOrderService.modifyOrder(dto);
        return success(result.getOrder().getId());
    }

    @PreAuthorize("@ss.hasPermi('system:order:add')")
    @Log(title = "订单", businessType = BusinessType.OTHER)
    @ApiOperation("支付订单")
    @PostMapping("pay")
    public R<StoreOrderPayRespVO> pay(@Valid @RequestBody StoreOrderPayReqVO vo) {
        PaymentManager paymentManager = getPaymentManager(EPayChannel.of(vo.getPayChannel()));
        //订单支付状态->支付中
        StoreOrderInfo storeOrderInfo = storeOrderService.preparePayOrder(vo.getStoreOrderId());
        //调用支付
        String rtnStr = paymentManager.payOrder(storeOrderInfo, EPayPage.of(vo.getPayFrom()));
        StoreOrderPayRespVO respVO = new StoreOrderPayRespVO(vo.getStoreOrderId(), rtnStr);
        return success(respVO);
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
