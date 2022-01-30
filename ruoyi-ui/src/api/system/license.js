import request from '@/utils/request'

// 查询授权信息
export function getLicenseInfo() {
  return request({
    url: '/system/license/info',
    method: 'get'
  })
}
