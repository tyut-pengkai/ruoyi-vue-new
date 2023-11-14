import request from '@/utils/request'

// 查询软件列表
export function getNotice(query) {
  return request({
    url: '/system/getNotice',
    method: 'get',
    params: query
  })
}

export function getDashboard() {
  return request({
    url: '/index/getDashboard',
    method: 'get'
  })
}
