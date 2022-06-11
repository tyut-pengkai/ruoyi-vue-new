import request from '@/utils/request'

// 查询代理卡类关联列表
export function listAgentItem(query) {
  return request({
    url: '/agent/agentItem/list',
    method: 'get',
    params: query
  })
}

// 查询代理卡类关联详细
export function getAgentItem(id) {
  return request({
    url: '/agent/agentItem/' + id,
    method: 'get'
  })
}

// 新增代理卡类关联
export function addAgentItem(data) {
  return request({
    url: '/agent/agentItem',
    method: 'post',
    data: data
  })
}

// 修改代理卡类关联
export function updateAgentItem(data) {
  return request({
    url: '/agent/agentItem',
    method: 'put',
    data: data
  })
}

// 删除代理卡类关联
export function delAgentItem(id) {
  return request({
    url: '/agent/agentItem/' + id,
    method: 'delete'
  })
}

// 查询可授权卡类列表
export function grantableTemplate(query) {
  return request({
    url: '/agent/agentItem/grantableTemplate',
    method: 'get',
    params: query
  })
}
