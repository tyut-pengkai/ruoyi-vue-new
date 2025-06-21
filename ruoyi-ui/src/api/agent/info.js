import request from '@/utils/request'

// 查询智能体信息列表
export function listInfo(query) {
  return request({
    url: '/agent/info/list',
    method: 'get',
    params: query
  })
}

// 查询智能体信息详细
export function getInfo(agentId) {
  return request({
    url: '/agent/info/' + agentId,
    method: 'get'
  })
}

// 新增智能体信息
export function addInfo(data) {
  return request({
    url: '/agent/info',
    method: 'post',
    data: data
  })
}

// 修改智能体信息
export function updateInfo(data) {
  return request({
    url: '/agent/info',
    method: 'put',
    data: data
  })
}

// 删除智能体信息
export function delInfo(agentId) {
  return request({
    url: '/agent/info/' + agentId,
    method: 'delete'
  })
}
