import request from '@/utils/request'

// 查询访客信息列表
export function listVisitors(query) {
  return request({
    url: '/bookSys/visitors/list',
    method: 'get',
    params: query
  })
}

// 查询访客信息详细
export function getVisitors(visitorId) {
  return request({
    url: '/bookSys/visitors/' + visitorId,
    method: 'get'
  })
}

// 新增访客信息
export function addVisitors(data) {
  return request({
    url: '/bookSys/visitors',
    method: 'post',
    data: data
  })
}

// 修改访客信息
export function updateVisitors(data) {
  return request({
    url: '/bookSys/visitors',
    method: 'put',
    data: data
  })
}

// 删除访客信息
export function delVisitors(visitorId) {
  return request({
    url: '/bookSys/visitors/' + visitorId,
    method: 'delete'
  })
}
