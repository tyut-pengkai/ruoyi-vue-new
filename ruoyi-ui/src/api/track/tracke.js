import request from '@/utils/request'

// 查询运踪列表
export function listTracke(query) {
  return request({
    url: '/system/tracke/list',
    method: 'get',
    params: query
  })
}

// 查询运踪详细
export function getTracke(id) {
  return request({
    url: '/system/tracke/' + id,
    method: 'get'
  })
}

// 新增运踪
export function addTracke(data) {
  return request({
    url: '/system/tracke',
    method: 'post',
    data: data
  })
}

// 修改运踪
export function updateTracke(data) {
  return request({
    url: '/system/tracke',
    method: 'put',
    data: data
  })
}

// 删除运踪
export function delTracke(id) {
  return request({
    url: '/system/tracke/' + id,
    method: 'delete'
  })
}
