import request from '@/utils/request'

// 查询医院分类管理列表
export function listCategorys(query) {
  return request({
    url: '/mmclub/categorys/list',
    method: 'get',
    params: query
  })
}

// 查询医院分类管理详细
export function getCategorys(id) {
  return request({
    url: '/mmclub/categorys/' + id,
    method: 'get'
  })
}

// 新增医院分类管理
export function addCategorys(data) {
  return request({
    url: '/mmclub/categorys',
    method: 'post',
    data: data
  })
}

// 修改医院分类管理
export function updateCategorys(data) {
  return request({
    url: '/mmclub/categorys',
    method: 'put',
    data: data
  })
}

// 删除医院分类管理
export function delCategorys(id) {
  return request({
    url: '/mmclub/categorys/' + id,
    method: 'delete'
  })
}
