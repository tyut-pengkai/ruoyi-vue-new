import request from '@/utils/request'

// 查询提现记录列表
export function listWithdrawOrder(query) {
  return request({
    url: '/system/withdrawOrder/list',
    method: 'get',
    params: query
  })
}

// 查询提现记录详细
export function getWithdrawOrder(id) {
  return request({
    url: '/system/withdrawOrder/' + id,
    method: 'get'
  })
}

// 新增提现记录
export function addWithdrawOrder(data) {
  return request({
    url: '/system/withdrawOrder',
    method: 'post',
    data: data
  })
}

// 修改提现记录
export function updateWithdrawOrder(data) {
  return request({
    url: '/system/withdrawOrder',
    method: 'put',
    data: data
  })
}

// 删除提现记录
export function delWithdrawOrder(id) {
  return request({
    url: '/system/withdrawOrder/' + id,
    method: 'delete'
  })
}

// 创建提现订单
export function createWithdrawOrder(data) {
  return request({
    url: '/system/withdrawOrder/createWithdrawOrder',
    method: 'put',
    data: data
  })
}

// 查询提现记录列表
export function listWithdrawOrderSelf(query) {
  return request({
    url: '/system/withdrawOrder/self/list',
    method: 'get',
    params: query
  })
}

// 查询提现记录详细
export function getWithdrawOrderSelf(id) {
  return request({
    url: '/system/withdrawOrder/self/' + id,
    method: 'get'
  })
}

// 删除提现记录
export function cancelWithdrawOrder(id) {
  return request({
    url: '/system/withdrawOrder/cancel/' + id,
    method: 'delete'
  })
}


