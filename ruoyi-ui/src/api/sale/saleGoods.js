import request from '@/utils/request'

// 查询订单商品列表
export function listSaleGoods(query) {
  return request({
    url: '/sale/saleGoods/list',
    method: 'get',
    params: query
  })
}

// 查询订单商品详细
export function getSaleGoods(id) {
  return request({
    url: '/sale/saleGoods/' + id,
    method: 'get'
  })
}

// 新增订单商品
export function addSaleGoods(data) {
  return request({
    url: '/sale/saleGoods',
    method: 'post',
    data: data
  })
}

// 修改订单商品
export function updateSaleGoods(data) {
  return request({
    url: '/sale/saleGoods',
    method: 'put',
    data: data
  })
}

// 删除订单商品
export function delSaleGoods(id) {
  return request({
    url: '/sale/saleGoods/' + id,
    method: 'delete'
  })
}
