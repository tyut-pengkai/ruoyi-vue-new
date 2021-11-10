import request from '@/utils/request'

// 查询软件用户列表
export function listApp_user(query) {
  return request({
    url: '/system/app_user/list',
    method: 'get',
    params: query
  })
}

// 查询软件用户详细
export function getApp_user(appUserId) {
  return request({
    url: '/system/app_user/' + appUserId,
    method: 'get'
  })
}

// 新增软件用户
export function addApp_user(data) {
  return request({
    url: '/system/app_user',
    method: 'post',
    data: data
  })
}

// 修改软件用户
export function updateApp_user(data) {
  return request({
    url: '/system/app_user',
    method: 'put',
    data: data
  })
}

// 删除软件用户
export function delApp_user(appUserId) {
  return request({
    url: '/system/app_user/' + appUserId,
    method: 'delete'
  })
}

// 导出软件用户
export function exportApp_user(query) {
  return request({
    url: '/system/app_user/export',
    method: 'get',
    params: query
  })
}