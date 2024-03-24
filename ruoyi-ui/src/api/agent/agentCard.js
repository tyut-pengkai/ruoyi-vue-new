import request from '@/utils/request'

// 查询卡密列表
export function listCard(query) {
  return request({
    url: '/agent/agentCard/list',
    method: 'get',
    params: query
  })
}

// 查询卡密详细
export function getCard(cardId) {
  return request({
    url: '/agent/agentCard/' + cardId,
    method: 'get'
  })
}

// 新增卡密
export function addCard(data) {
  return request({
    url: '/agent/agentCard',
    method: 'post',
    data: data
  })
}

// 修改卡密
export function updateCard(data) {
  return request({
    url: '/agent/agentCard',
    method: 'put',
    data: data
  })
}

// 删除卡密
export function delCard(cardId) {
  return request({
    url: '/agent/agentCard/' + cardId,
    method: 'delete'
  })
}

// 导出卡密
export function exportCard(query) {
  return request({
    url: '/agent/agentCard/export',
    method: 'get',
    params: query
  })
}

// 查询卡密模板列表
export function listCardTemplateAll(query) {
  return request({
    url: '/agent/agentCard/cardTemplate/listAll',
    method: 'get',
    params: query
  })
}

// 查询卡密列表
export function getBatchNoList() {
  return request({
    url: '/agent/agentCard/selectBatchNoList',
    method: 'get',
  })
}
