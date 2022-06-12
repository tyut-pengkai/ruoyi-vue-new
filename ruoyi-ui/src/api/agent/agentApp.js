import request from '@/utils/request'

// 查询软件列表
export function listAppAll(query) {
  return request({
    url: '/agent/agentApp/listAll',
    method: 'get',
    params: query
  })
}
