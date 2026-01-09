import request from '@/utils/request'

// 查询异常记录列表
export function listAnomalies(query) {
  return request({
    url: '/bookSys/anomalies/list',
    method: 'get',
    params: query
  })
}

// 查询异常记录详细
export function getAnomalies(anomalyId) {
  return request({
    url: '/bookSys/anomalies/' + anomalyId,
    method: 'get'
  })
}

// 新增异常记录
export function addAnomalies(data) {
  return request({
    url: '/bookSys/anomalies',
    method: 'post',
    data: data
  })
}

// 修改异常记录
export function updateAnomalies(data) {
  return request({
    url: '/bookSys/anomalies',
    method: 'put',
    data: data
  })
}

// 删除异常记录
export function delAnomalies(anomalyId) {
  return request({
    url: '/bookSys/anomalies/' + anomalyId,
    method: 'delete'
  })
}
