import request from '@/utils/request'

// 查询商品套餐列表
export function listPackage(query) {
  return request({
    url: '/payment/package/list',
    method: 'get',
    params: query
  })
}

// 查询可用的商品套餐列表（供用户选择）
export function listPackageForUser() {
  return request({
    url: '/payment/package/listForUser',
    method: 'get'
  })
}

// 查询商品套餐详细
export function getPackage(packageId) {
  return request({
    url: '/payment/package/' + packageId,
    method: 'get'
  })
}

// 新增商品套餐
export function addPackage(data) {
  return request({
    url: '/payment/package',
    method: 'post',
    data: data
  })
}

// 修改商品套餐
export function updatePackage(data) {
  return request({
    url: '/payment/package',
    method: 'put',
    data: data
  })
}

// 删除商品套餐
export function delPackage(packageId) {
  return request({
    url: '/payment/package/' + packageId,
    method: 'delete'
  })
}
