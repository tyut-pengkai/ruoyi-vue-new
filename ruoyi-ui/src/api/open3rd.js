import request from '@/utils/request'

export function getOpen3rdSignature() {
  return request({
    url: '/open3rd/signature',
    method: 'get'
  })
}
