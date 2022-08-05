import request from '@/utils/request'

// 查询试用信息列表
export function listAppTrialUser(query) {
  return request({
    url: '/system/appTrialUser/list',
    method: 'get',
    params: query
  })
}

// 查询试用信息详细
export function getAppTrialUser(appTrialUserId) {
  return request({
    url: '/system/appTrialUser/' + appTrialUserId,
    method: 'get'
  })
}

// 新增试用信息
export function addAppTrialUser(data) {
  return request({
    url: '/system/appTrialUser',
    method: 'post',
    data: data
  })
}

// 修改试用信息
export function updateAppTrial(data) {
  return request({
    url: '/system/appTrialUser',
    method: 'put',
    data: data
  })
}

// 删除试用信息
export function delAppTrialUser(appTrialUserId) {
  return request({
    url: '/system/appTrialUser/' + appTrialUserId,
    method: 'delete'
  })
}
