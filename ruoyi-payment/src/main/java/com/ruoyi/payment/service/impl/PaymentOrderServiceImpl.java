package com.ruoyi.payment.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.payment.domain.PaymentPackage;
import com.ruoyi.payment.mapper.PaymentPackageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.payment.mapper.PaymentOrderMapper;
import com.ruoyi.payment.domain.PaymentOrder;
import com.ruoyi.payment.service.IPaymentOrderService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.device.domain.DeviceInfo;
import com.ruoyi.device.domain.DeviceHour;
import com.ruoyi.device.domain.DeviceUser;
import com.ruoyi.device.mapper.DeviceHourMapper;
import com.ruoyi.device.mapper.DeviceUserMapper;
import com.ruoyi.device.service.IDeviceInfoService;
import com.ruoyi.payment.domain.dto.CreateOrderRequest;

import java.util.Date;
import java.util.UUID;

/**
 * 支付订单Service业务层处理
 * 
 * @author auto
 * @date 2025-06-24
 */
@Service
public class PaymentOrderServiceImpl implements IPaymentOrderService 
{
    @Autowired
    private PaymentOrderMapper paymentOrderMapper;

    @Autowired
    private PaymentPackageMapper paymentPackageMapper;

    @Autowired
    private IDeviceInfoService deviceInfoService;

    @Autowired
    private DeviceUserMapper deviceUserMapper;

    @Autowired
    private DeviceHourMapper deviceHourMapper;

    /**
     * 查询支付订单
     * 
     * @param orderId 支付订单主键
     * @return 支付订单
     */
    @Override
    public PaymentOrder selectPaymentOrderByOrderId(Long orderId)
    {
        return paymentOrderMapper.selectPaymentOrderByOrderId(orderId);
    }

    /**
     * 查询支付订单列表
     * 
     * @param paymentOrder 支付订单
     * @return 支付订单
     */
    @Override
    public List<PaymentOrder> selectPaymentOrderList(PaymentOrder paymentOrder)
    {
        return paymentOrderMapper.selectPaymentOrderList(paymentOrder);
    }

    /**
     * 新增支付订单
     * 
     * @param paymentOrder 支付订单
     * @return 结果
     */
    @Override
    public int insertPaymentOrder(PaymentOrder paymentOrder)
    {
        paymentOrder.setCreateTime(DateUtils.getNowDate());
        return paymentOrderMapper.insertPaymentOrder(paymentOrder);
    }

    /**
     * 修改支付订单
     * 
     * @param paymentOrder 支付订单
     * @return 结果
     */
    @Override
    public int updatePaymentOrder(PaymentOrder paymentOrder)
    {
        paymentOrder.setUpdateTime(DateUtils.getNowDate());
        return paymentOrderMapper.updatePaymentOrder(paymentOrder);
    }

    /**
     * 批量删除支付订单
     * 
     * @param orderIds 需要删除的支付订单主键
     * @return 结果
     */
    @Override
    public int deletePaymentOrderByOrderIds(Long[] orderIds)
    {
        return paymentOrderMapper.deletePaymentOrderByOrderIds(orderIds);
    }

    /**
     * 删除支付订单信息
     * 
     * @param orderId 支付订单主键
     * @return 结果
     */
    @Override
    public int deletePaymentOrderByOrderId(Long orderId)
    {
        return paymentOrderMapper.deletePaymentOrderByOrderId(orderId);
    }

    /**
     * 创建支付订单
     *
     * @param request 创建订单请求
     * @return 支付订单
     */
    @Override
    @Transactional
    public PaymentOrder createOrder(CreateOrderRequest request) {
        if (request.getPackageId() == null || request.getDeviceId() == null) {
            throw new ServiceException("参数错误，套餐ID和设备ID不能为空");
        }

        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();

        // 1. 验证套餐
        PaymentPackage paymentPackage = paymentPackageMapper.selectPaymentPackageByPackageId(request.getPackageId());
        if (paymentPackage == null) {
            throw new ServiceException("支付套餐不存在");
        }

        // 2. 验证设备
        DeviceInfo deviceInfo = deviceInfoService.selectDeviceInfoByDeviceId(request.getDeviceId());
        if (deviceInfo == null) {
            throw new ServiceException("设备不存在");
        }

        // 验证设备是否属于当前用户
        DeviceUser deviceUser = deviceUserMapper.selectDeviceUserByDeviceId(request.getDeviceId());
        if (deviceUser == null || !deviceUser.getUserId().equals(user.getUserId())) {
            throw new ServiceException("只能为自己的设备创建订单");
        }

        // 3. 创建订单
        PaymentOrder order = new PaymentOrder();
        order.setOrderNo(UUID.randomUUID().toString().replace("-", ""));
        order.setUserId(user.getUserId());
        order.setUserName(user.getUserName());
        order.setPackageId(paymentPackage.getPackageId());
        order.setPackageName(paymentPackage.getName());
        order.setPackageHours(paymentPackage.getHours().intValue());
        order.setPackageFreeHours(paymentPackage.getFreeHours());
        order.setAmount(paymentPackage.getPrice());
        order.setCurrency(paymentPackage.getCurrency());
        order.setStatus("0"); // 0=待支付
        order.setDeviceId(deviceInfo.getDeviceId());
        order.setDeviceName(deviceInfo.getDeviceName());
        order.setCreateBy(user.getUserName());
        order.setDelFlag("0");

        paymentOrderMapper.insertPaymentOrder(order);

        return order;
    }

    /**
     * 处理支付成功逻辑
     *
     * @param orderNo 订单号
     * @return 结果
     */
    @Override
    @Transactional
    public boolean processPaymentSuccess(String orderNo)
    {
        PaymentOrder order = paymentOrderMapper.selectPaymentOrderByOrderNo(orderNo);

        // 1. 验证订单
        if (order == null) {
            throw new ServiceException("订单不存在");
        }
        if (!"0".equals(order.getStatus())) {
            // 订单状态不为"待支付"，可能已处理，直接返回成功，防止重复处理
            return true;
        }

        // 2. 更新订单状态
        PaymentOrder updateOrder = new PaymentOrder();
        updateOrder.setOrderId(order.getOrderId());
        updateOrder.setStatus("1"); // 1=已支付
        updateOrder.setPayTime(DateUtils.getNowDate());
        paymentOrderMapper.updatePaymentOrder(updateOrder);

        // 3. 增加设备Pro时长
        DeviceHour deviceHour = deviceHourMapper.selectDeviceHourByDeviceId(order.getDeviceId());
        if (deviceHour == null) {
            throw new ServiceException("订单关联的设备时长记录不存在,未初始化");
        }

        DeviceHour updateDeviceHour = new DeviceHour();
        updateDeviceHour.setDeviceId(deviceHour.getDeviceId());
        boolean needUpdate = false;

        // 增加Pro时长
        Integer proHours = order.getPackageHours();
        if (proHours != null && proHours > 0) {
            long currentProHours = deviceHour.getAvailProHours() != null ? deviceHour.getAvailProHours() : 0L;
            updateDeviceHour.setAvailProHours(currentProHours + proHours);
            needUpdate = true;
        }

        // 增加免费时长
        Integer freeHours = order.getPackageFreeHours();
        if (freeHours != null && freeHours > 0) {
            long currentFreeHours = deviceHour.getAvailFreeHours() != null ? deviceHour.getAvailFreeHours() : 0L;
            updateDeviceHour.setAvailFreeHours(currentFreeHours + freeHours);
            needUpdate = true;
        }

        if (needUpdate) {
            deviceHourMapper.updateDeviceHour(updateDeviceHour);
        }
        
        return true;
    }

    @Override
    @Transactional
    public int cancelOrder(Long orderId) {
        PaymentOrder order = paymentOrderMapper.selectPaymentOrderByOrderId(orderId);
        if (order == null) {
            throw new ServiceException("订单不存在");
        }

        // 只有待支付的订单可以被取消
        if (!"0".equals(order.getStatus())) {
            throw new ServiceException("只有待支付的订单才能被取消");
        }

        // 校验权限：非管理员只能取消自己的订单
        if(!SecurityUtils.isAdmin(SecurityUtils.getUserId())) {
            if(!order.getUserId().equals(SecurityUtils.getUserId())) {
                throw new ServiceException("无权操作");
            }
        }
        
        PaymentOrder updateOrder = new PaymentOrder();
        updateOrder.setOrderId(orderId);
        updateOrder.setStatus("2"); // 2=已取消
        return paymentOrderMapper.updatePaymentOrder(updateOrder);
    }

    /**
     * 根据订单号查询订单
     *
     * @param orderNo 订单号
     * @return 订单信息
     */
    @Override
    public PaymentOrder getOrderByOrderNo(String orderNo)
    {
        return paymentOrderMapper.selectPaymentOrderByOrderNo(orderNo);
    }
}
