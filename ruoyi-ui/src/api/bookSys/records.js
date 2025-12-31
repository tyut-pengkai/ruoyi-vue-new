import request from '@/utils/request'

// 查询访客记录列表
export function listRecords(query) {
  return request({
    url: '/bookSys/records/list',
    method: 'get',
    params: query
  })
}

// 查询访客记录详细
export function getRecords(recordId) {
  return request({
    url: '/bookSys/records/' + recordId,
    method: 'get'
  })
}

// 新增访客记录
export function addRecords(data) {
  return request({
    url: '/bookSys/records',
    method: 'post',
    data: data
  })
}

// 修改访客记录
export function updateRecords(data) {
  return request({
    url: '/bookSys/records',
    method: 'put',
    data: data
  })
}

// 删除访客记录
export function delRecords(recordId) {
  return request({
    url: '/bookSys/records/' + recordId,
    method: 'delete'
  })
}
