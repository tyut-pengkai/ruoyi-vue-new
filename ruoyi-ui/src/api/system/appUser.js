import request from '@/utils/request'

// 查询软件用户列表
export function listAppUser(query) {
  return request({
    url: '/system/appUser/list',
    method: 'get',
    params: query
  })
}

// 查询软件用户详细
export function getAppUser(appUserId) {
  return request({
    url: '/system/appUser/' + appUserId,
    method: 'get'
  })
}

// 新增软件用户
export function addAppUser(data) {
  return request({
    url: '/system/appUser',
    method: 'post',
    data: data
  })
}

// 修改软件用户
export function updateAppUser(data) {
  return request({
    url: '/system/appUser',
    method: 'put',
    data: data
  })
}

// 删除软件用户
export function delAppUser(appUserId) {
  return request({
    url: '/system/appUser/' + appUserId,
    method: 'delete'
  })
}

// 导出软件用户
export function exportAppUser(query) {
  return request({
    url: '/system/appUser/export',
    method: 'get',
    params: query
  })
}
