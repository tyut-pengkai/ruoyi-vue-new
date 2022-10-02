import request from '@/utils/request'

// 查询远程文件列表
export function listGlobalFile(query) {
  return request({
    url: '/system/globalFile/list',
    method: 'get',
    params: query
  })
}

// 查询远程文件详细
export function getGlobalFile(id) {
  return request({
    url: '/system/globalFile/' + id,
    method: 'get'
  })
}

// 新增远程文件
export function addGlobalFile(data) {
  return request({
    url: '/system/globalFile',
    method: 'post',
    data: data
  })
}

// 修改远程文件
export function updateGlobalFile(data) {
  return request({
    url: '/system/globalFile',
    method: 'put',
    data: data
  })
}

// 删除远程文件
export function delGlobalFile(id) {
  return request({
    url: '/system/globalFile/' + id,
    method: 'delete'
  })
}

// 下载远程文件
export function downloadGlobalFile(id) {
  return request({
    url: '/system/globalFile/download/' + id,
    method: 'get'
  })
}
