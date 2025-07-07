import request from '@/utils/request'

// 查询地区管理列表
export function listRegions(query) {
  return request({
    url: '/mmclub/regions/list',
    method: 'get',
    params: query
  })
}

// 查询地区管理详细
export function getRegions(id) {
  return request({
    url: '/mmclub/regions/' + id,
    method: 'get'
  })
}

// 新增地区管理
export function addRegions(data) {
  return request({
    url: '/mmclub/regions',
    method: 'post',
    data: data
  })
}

// 修改地区管理
export function updateRegions(data) {
  return request({
    url: '/mmclub/regions',
    method: 'put',
    data: data
  })
}

// 删除地区管理
export function delRegions(id) {
  return request({
    url: '/mmclub/regions/' + id,
    method: 'delete'
  })
}
