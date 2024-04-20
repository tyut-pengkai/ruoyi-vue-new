import request from '@/utils/request'

// 查询卡密列表
export function listLoginCode(query) {
  return request({
    url: '/system/loginCode/list',
    method: 'get',
    params: query
  })
}

// 查询卡密详细
export function getLoginCode(loginCodeId) {
  return request({
    url: '/system/loginCode/' + loginCodeId,
    method: 'get'
  })
}

// 新增卡密
export function addLoginCode(data) {
  return request({
    url: '/system/loginCode',
    method: 'post',
    data: data
  })
}

// 修改卡密
export function updateLoginCode(data) {
  return request({
    url: '/system/loginCode',
    method: 'put',
    data: data
  })
}

// 删除卡密
export function delLoginCode(loginCodeId) {
  return request({
    url: '/system/loginCode/' + loginCodeId,
    method: 'delete'
  })
}

// 导出卡密
export function exportLoginCode(query) {
  return request({
    url: '/system/loginCode/export',
    method: 'get',
    params: query
  })
}

// 查询卡密列表
export function getBatchNoList() {
  return request({
    url: '/system/loginCode/selectBatchNoList',
    method: 'get',
  })
}

// 批量换卡
export function batchReplace(data) {
  return request({
    url: '/system/loginCode/batchReplace',
    method: 'post',
    data: data
  })
}
