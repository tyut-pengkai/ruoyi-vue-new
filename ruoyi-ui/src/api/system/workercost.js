import request from '@/utils/request'

// 查询人工成本列表
export function listWorkercost(query) {
  return request({
    url: '/system/workercost/list',
    method: 'get',
    params: query
  })
}

// 查询人工成本详细
export function getWorkercost(id) {
  return request({
    url: '/system/workercost/' + id,
    method: 'get'
  })
}

// 新增人工成本
export function addWorkercost(data) {
  return request({
    url: '/system/workercost',
    method: 'post',
    data: data
  })
}

// 修改人工成本
export function updateWorkercost(data) {
  return request({
    url: '/system/workercost',
    method: 'put',
    data: data
  })
}

// 删除人工成本
export function delWorkercost(id) {
  return request({
    url: '/system/workercost/' + id,
    method: 'delete'
  })
}
