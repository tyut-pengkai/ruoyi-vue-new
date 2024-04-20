import request from '@/utils/request'

// 查询单码类别列表
export function listLoginCodeTemplate(query) {
  return request({
    url: '/system/loginCodeTemplate/list',
    method: 'get',
    params: query
  })
}

// 查询单码类别列表
export function listLoginCodeTemplateAll(query) {
  return request({
    url: '/system/loginCodeTemplate/listAll',
    method: 'get',
    params: query
  })
}

// 查询单码类别详细
export function getLoginCodeTemplate(templateId) {
  return request({
    url: '/system/loginCodeTemplate/' + templateId,
    method: 'get'
  })
}

// 新增单码类别
export function addLoginCodeTemplate(data) {
  return request({
    url: '/system/loginCodeTemplate',
    method: 'post',
    data: data
  })
}

// 新增单码类别
export function addLoginCodeTemplateRapid(data) {
  return request({
    url: '/system/loginCodeTemplate/addRapid',
    method: 'post',
    data: data
  })
}

// 修改单码类别
export function updateLoginCodeTemplate(data) {
  return request({
    url: '/system/loginCodeTemplate',
    method: 'put',
    data: data
  })
}

// 删除单码类别
export function delLoginCodeTemplate(templateId) {
  return request({
    url: '/system/loginCodeTemplate/' + templateId,
    method: 'delete'
  })
}


