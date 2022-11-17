import request from '@/utils/request'

// 查询首页导航列表
export function listNavigation(query) {
  return request({
    url: '/system/navigation/list',
    method: 'get',
    params: query
  })
}

// 查询首页导航详细
export function getNavigation(navId) {
  return request({
    url: '/system/navigation/' + navId,
    method: 'get'
  })
}

// 新增首页导航
export function addNavigation(data) {
  return request({
    url: '/system/navigation',
    method: 'post',
    data: data
  })
}

// 修改首页导航
export function updateNavigation(data) {
  return request({
    url: '/system/navigation',
    method: 'put',
    data: data
  })
}

// 删除首页导航
export function delNavigation(navId) {
  return request({
    url: '/system/navigation/' + navId,
    method: 'delete'
  })
}
