import request from '@/utils/request'

// 按输入批量操作
export function batchOperation(query) {
  return request({
    url: '/system/batchOperation/operation',
    method: 'get',
    params: query
  })
}

// 按范围批量操作
export function scopeOperation(query) {
  return request({
    url: '/system/batchOperation/scopeOperation',
    method: 'get',
    params: query
  })
}
