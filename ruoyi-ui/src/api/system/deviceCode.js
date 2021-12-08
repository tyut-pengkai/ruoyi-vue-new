import request from '@/utils/request'

// 查询机器码管理列表
export function listDeviceCode(query) {
  return request({
    url: '/system/deviceCode/list',
    method: 'get',
    params: query
  })
}

// 查询机器码管理详细
export function getDeviceCode(deviceCodeId) {
  return request({
    url: '/system/deviceCode/' + deviceCodeId,
    method: 'get'
  })
}

// 新增机器码管理
export function addDeviceCode(data) {
  return request({
    url: '/system/deviceCode',
    method: 'post',
    data: data
  })
}

// 修改机器码管理
export function updateDeviceCode(data) {
  return request({
    url: '/system/deviceCode',
    method: 'put',
    data: data
  })
}

// 删除机器码管理
export function delDeviceCode(deviceCodeId) {
  return request({
    url: '/system/deviceCode/' + deviceCodeId,
    method: 'delete'
  })
}

// 导出机器码管理
export function exportDeviceCode(query) {
  return request({
    url: '/system/deviceCode/export',
    method: 'get',
    params: query
  })
}

// 状态修改
export function changeDeviceCodeStatus(deviceCodeId, status) {
  const data = {
    deviceCodeId,
    status
  }
  return request({
    url: '/system/deviceCode/changeStatus',
    method: 'put',
    data: data
  })
}
