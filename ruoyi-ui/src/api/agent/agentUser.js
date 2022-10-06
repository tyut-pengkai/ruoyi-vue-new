import request from '@/utils/request'
import {parseStrEmpty} from "@/utils/ruoyi";

// 查询代理管理列表
export function listAgentUser(query) {
  return request({
    url: '/agent/agentUser/list',
    method: 'get',
    params: query
  })
}

// 查询代理管理详细
export function getAgentUser(agentId) {
  return request({
    url: '/agent/agentUser/' + agentId,
    method: 'get'
  })
}

// 新增代理管理
export function addAgentUser(data) {
  return request({
    url: '/agent/agentUser',
    method: 'post',
    data: data
  })
}

// 修改代理管理
export function updateAgentUser(data) {
  return request({
    url: '/agent/agentUser',
    method: 'put',
    data: data
  })
}

// 删除代理管理
export function delAgentUser(agentId) {
  return request({
    url: '/agent/agentUser/' + agentId,
    method: 'delete'
  })
}

// 获取所有作者和代理，除去参数的代理
export function listAgents(agentId) {
  return request({
    url: '/agent/agentUser/listAgents/' + parseStrEmpty(agentId),
    method: 'get'
  })
}

// 获取所有非代理
export function listNonAgents(query) {
  return request({
    url: '/agent/agentUser/listNonAgents',
    method: 'get',
    params: query
  })
}
