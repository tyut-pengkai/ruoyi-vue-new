import request from '@/utils/request'

// 获取系统详细信息
export function getSysInfo() {
  return request({
    url: '/common/sysInfo',
    method: 'get'
  })
}
