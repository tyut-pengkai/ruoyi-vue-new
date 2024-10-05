import request from '@/utils/request'

// 查询工种管理列表
export function listWorktype(query) {
  return request({
    url: '/system/worktype/list',
    method: 'get',
    params: query
  })
}

// 查询工种管理详细
export function getWorktype(id) {
  return request({
    url: '/system/worktype/' + id,
    method: 'get'
  })
}

// 新增工种管理
export function addWorktype(data) {
  return request({
    url: '/system/worktype',
    method: 'post',
    data: data
  })
}

// 修改工种管理
export function updateWorktype(data) {
  return request({
    url: '/system/worktype',
    method: 'put',
    data: data
  })
}

// 删除工种管理
export function delWorktype(id) {
  return request({
    url: '/system/worktype/' + id,
    method: 'delete'
  })
}
