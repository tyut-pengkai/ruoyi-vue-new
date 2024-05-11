import request from '@/utils/request'

// 查询软件用户与设备码关联列表
export function listAppUserDeviceCode(query) {
  return request({
    url: '/agent/appUserDeviceCode/list',
    method: 'get',
    params: query
  })
}

// 查询软件用户与设备码关联详细
export function getAppUserDeviceCode(id) {
  return request({
    url: '/agent/appUserDeviceCode/' + id,
    method: 'get'
  })
}

// 新增软件用户与设备码关联
export function addAppUserDeviceCode(data) {
  return request({
    url: '/agent/appUserDeviceCode',
    method: 'post',
    data: data
  })
}

// 修改软件用户与设备码关联
export function updateAppUserDeviceCode(data) {
  return request({
    url: '/agent/appUserDeviceCode',
    method: 'put',
    data: data
  })
}

// 删除软件用户与设备码关联
export function delAppUserDeviceCode(id) {
  return request({
    url: '/agent/appUserDeviceCode/' + id,
    method: 'delete'
  })
}

// 导出软件用户与设备码关联
export function exportAppUserDeviceCode(query) {
  return request({
    url: '/agent/appUserDeviceCode/export',
    method: 'get',
    params: query
  })
}

// 状态修改
export function changeAppUserDeviceCodeStatus(id, status) {
  const data = {
    id,
    status
  }
  return request({
    url: '/agent/appUserDeviceCode/changeStatus',
    method: 'put',
    data: data
  })
}
