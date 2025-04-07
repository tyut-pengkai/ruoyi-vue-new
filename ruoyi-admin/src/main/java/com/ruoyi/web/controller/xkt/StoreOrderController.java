package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.web.controller.xkt.vo.order.StoreOrderAddReqVO;
import com.ruoyi.web.controller.xkt.vo.order.StoreOrderAddRespVO;
import com.ruoyi.xkt.dto.order.StoreOrderAddDTO;
import com.ruoyi.xkt.dto.order.StoreOrderInfo;
import com.ruoyi.xkt.enums.EPayChannel;
import com.ruoyi.xkt.enums.EPayFrom;
import com.ruoyi.xkt.manager.PaymentManager;
import com.ruoyi.xkt.service.IStoreOrderService;
import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 创建订单
     */
//    @PreAuthorize("@ss.hasPermi('system:order:add')")
    @Log(title = "订单", businessType = BusinessType.INSERT)
    @PostMapping
    public R<StoreOrderAddRespVO> create(@RequestBody StoreOrderAddReqVO vo) {
        StoreOrderAddDTO dto = BeanUtil.toBean(vo, StoreOrderAddDTO.class);
        dto.setOrderUserId(SecurityUtils.getUserId());
        //初始化订单
        StoreOrderInfo orderInfo = storeOrderService.createOrder(dto);
        //发起支付
        PaymentManager paymentManager = getPaymentManager(EPayChannel.of(vo.getPayChannel()));
        String rtnStr = paymentManager.payForOrder(orderInfo.getOrder().getId(), EPayFrom.of(vo.getPayFrom()));
        //返回信息
        StoreOrderAddRespVO respVO = new StoreOrderAddRespVO(orderInfo.getOrder().getId(),
                orderInfo.getOrder().getOrderNo(), rtnStr);
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
