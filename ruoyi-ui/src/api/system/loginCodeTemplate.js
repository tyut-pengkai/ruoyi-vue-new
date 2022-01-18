import request from '@/utils/request'

// 查询登录码类别列表
export function listLoginCodeTemplate(query) {
  return request({
    url: '/system/loginCodeTemplate/list',
    method: 'get',
    params: query
  })
}

// 查询登录码类别详细
export function getLoginCodeTemplate(templateId) {
  return request({
    url: '/system/loginCodeTemplate/' + templateId,
    method: 'get'
  })
}

// 新增登录码类别
export function addLoginCodeTemplate(data) {
  return request({
    url: '/system/loginCodeTemplate',
    method: 'post',
    data: data
  })
}

// 修改登录码类别
export function updateLoginCodeTemplate(data) {
  return request({
    url: '/system/loginCodeTemplate',
    method: 'put',
    data: data
  })
}

// 删除登录码类别
export function delLoginCodeTemplate(templateId) {
  return request({
    url: '/system/loginCodeTemplate/' + templateId,
    method: 'delete'
  })
}
