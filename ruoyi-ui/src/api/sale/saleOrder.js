import request from '@/utils/request'

// 查询销售订单列表
export function listSaleOrder(query) {
  return request({
    url: '/sale/saleOrder/list',
    method: 'get',
    params: query
  })
}

// 查询销售订单详细
export function getSaleOrder(orderId) {
  return request({
    url: '/sale/saleOrder/' + orderId,
    method: 'get'
  })
}

// 查询销售订单列表
export function listSaleOrderSelf(query) {
  return request({
    url: '/sale/saleOrder/self/list',
    method: 'get',
    params: query
  })
}

// 查询销售订单详细
export function getSaleOrderSelf(orderId) {
  return request({
    url: '/sale/saleOrder/self/' + orderId,
    method: 'get'
  })
}

// 新增销售订单
export function addSaleOrder(data) {
  return request({
    url: '/sale/saleOrder',
    method: 'post',
    data: data
  })
}

// 修改销售订单
export function updateSaleOrder(data) {
  return request({
    url: '/sale/saleOrder',
    method: 'put',
    data: data
  })
}

// 删除销售订单
export function delSaleOrder(orderId) {
  return request({
    url: '/sale/saleOrder/' + orderId,
    method: 'delete'
  })
}

// 手动发货
export function manualDelivery(query) {
  return request({
    url: '/sale/saleOrder/manualDelivery',
    method: 'get',
    params: query
  })
}
