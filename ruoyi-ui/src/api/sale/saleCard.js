import request from '@/utils/request'

// 查询订单卡密列表
export function listSaleCard(query) {
  return request({
    url: '/sale/saleCard/list',
    method: 'get',
    params: query
  })
}

// 查询订单卡密详细
export function getSaleCard(id) {
  return request({
    url: '/sale/saleCard/' + id,
    method: 'get'
  })
}

// 新增订单卡密
export function addSaleCard(data) {
  return request({
    url: '/sale/saleCard',
    method: 'post',
    data: data
  })
}

// 修改订单卡密
export function updateSaleCard(data) {
  return request({
    url: '/sale/saleCard',
    method: 'put',
    data: data
  })
}

// 删除订单卡密
export function delSaleCard(id) {
  return request({
    url: '/sale/saleCard/' + id,
    method: 'delete'
  })
}
