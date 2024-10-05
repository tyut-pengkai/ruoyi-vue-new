import request from '@/utils/request'

// 查询报价参数列表
export function listQuoteparam(query) {
  return request({
    url: '/system/quoteparam/list',
    method: 'get',
    params: query
  })
}

// 查询报价参数详细
export function getQuoteparam(id) {
  return request({
    url: '/system/quoteparam/' + id,
    method: 'get'
  })
}

// 新增报价参数
export function addQuoteparam(data) {
  return request({
    url: '/system/quoteparam',
    method: 'post',
    data: data
  })
}

// 修改报价参数
export function updateQuoteparam(data) {
  return request({
    url: '/system/quoteparam',
    method: 'put',
    data: data
  })
}

// 删除报价参数
export function delQuoteparam(id) {
  return request({
    url: '/system/quoteparam/' + id,
    method: 'delete'
  })
}
