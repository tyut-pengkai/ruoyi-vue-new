import request from '@/utils/request'

// 检查更新
export function checkUpdate() {
  return request({
    url: '/system/update/checkUpdate',
    method: 'get',
    timeout: 30000
  })
}

// 升级
export function doUpdate() {
  return request({
    url: '/system/update/doUpdate',
    method: 'get',
    timeout: 30000
  })
}

// 获取升级状态
export function getStatus() {
  return request({
    url: '/system/update/status',
    method: 'get',
    timeout: 30000
  })
}

// 重启
export function doRestart() {
  return request({
    url: '/system/restart/doRestart',
    method: 'get',
    timeout: 30000
  })
}

// 获取重启状态
export function getRestartStatus() {
  return request({
    url: '/system/restart/status',
    method: 'get',
    timeout: 30000
  })
}

// 导出错误日志
export function exportErrorLog(query) {
  return request({
    url: '/system/exportErrorLog',
    method: 'get',
    params: query
  })
}
