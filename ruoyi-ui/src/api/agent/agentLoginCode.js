import request from '@/utils/request'

// 查询卡密列表
export function listLoginCode(query) {
  return request({
    url: '/agent/agentLoginCode/list',
    method: 'get',
    params: query
  })
}

// 查询卡密详细
export function getLoginCode(loginCodeId) {
  return request({
    url: '/agent/agentLoginCode/' + loginCodeId,
    method: 'get'
  })
}

// 新增卡密
export function addLoginCode(data) {
  return request({
    url: '/agent/agentLoginCode',
    method: 'post',
    data: data
  })
}

// 修改卡密
export function updateLoginCode(data) {
  return request({
    url: '/agent/agentLoginCode',
    method: 'put',
    data: data
  })
}

// 删除卡密
export function delLoginCode(loginCodeId) {
  return request({
    url: '/agent/agentLoginCode/' + loginCodeId,
    method: 'delete'
  })
}

// 导出卡密
export function exportLoginCode(query) {
  return request({
    url: '/agent/agentLoginCode/export',
    method: 'get',
    params: query
  })
}

// 查询卡密模板列表
export function listLoginCodeTemplateAll(query) {
  return request({
    url: '/agent/agentLoginCode/loginCodeTemplate/listAll',
    method: 'get',
    params: query
  })
}

// 查询卡密列表
export function getBatchNoList() {
  return request({
    url: '/agent/agentLoginCode/selectBatchNoList',
    method: 'get',
  })
}

// 批量换卡
export function batchReplace(data) {
  return request({
    url: '/agent/agentLoginCode/batchReplace',
    method: 'post',
    data: data
  })
}
