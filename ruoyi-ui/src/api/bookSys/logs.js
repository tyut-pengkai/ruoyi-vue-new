import request from '@/utils/request'

// 查询通行记录列表
export function listLogs(query) {
  return request({
    url: '/bookSys/logs/list',
    method: 'get',
    params: query
  })
}

// 查询通行记录详细
export function getLogs(logId) {
  return request({
    url: '/bookSys/logs/' + logId,
    method: 'get'
  })
}

// 新增通行记录
export function addLogs(data) {
  return request({
    url: '/bookSys/logs',
    method: 'post',
    data: data
  })
}

// 修改通行记录
export function updateLogs(data) {
  return request({
    url: '/bookSys/logs',
    method: 'put',
    data: data
  })
}

// 删除通行记录
export function delLogs(logId) {
  return request({
    url: '/bookSys/logs/' + logId,
    method: 'delete'
  })
}
