import request from '@/utils/request'

// 创建支付订单
export function createOrder(data) {
  return request({
    url: '/payment/order/create',
    method: 'post',
    data: data
  })
}

// 查询支付订单列表
export function listOrder(query) {
  return request({
    url: '/payment/order/list',
    method: 'get',
    params: query
  })
}

// 查询支付订单详细
export function getOrder(orderId) {
  return request({
    url: '/payment/order/' + orderId,
    method: 'get'
  })
}

// 新增支付订单
export function addOrder(data) {
  return request({
    url: '/payment/order',
    method: 'post',
    data: data
  })
}

// 修改支付订单
export function updateOrder(data) {
  return request({
    url: '/payment/order',
    method: 'put',
    data: data
  })
}

// 删除支付订单
export function delOrder(orderId) {
  return request({
    url: '/payment/order/' + orderId,
    method: 'delete'
  })
}

// 模拟支付
export function mockPay(orderNo) {
  return request({
    url: '/payment/order/mock-pay/' + orderNo,
    method: 'post'
  })
}

// 取消订单
export function cancelOrder(orderId) {
  return request({
    url: '/payment/order/cancel/' + orderId,
    method: 'post'
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

// 查询支付状态
export function getPaymentStatus(orderNo) {
  return request({
    url: '/payment/api/status/' + orderNo,
    method: 'get'
  })
}
