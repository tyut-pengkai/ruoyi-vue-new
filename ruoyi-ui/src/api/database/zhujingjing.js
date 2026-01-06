import request from '@/utils/request'

// 查询朱晶晶列表
export function listZhujingjing(query) {
  return request({
    url: '/database/product/list',
    method: 'get',
    params: query
  })
}

// 查询朱晶晶详细
export function getZhujingjing(id) {
  return request({
    url: '/database/product/' + id,
    method: 'get'
  })
}

// 新增朱晶晶
export function addZhujingjing(data) {
  return request({
    url: '/database/product',
    method: 'post',
    data: data
  })
}

// 修改朱晶晶
export function updateZhujingjing(data) {
  return request({
    url: '/database/product',
    method: 'put',
    data: data
  })
}

// 删除朱晶晶
export function delZhujingjing(id) {
  return request({
    url: '/database/product/' + id,
    method: 'delete'
  })
}

// 导出朱晶晶
export function exportZhujingjing(query) {
  return request({
    url: '/database/product/export',
    method: 'get',
    params: query
  })
}