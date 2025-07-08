import request from '@/utils/request'

// 查询医院管理列表
export function listHospitals(query) {
  return request({
    url: '/mmclub/hospitals/list',
    method: 'get',
    params: query
  })
}

// 查询医院管理详细
export function getHospitals(id) {
  return request({
    url: '/mmclub/hospitals/' + id,
    method: 'get'
  })
}

// 新增医院管理
export function addHospitals(data) {
  return request({
    url: '/mmclub/hospitals',
    method: 'post',
    data: data
  })
}

// 修改医院管理
export function updateHospitals(data) {
  return request({
    url: '/mmclub/hospitals',
    method: 'put',
    data: data
  })
}

// 删除医院管理
export function delHospitals(id) {
  return request({
    url: '/mmclub/hospitals/' + id,
    method: 'delete'
  })
}
