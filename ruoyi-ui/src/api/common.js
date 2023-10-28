import request from '@/utils/request'

// 获取系统详细信息
export function getSysInfo() {
  return request({
    url: '/common/sysInfo',
    method: 'get'
  })
}

// 获取Dashboard信息
export function getDashboardInfoSaleView(query) {
  return request({
    url: '/common/dashboardInfoSaleView',
    method: 'get',
    params: query,
    timeout: 30000
  })
}

// 获取Dashboard信息
export function getDashboardInfoAppView(query) {
  return request({
    url: '/common/dashboardInfoAppView',
    method: 'get',
    params: query,
    timeout: 30000
  })
}

export function randomString(length) {
  return request({
    url: '/common/randomString?length=' + length,
    method: 'get'
  })
}
