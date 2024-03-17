import request from '@/utils/request'

// 查询软件列表
export function getNotice() {
  return request({
    url: '/system/getNotice',
    method: 'get'
  })
}

export function getDashboard() {
  return request({
    url: '/index/getDashboard',
    method: 'get'
  })
}
