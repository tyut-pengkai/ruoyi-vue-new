import request from '@/utils/request'

// 查询医生管理列表
export function listDoctors(query) {
  return request({
    url: '/mmclub/doctors/list',
    method: 'get',
    params: query
  })
}

// 查询医生管理详细
export function getDoctors(id) {
  return request({
    url: '/mmclub/doctors/' + id,
    method: 'get'
  })
}

// 新增医生管理
export function addDoctors(data) {
  return request({
    url: '/mmclub/doctors',
    method: 'post',
    data: data
  })
}

// 修改医生管理
export function updateDoctors(data) {
  return request({
    url: '/mmclub/doctors',
    method: 'put',
    data: data
  })
}

// 删除医生管理
export function delDoctors(id) {
  return request({
    url: '/mmclub/doctors/' + id,
    method: 'delete'
  })
}
