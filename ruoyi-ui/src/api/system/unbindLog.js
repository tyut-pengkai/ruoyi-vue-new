import request from '@/utils/request'

// 查询解绑日志列表
export function listUnbindLog(query) {
  return request({
    url: '/system/unbindLog/list',
    method: 'get',
    params: query
  })
}

// 查询解绑日志详细
export function getUnbindLog(id) {
  return request({
    url: '/system/unbindLog/' + id,
    method: 'get'
  })
}

// 新增解绑日志
export function addUnbindLog(data) {
  return request({
    url: '/system/unbindLog',
    method: 'post',
    data: data
  })
}

// 修改解绑日志
export function updateUnbindLog(data) {
  return request({
    url: '/system/unbindLog',
    method: 'put',
    data: data
  })
}

// 删除解绑日志
export function delUnbindLog(id) {
  return request({
    url: '/system/unbindLog/' + id,
    method: 'delete'
  })
}

// 清空操作日志
export function cleanUnbindlog() {
  return request({
    url: '/system/unbindLog/clean',
    method: 'delete'
  })
}
