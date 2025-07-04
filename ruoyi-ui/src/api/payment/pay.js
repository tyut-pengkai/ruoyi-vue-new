import request from '@/utils/request'

// 获取订单信息
export function getOrder(orderNo) {
  return request({
    url: '/payment/order/getOrder/' + orderNo,
    method: 'get'
  })
}

// 获取支付回调结果
export function getPaymentCallback(type, paymentMethod,params) {
  return request({
    url: `/payment/api/callback/${type}/${paymentMethod}`,
    method: 'get',
    params: params

  })
}

// 发起支付
export function processPayment(data) {
  return request({
    url: '/payment/api/process',
    method: 'post',
    data: data
  })
}

// 查询支付状态
export function getPaymentStatus(orderNo) {
  return request({
    url: '/payment/api/status/' + orderNo,
    method: 'get'
  })
}

// 处理支付回调
export function handlePaymentCallback(params) {
  return request({
    url: '/payment/api/callback',
    method: 'get',
    params: params
  })
} 