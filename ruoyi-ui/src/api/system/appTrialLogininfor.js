import request from '@/utils/request'

// 查询系统访问记录列表
export function listAppTrialLogininfor(query) {
  return request({
    url: '/system/appTrialLogininfor/list',
    method: 'get',
    params: query
  })
}

// 查询系统访问记录详细
export function getAppTrialLogininfor(infoId) {
  return request({
    url: '/system/appTrialLogininfor/' + infoId,
    method: 'get'
  })
}

// 新增系统访问记录
export function addAppTrialLogininfor(data) {
  return request({
    url: '/system/appTrialLogininfor',
    method: 'post',
    data: data
  })
}

// 修改系统访问记录
export function updateAppTrialLogininfor(data) {
  return request({
    url: '/system/appTrialLogininfor',
    method: 'put',
    data: data
  })
}

// 删除系统访问记录
export function delAppTrialLogininfor(infoId) {
  return request({
    url: '/system/appTrialLogininfor/' + infoId,
    method: 'delete'
  })
}

// 导出系统访问记录
export function exportAppTrialLogininfor(query) {
  return request({
    url: '/system/appTrialLogininfor/export',
    method: 'get',
    params: query
  })
}

// 清空登录日志
export function cleanTrialLogininfor() {
  return request({
    url: '/system/appTrialLogininfor/clean',
    method: 'delete'
  })
}
