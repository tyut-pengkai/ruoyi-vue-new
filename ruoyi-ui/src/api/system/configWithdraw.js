import request from '@/utils/request'

// 查询网站配置
export function getWithdrawConfig() {
  return request({
    url: '/system/withdraw/config',
    method: 'get'
  })
}

// 修改网站配置
export function updateWithdrawConfig(data) {
  return request({
    url: '/system/withdraw/config',
    method: 'put',
    data: data
  })
}
