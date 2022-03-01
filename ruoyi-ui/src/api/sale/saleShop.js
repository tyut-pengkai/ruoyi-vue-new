import request from '@/utils/request'

// 查询软件列表
export function listApp() {
  return request({
    url: '/sale/shop/appList',
    method: 'get'
  })
}

// 查询卡类列表
export function listCardTemplate(data) {
  return request({
    url: '/sale/shop/cardTemplateList',
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
