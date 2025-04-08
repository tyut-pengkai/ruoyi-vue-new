import request from '@/utils/request'

// 查询网站配置
export function getWebsiteConfig() {
  return request({
    url: '/system/website/config',
    method: 'get'
  })
}

// 修改网站配置
export function updateWebsiteConfig(data) {
  return request({
    url: '/system/website/config',
    method: 'put',
    data: data
  })
}

// 检测网站域名正确性
export function checkWebsiteDomain(data) {
  return request({
    url: '/system/website/config/checkDomain',
    method: 'post',
    data: data
  })
}
