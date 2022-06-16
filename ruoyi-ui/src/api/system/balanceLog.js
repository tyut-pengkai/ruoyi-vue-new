import request from '@/utils/request'

// 查询余额变动列表
export function listBalanceLog(query) {
  return request({
    url: '/system/balanceLog/list',
    method: 'get',
    params: query
  })
}

// 查询余额变动详细
export function getBalanceLog(id) {
  return request({
    url: '/system/balanceLog/' + id,
    method: 'get'
  })
}

// 新增余额变动
export function addBalanceLog(data) {
  return request({
    url: '/system/balanceLog',
    method: 'post',
    data: data
  })
}

// 修改余额变动
export function updateBalanceLog(data) {
  return request({
    url: '/system/balanceLog',
    method: 'put',
    data: data
  })
}

// 删除余额变动
export function delBalanceLog(id) {
  return request({
    url: '/system/balanceLog/' + id,
    method: 'delete'
  })
}
