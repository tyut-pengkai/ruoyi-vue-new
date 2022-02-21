import request from '@/utils/request'

// 查询软件列表
export function listApp() {
  return request({
    url: '/sale/appList',
    method: 'get'
  })
}
