import request from '@/utils/request'

// 查询全局变量列表
export function listGlobalVariable(query) {
  return request({
    url: '/system/globalVariable/list',
    method: 'get',
    params: query
  })
}

// 查询全局变量详细
export function getGlobalVariable(id) {
  return request({
    url: '/system/globalVariable/' + id,
    method: 'get'
  })
}

// 新增全局变量
export function addGlobalVariable(data) {
  return request({
    url: '/system/globalVariable',
    method: 'post',
    data: data
  })
}

// 修改全局变量
export function updateGlobalVariable(data) {
  return request({
    url: '/system/globalVariable',
    method: 'put',
    data: data
  })
}

// 删除全局变量
export function delGlobalVariable(id) {
  return request({
    url: '/system/globalVariable/' + id,
    method: 'delete'
  })
}
