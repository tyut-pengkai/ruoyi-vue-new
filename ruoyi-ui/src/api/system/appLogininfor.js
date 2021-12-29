import request from '@/utils/request'

// 查询系统访问记录列表
export function listAppLogininfor(query) {
  return request({
    url: '/system/appLogininfor/list',
    method: 'get',
    params: query
  })
}

// 查询系统访问记录详细
export function getAppLogininfor(infoId) {
  return request({
    url: '/system/appLogininfor/' + infoId,
    method: 'get'
  })
}

// 新增系统访问记录
export function addAppLogininfor(data) {
  return request({
    url: '/system/appLogininfor',
    method: 'post',
    data: data
  })
}

// 修改系统访问记录
export function updateAppLogininfor(data) {
  return request({
    url: '/system/appLogininfor',
    method: 'put',
    data: data
  })
}

// 删除系统访问记录
export function delAppLogininfor(infoId) {
  return request({
    url: '/system/appLogininfor/' + infoId,
    method: 'delete'
  })
}

// 导出系统访问记录
export function exportAppLogininfor(query) {
  return request({
    url: '/system/appLogininfor/export',
    method: 'get',
    params: query
  })
}