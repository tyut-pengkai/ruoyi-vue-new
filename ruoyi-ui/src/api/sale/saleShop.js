import request from '@/utils/request'

// 查询软件列表
export function listApp(data) {
  return request({
    url: '/sale/shop/appList',
    method: 'get',
    params: data
  })
}

// 查询卡类列表
export function listCategory(data) {
  return request({
    url: '/sale/shop/listCategory',
    method: 'get',
    params: data
  })
}

// 检查库存
export function checkStock(data) {
  return request({
    url: '/sale/shop/checkStock',
    method: 'post',
    data: data
  })
}

// 创建销售订单
export function createSaleOrder(data) {
  return request({
    url: '/sale/shop/createSaleOrder',
    method: 'post',
    data: data
  })
}

// 创建充值订单
export function createChargeOrder(data) {
  return request({
    url: '/sale/shop/createChargeOrder',
    method: 'post',
    data: data
  })
}

// 支付成功
export function notify(data) {
  return request({
    url: '/sale/shop/notify',
    method: 'get',
    params: data
  })
}

// 获取卡密
export function getCardList(data) {
  return request({
    url: '/sale/shop/getCardList',
    method: 'get',
    params: data
  })
}

// 查询销售订单列表，查询订单调用
export function querySaleOrderByContact(query) {
  return request({
    url: '/sale/shop/querySaleOrderByContact',
    method: 'get',
    params: query
  })
}

// 查询销售订单列表，查询订单调用
export function fetchGoods(query) {
  return request({
    url: '/sale/shop/fetchGoods',
    method: 'get',
    params: query
  })
}

// 支付成功
export function paySaleOrder(data) {
  return request({
    url: '/sale/shop/paySaleOrder',
    method: 'get',
    params: data
  })
}

// 获取商店配置
export function getShopConfig() {
  return request({
    url: '/sale/shop/getShopConfig',
    method: 'get'
  })
}

// 获取订单状态
export function getPayStatus(query) {
  return request({
    url: '/sale/shop/getPayStatus',
    method: 'get',
    params: query
  })
}

// 支付回调
export function callReturn(code, paramsStr) {
  return request({
    url: '/sale/shop/notify/' + code + paramsStr,
    method: 'get'
  })
}

// 查询卡密
export function queryCard(data) {
  return request({
    url: '/sale/shop/queryCard',
    method: 'get',
    params: data
  })
}

// 充值卡密
export function chargeCard(data) {
  return request({
    url: '/sale/shop/chargeCard',
    method: 'get',
    params: data
  })
}

// 查询设备列表
export function queryBindDevice(data) {
  return request({
    url: '/sale/shop/queryBindDevice',
    method: 'get',
    params: data
  })
}

// 解绑设备
export function unbindDevice(data) {
  return request({
    url: '/sale/shop/unbindDevice',
    method: 'get',
    params: data
  })
}

// 获取首页导航信息
export function getNavInfo() {
  return request({
    url: '/sale/shop/getNavInfo',
    method: 'get'
  })
}

// 查询软件类型，查询软件的登录类型
export function getAppAuthType(data) {
  return request({
    url: '/sale/shop/getAppAuthType',
    method: 'get',
    params: data
  })
}
