package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.PageVO;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.web.controller.xkt.vo.order.*;
import com.ruoyi.xkt.dto.order.*;
import com.ruoyi.xkt.enums.EPayChannel;
import com.ruoyi.xkt.enums.EPayPage;
import com.ruoyi.xkt.manager.PaymentManager;
import com.ruoyi.xkt.service.IStoreOrderService;
import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        StoreOrderPayRespVO respVO = new StoreOrderPayRespVO(result.getOrderExt().getOrder().getId(),
                result.getPayRtnStr());
        return success(respVO);
    }

    @PreAuthorize("@ss.hasPermi('system:order:edit')")
    @Log(title = "订单", businessType = BusinessType.UPDATE)
    @ApiOperation("修改订单")
    @PutMapping("edit")
    public R<Long> edit(@Valid @RequestBody StoreOrderUpdateReqVO vo) {
        StoreOrderUpdateDTO dto = BeanUtil.toBean(vo, StoreOrderUpdateDTO.class);
        dto.setOrderUserId(SecurityUtils.getUserId());
        StoreOrderExt result = storeOrderService.modifyOrder(dto);
        //TODO 非下单人，不允许修改
        return success(result.getOrder().getId());
    }

    @PreAuthorize("@ss.hasPermi('system:order:add')")
    @Log(title = "订单", businessType = BusinessType.OTHER)
    @ApiOperation("支付订单")
    @PostMapping("pay")
    public R<StoreOrderPayRespVO> pay(@Valid @RequestBody StoreOrderPayReqVO vo) {
        EPayChannel payChannel = EPayChannel.of(vo.getPayChannel());
        PaymentManager paymentManager = getPaymentManager(payChannel);
        //订单支付状态->支付中
        StoreOrderExt orderExt = storeOrderService.preparePayOrder(vo.getStoreOrderId(), payChannel);
        //调用支付
        String rtnStr = paymentManager.payStoreOrder(orderExt, EPayPage.of(vo.getPayFrom()));
        StoreOrderPayRespVO respVO = new StoreOrderPayRespVO(vo.getStoreOrderId(), rtnStr);
        return success(respVO);
    }

    @PreAuthorize("@ss.hasPermi('system:order:edit')")
    @Log(title = "订单", businessType = BusinessType.UPDATE)
    @ApiOperation("取消订单")
    @PutMapping("cancel")
    public R cancel(@Valid @RequestBody StoreOrderCancelReqVO vo) {
        OrderOptDTO dto = OrderOptDTO.builder()
                .storeOrderId(vo.getStoreOrderId())
                .operatorId(SecurityUtils.getUserId())
                .build();
        storeOrderService.cancelOrder(dto);
        //TODO 非下单人，不允许取消
        return success();
    }

    @PreAuthorize("@ss.hasPermi('system:order:query')")
    @ApiOperation(value = "订单详情")
    @GetMapping(value = "/{id}")
    public R<StoreOrderInfoVO> getInfo(@PathVariable("id") Long id) {
        StoreOrderInfoDTO infoDTO = storeOrderService.getInfo(id);
        //TODO 非下单人，非所属档口不允许查询
        return success(BeanUtil.toBean(infoDTO, StoreOrderInfoVO.class));
    }


    @PreAuthorize("@ss.hasPermi('system:order:list')")
    @ApiOperation(value = "订单分页查询")
    @PostMapping("/page")
    public R<PageVO<StoreOrderPageItemVO>> page(@Validated @RequestBody StoreOrderQueryVO vo) {
        StoreOrderQueryDTO queryDTO = BeanUtil.toBean(vo, StoreOrderQueryDTO.class);
        if (1 == vo.getSrcPage()) {
            queryDTO.setOrderUserId(SecurityUtils.getUserId());
        } else {
            //TODO 当前档口
        }
        Page<StoreOrderPageItemDTO> pageDTO = storeOrderService.page(queryDTO);
        return success(PageVO.of(pageDTO, StoreOrderPageItemVO.class));
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
