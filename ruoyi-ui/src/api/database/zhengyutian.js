import request from '@/utils/request'

// 查询郑瑜甜列表
export function listZhengyutian(query) {
  return request({
    url: '/database/product/list',
    method: 'get',
    params: query
  })
}

// 查询郑瑜甜详细
export function getZhengyutian(id) {
  return request({
    url: '/database/product/' + id,
    method: 'get'
  })
}

// 新增郑瑜甜
export function addZhengyutian(data) {
  return request({
    url: '/database/product',
    method: 'post',
    data: data
  })
}

// 修改郑瑜甜
export function updateZhengyutian(data) {
  return request({
    url: '/database/product',
    method: 'put',
    data: data
  })
}

// 删除郑瑜甜
export function delZhengyutian(id) {
  return request({
    url: '/database/product/' + id,
    method: 'delete'
  })
}

// 导出郑瑜甜
export function exportZhengyutian(query) {
  return request({
    url: '/database/product/export',
    method: 'get',
    params: query
  })
}