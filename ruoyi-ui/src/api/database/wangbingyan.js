import request from '@/utils/request'

// 查询王冰堰列表
export function listWangbingyan(query) {
  return request({
    url: '/database/product/list',
    method: 'get',
    params: query
  })
}

// 查询王冰堰详细
export function getWangbingyan(id) {
  return request({
    url: '/database/product/' + id,
    method: 'get'
  })
}

// 新增王冰堰
export function addWangbingyan(data) {
  return request({
    url: '/database/product',
    method: 'post',
    data: data
  })
}

// 修改王冰堰
export function updateWangbingyan(data) {
  return request({
    url: '/database/product',
    method: 'put',
    data: data
  })
}

// 删除王冰堰
export function delWangbingyan(id) {
  return request({
    url: '/database/product/' + id,
    method: 'delete'
  })
}

// 导出王冰堰
export function exportWangbingyan(query) {
  return request({
    url: '/database/product/export',
    method: 'get',
    params: query
  })
}