import request from '@/utils/request'

// 查询对外报价列表
export function listEasyquote(query) {
  return request({
    url: '/system/easyquote/list',
    method: 'get',
    params: query
  })
}

// 查询对外报价详细
export function getEasyquote(id) {
  return request({
    url: '/system/easyquote/' + id,
    method: 'get'
  })
}

// 新增对外报价
export function addEasyquote(data) {
  return request({
    url: '/system/easyquote',
    method: 'post',
    data: data
  })
}

// 修改对外报价
export function updateEasyquote(data) {
  return request({
    url: '/system/easyquote',
    method: 'put',
    data: data
  })
}

// 删除对外报价
export function delEasyquote(id) {
  return request({
    url: '/system/easyquote/' + id,
    method: 'delete'
  })
}
