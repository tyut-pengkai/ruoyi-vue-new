import request from '@/utils/request'

// 查询软件版本信息列表
export function listAppVersion(query) {
  return request({
    url: '/system/appVersion/list',
    method: 'get',
    params: query
  })
}

// 查询软件版本信息详细
export function getAppVersion(appVersionId) {
  return request({
    url: '/system/appVersion/' + appVersionId,
    method: 'get'
  })
}

// 新增软件版本信息
export function addAppVersion(data) {
  return request({
    url: '/system/appVersion',
    method: 'post',
    data: data
  })
}

// 修改软件版本信息
export function updateAppVersion(data) {
  return request({
    url: '/system/appVersion',
    method: 'put',
    data: data
  })
}

// 删除软件版本信息
export function delAppVersion(appVersionId) {
  return request({
    url: '/system/appVersion/' + appVersionId,
    method: 'delete'
  })
}

// 导出软件版本信息
export function exportAppVersion(query) {
  return request({
    url: '/system/appVersion/export',
    method: 'get',
    params: query
  })
}

// 获取快速接入参数信息
export function getQuickAccessParams(appVersionId) {
  return request({
    url: '/system/appVersion/quickAccessParams/' + appVersionId,
    method: 'get',
  })
}
