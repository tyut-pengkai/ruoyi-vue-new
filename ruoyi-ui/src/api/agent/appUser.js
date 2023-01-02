import request from '@/utils/request'

// 查询软件用户列表
export function listAppUser(query) {
  return request({
    url: '/agent/appUser/list',
    method: 'get',
    params: query
  })
}

// 查询软件用户详细
export function getAppUser(appUserId) {
  return request({
    url: '/agent/appUser/' + appUserId,
    method: 'get'
  })
}

// 新增软件用户
export function addAppUser(data) {
  return request({
    url: '/agent/appUser',
    method: 'post',
    data: data
  })
}

// 修改软件用户
export function updateAppUser(data) {
  return request({
    url: '/agent/appUser',
    method: 'put',
    data: data
  })
}

// 删除软件用户
export function delAppUser(appUserId) {
  return request({
    url: '/agent/appUser/' + appUserId,
    method: 'delete'
  })
}

// 导出软件用户
export function exportAppUser(query) {
  return request({
    url: '/agent/appUser/export',
    method: 'get',
    params: query
  })
}

// 状态修改
export function changeAppUserStatus(appUserId, status) {
  const data = {
    appUserId,
    status
  }
  return request({
    url: '/agent/appUser/changeStatus',
    method: 'put',
    data: data
  })
}
