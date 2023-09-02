import request from '@/utils/request'

// 查询卡密模板列表
export function listCardTemplate(query) {
  return request({
    url: '/system/cardTemplate/list',
    method: 'get',
    params: query
  })
}

// 查询卡密模板列表
export function listCardTemplateAll(query) {
  return request({
    url: '/system/cardTemplate/listAll',
    method: 'get',
    params: query
  })
}

// 查询卡密模板详细
export function getCardTemplate(templateId) {
  return request({
    url: '/system/cardTemplate/' + templateId,
    method: 'get'
  })
}

// 新增卡密模板
export function addCardTemplate(data) {
  return request({
    url: '/system/cardTemplate',
    method: 'post',
    data: data
  })
}

// 新增卡密模板
export function addCardTemplateRapid(data) {
  return request({
    url: '/system/cardTemplate/addRapid',
    method: 'post',
    data: data
  })
}

// 修改卡密模板
export function updateCardTemplate(data) {
  return request({
    url: '/system/cardTemplate',
    method: 'put',
    data: data
  })
}

// 删除卡密模板
export function delCardTemplate(templateId) {
  return request({
    url: '/system/cardTemplate/' + templateId,
    method: 'delete'
  })
}

// 导出卡密模板
export function exportCardTemplate(query) {
  return request({
    url: '/system/cardTemplate/export',
    method: 'get',
    params: query
  })
}
