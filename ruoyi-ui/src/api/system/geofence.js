import request from '@/utils/request'

// 查询地理围栏列表
export function listGeofence(query) {
  return request({
    url: '/system/geofence/list',
    method: 'get',
    params: query
  })
}

// 查询地理围栏详细
export function getGeofence(id) {
  return request({
    url: '/system/geofence/' + id,
    method: 'get'
  })
}

// 新增地理围栏
export function addGeofence(data) {
  return request({
    url: '/system/geofence',
    method: 'post',
    data: data
  })
}

// 修改地理围栏
export function updateGeofence(data) {
  return request({
    url: '/system/geofence',
    method: 'put',
    data: data
  })
}

// 删除地理围栏
export function delGeofence(id) {
  return request({
    url: '/system/geofence/' + id,
    method: 'delete'
  })
}
