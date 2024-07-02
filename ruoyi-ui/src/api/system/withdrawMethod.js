import request from '@/utils/request'

// 查询收款方式列表
export function listWithdrawMethod(query) {
  return request({
    url: '/system/withdrawMethod/list',
    method: 'get',
    params: query
  })
}

// 查询收款方式详细
export function getWithdrawMethod(id) {
  return request({
    url: '/system/withdrawMethod/' + id,
    method: 'get'
  })
}

// 新增收款方式
export function addWithdrawMethod(data) {
  return request({
    url: '/system/withdrawMethod',
    method: 'post',
    data: data
  })
}

// 修改收款方式
export function updateWithdrawMethod(data) {
  return request({
    url: '/system/withdrawMethod',
    method: 'put',
    data: data
  })
}

// 删除收款方式
export function delWithdrawMethod(id) {
  return request({
    url: '/system/withdrawMethod/' + id,
    method: 'delete'
  })
}
