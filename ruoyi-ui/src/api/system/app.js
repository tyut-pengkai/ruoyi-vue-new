import request from '@/utils/request'

// 查询软件列表
export function listApp(query) {
  return request({
    url: '/system/app/list',
    method: 'get',
    params: query
  })
}

// 查询软件列表
export function listAppAll(query) {
  return request({
    url: '/system/app/listAll',
    method: 'get',
    params: query
  })
}

// 查询软件详细
export function getApp(appId) {
  return request({
    url: '/system/app/' + appId,
    method: 'get'
  })
}

// 新增软件
export function addApp(data) {
  return request({
    url: '/system/app',
    method: 'post',
    data: data
  })
}

// 修改软件
export function updateApp(data) {
  return request({
    url: '/system/app',
    method: 'put',
    data: data
  })
}

// 删除软件
export function delApp(appId) {
  return request({
    url: '/system/app/' + appId,
    method: 'delete'
  })
}

// 软件状态修改
export function changeAppStatus(appId, status) {
  const data = {
    appId,
    status
  }
  return request({
    url: '/system/app/changeStatus',
    method: 'put',
    data: data
  })
}

// 软件计费状态修改
export function changeAppChargeStatus(appId, isCharge) {
  const data = {
    appId,
    isCharge
  }
  return request({
    url: '/system/app/changeChargeStatus',
    method: 'put',
    data: data
  })
}

// 导出软件
export function exportApp(query) {
  return request({
    url: '/system/app/export',
    method: 'get',
    params: query
  })
}
