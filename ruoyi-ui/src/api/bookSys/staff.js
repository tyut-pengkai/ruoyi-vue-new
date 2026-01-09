import request from '@/utils/request'

// 查询物业人员列表
export function listStaff(query) {
  return request({
    url: '/bookSys/staff/list',
    method: 'get',
    params: query
  })
}

// 查询物业人员详细
export function getStaff(staffId) {
  return request({
    url: '/bookSys/staff/' + staffId,
    method: 'get'
  })
}

// 新增物业人员
export function addStaff(data) {
  return request({
    url: '/bookSys/staff',
    method: 'post',
    data: data
  })
}

// 修改物业人员
export function updateStaff(data) {
  return request({
    url: '/bookSys/staff',
    method: 'put',
    data: data
  })
}

// 删除物业人员
export function delStaff(staffId) {
  return request({
    url: '/bookSys/staff/' + staffId,
    method: 'delete'
  })
}
