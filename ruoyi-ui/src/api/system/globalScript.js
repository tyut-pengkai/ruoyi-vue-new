import request from '@/utils/request'

// 查询全局脚本列表
export function listGlobalScript(query) {
  return request({
    url: '/system/globalScript/list',
    method: 'get',
    params: query
  })
}

// 查询全局脚本详细
export function getGlobalScript(scriptId) {
  return request({
    url: '/system/globalScript/' + scriptId,
    method: 'get'
  })
}

// 新增全局脚本
export function addGlobalScript(data) {
  return request({
    url: '/system/globalScript',
    method: 'post',
    data: data
  })
}

// 修改全局脚本
export function updateGlobalScript(data) {
  return request({
    url: '/system/globalScript',
    method: 'put',
    data: data
  })
}

// 删除全局脚本
export function delGlobalScript(scriptId) {
  return request({
    url: '/system/globalScript/' + scriptId,
    method: 'delete'
  })
}
