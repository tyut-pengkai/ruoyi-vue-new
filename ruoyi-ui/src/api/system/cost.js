import request from '@/utils/request'

// 查询cost列表
export function listCost(query) {
  return request({
    url: '/system/cost/list',
    method: 'get',
    params: query
  })
}

// 查询cost详细
export function getCost(id) {
  return request({
    url: '/system/cost/' + id,
    method: 'get'
  })
}

// 新增cost
export function addCost(data) {
  return request({
    url: '/system/cost',
    method: 'post',
    data: data
  })
}

// 修改cost
export function updateCost(data) {
  return request({
    url: '/system/cost',
    method: 'put',
    data: data
  })
}

// 删除cost
export function delCost(id) {
  return request({
    url: '/system/cost/' + id,
    method: 'delete'
  })
}
