import request from '@/utils/request'

// 检查更新
export function checkUpdate() {
  return request({
    url: '/system/update/checkUpdate',
    method: 'get'
  })
}

// 升级
export function doUpdate() {
  return request({
    url: '/system/update/doUpdate',
    method: 'get',
  })
}

// 获取状态
export function getStatus() {
  return request({
    url: '/system/update/status',
    method: 'get',
  })
}
