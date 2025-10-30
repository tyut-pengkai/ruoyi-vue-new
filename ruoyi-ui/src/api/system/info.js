import request from '@/utils/request'

// 查询学生信息列表
export function listInfo(query) {
  return request({
    url: '/system/info/list',
    method: 'get',
    params: query
  })
}

// 查询学生信息详细
export function getInfo(studentId) {
  return request({
    url: '/system/info/' + studentId,
    method: 'get'
  })
}

// 新增学生信息
export function addInfo(data) {
  return request({
    url: '/system/info',
    method: 'post',
    data: data
  })
}

// 修改学生信息
export function updateInfo(data) {
  return request({
    url: '/system/info',
    method: 'put',
    data: data
  })
}

// 删除学生信息
export function delInfo(studentId) {
  return request({
    url: '/system/info/' + studentId,
    method: 'delete'
  })
}