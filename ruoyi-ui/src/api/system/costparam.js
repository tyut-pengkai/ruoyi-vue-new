import request from '@/utils/request'

// 查询成本参数列表
export function listCostparam(query) {
  return request({
    url: '/system/costparam/list',
    method: 'get',
    params: query
  })
}

// 查询成本参数详细
export function getCostparam(id) {
  return request({
    url: '/system/costparam/' + id,
    method: 'get'
  })
}

// 新增成本参数
export function addCostparam(data) {
  return request({
    url: '/system/costparam',
    method: 'post',
    data: data
  })
}

// 修改成本参数
export function updateCostparam(data) {
  return request({
    url: '/system/costparam',
    method: 'put',
    data: data
  })
}

// 删除成本参数
export function delCostparam(id) {
  return request({
    url: '/system/costparam/' + id,
    method: 'delete'
  })
}
