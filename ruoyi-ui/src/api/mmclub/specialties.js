import request from '@/utils/request'

// 查询医生专业管理列表
export function listSpecialties(query) {
  return request({
    url: '/mmclub/specialties/list',
    method: 'get',
    params: query
  })
}

// 查询医生专业管理详细
export function getSpecialties(id) {
  return request({
    url: '/mmclub/specialties/' + id,
    method: 'get'
  })
}

// 新增医生专业管理
export function addSpecialties(data) {
  return request({
    url: '/mmclub/specialties',
    method: 'post',
    data: data
  })
}

// 修改医生专业管理
export function updateSpecialties(data) {
  return request({
    url: '/mmclub/specialties',
    method: 'put',
    data: data
  })
}

// 删除医生专业管理
export function delSpecialties(id) {
  return request({
    url: '/mmclub/specialties/' + id,
    method: 'delete'
  })
}
