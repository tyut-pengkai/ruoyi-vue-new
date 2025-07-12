import request from '@/utils/request'

// 查询设备时长列表
export function listHour(query) {
  return request({
    url: '/device/hour/list',
    method: 'get',
    params: query
  })
}

// 查询设备时长详细
export function getHour(deviceId) {
  return request({
    url: '/device/hour/' + deviceId,
    method: 'get'
  })
}

// 新增设备时长
export function addHour(data) {
  return request({
    url: '/device/hour',
    method: 'post',
    data: data
  })
}

// 修改设备时长
export function updateHour(data) {
  return request({
    url: '/device/hour',
    method: 'put',
    data: data
  })
}

// 删除设备时长
export function delHour(deviceId) {
  return request({
    url: '/device/hour/' + deviceId,
    method: 'delete'
  })
}
