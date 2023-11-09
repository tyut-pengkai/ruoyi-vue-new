import request from '@/utils/request'

// 查询时长或点数变动列表
export function listAppUserExpireLog(query) {
  return request({
    url: '/system/appUserExpireLog/list',
    method: 'get',
    params: query
  })
}

// 查询时长或点数变动详细
export function getAppUserExpireLog(id) {
  return request({
    url: '/system/appUserExpireLog/' + id,
    method: 'get'
  })
}

// 新增时长或点数变动
export function addAppUserExpireLog(data) {
  return request({
    url: '/system/appUserExpireLog',
    method: 'post',
    data: data
  })
}

// 修改时长或点数变动
export function updateAppUserExpireLog(data) {
  return request({
    url: '/system/appUserExpireLog',
    method: 'put',
    data: data
  })
}

// 删除时长或点数变动
export function delAppUserExpireLog(id) {
  return request({
    url: '/system/appUserExpireLog/' + id,
    method: 'delete'
  })
}

export function cleanAppUserExpirelog() {
  return request({
    url: '/system/appUserExpireLog/clean',
    method: 'delete'
  })
}
