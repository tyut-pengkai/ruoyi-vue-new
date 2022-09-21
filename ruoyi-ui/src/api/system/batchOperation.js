import request from '@/utils/request'

// 查询软件列表
export function batchOperation(query) {
  return request({
    url: '/system/batchOperation/operation',
    method: 'get',
    params: query
  })
}
