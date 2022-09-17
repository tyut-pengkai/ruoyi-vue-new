import request from '@/utils/request'

// 查询验证授权用户列表
export function listLicenseRecord(query) {
  return request({
    url: '/license/licenseRecord/list',
    method: 'get',
    params: query
  })
}

// 查询验证授权用户详细
export function getLicenseRecord(id) {
  return request({
    url: '/license/licenseRecord/' + id,
    method: 'get'
  })
}

// 新增验证授权用户
export function addLicenseRecord(data) {
  return request({
    url: '/license/licenseRecord',
    method: 'post',
    data: data
  })
}

// 修改验证授权用户
export function updateLicenseRecord(data) {
  return request({
    url: '/license/licenseRecord',
    method: 'put',
    data: data
  })
}

// 删除验证授权用户
export function delLicenseRecord(id) {
  return request({
    url: '/license/licenseRecord/' + id,
    method: 'delete'
  })
}

// 兑换授权文件
export function genLicenseFileByWebUrl(query) {
  return request({
    url: '/license/licenseRecord/genLicenseFileByWebUrl',
    method: 'get',
    params: query
  })
}

// 兑换授权文件
export function genLicenseFileByDeviceCode(query) {
  return request({
    url: '/license/licenseRecord/genLicenseFileByDeviceCode',
    method: 'get',
    params: query
  })
}
