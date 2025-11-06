import request from '@/utils/request'

// 查询数据库产品列表
export function listProduct(query) {
  return request({
    url: '/database/product/list',
    method: 'get',
    params: query
  })
}

// 查询数据库产品详细
export function getProduct(productId) {
  return request({
    url: '/database/product/' + productId,
    method: 'get'
  })
}

// 新增数据库产品
export function addProduct(data) {
  return request({
    url: '/database/product',
    method: 'post',
    data: data
  })
}

// 修改数据库产品
export function updateProduct(data) {
  return request({
    url: '/database/product',
    method: 'put',
    data: data
  })
}

// 删除数据库产品
export function delProduct(productId) {
  return request({
    url: '/database/product/' + productId,
    method: 'delete'
  })
}

// 导出数据库产品
export function exportProduct(query) {
  return request({
    url: '/database/product/export',
    method: 'get',
    params: query
  })
}