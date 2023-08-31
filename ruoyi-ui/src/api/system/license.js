import request from '@/utils/request'

// 查询授权信息
export function getLicenseInfo() {
  return request({
    url: '/system/license/info',
    method: 'get'
  })
}

// 查询授权信息
export function getSimpleLicenseInfo() {
  return request({
    url: '/system/license/simpleInfo',
    method: 'get'
  })
}

// 重新载入授权信息
export function loadLicense() {
  return request({
    url: '/system/license/load',
    method: 'get'
  })
}
