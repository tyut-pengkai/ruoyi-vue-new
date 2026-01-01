import request from '@/utils/request'

// 查询数据库管理列表
export function listDatabase(query) {
  return request({
    url: '/system/database/list',
    method: 'get',
    params: query
  })
}

// 查询数据库管理详细信息
export function getDatabase(databaseId) {
  return request({
    url: '/system/database/' + databaseId,
    method: 'get'
  })
}

// 新增数据库管理
export function addDatabase(data) {
  return request({
    url: '/system/database',
    method: 'post',
    data: data
  })
}

// 修改数据库管理
export function updateDatabase(data) {
  return request({
    url: '/system/database',
    method: 'put',
    data: data
  })
}

// 删除数据库管理
export function delDatabase(databaseId) {
  return request({
    url: '/system/database/' + databaseId,
    method: 'delete'
  })
}

// 测试数据库连接
export function testConnection(data) {
  return request({
    url: '/system/database/testConnection',
    method: 'post',
    data: data
  })
}

// 查询数据库表结构
export function getDatabaseTables(databaseId) {
  return request({
    url: '/system/database/tables/' + databaseId,
    method: 'get'
  })
}

// 数据库备份
export function backupDatabase(databaseId) {
  return request({
    url: '/system/database/backup/' + databaseId,
    method: 'post'
  })
}

// 数据库恢复
export function restoreDatabase(databaseId, backupFile) {
  return request({
    url: '/system/database/restore/' + databaseId,
    method: 'post',
    params: { backupFile: backupFile }
  })
}

// 导入数据
export function importDatabase(data) {
  return request({
    url: '/system/database/importData',
    method: 'post',
    data: data
  })
}

// 导出数据
export function exportDatabase(query) {
  return request({
    url: '/system/database/export',
    method: 'get',
    params: query
  })
}