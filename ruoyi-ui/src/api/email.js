import request from '@/utils/request'

// 发送邮箱验证码
export function sendEmailCode(data) {
  return request({
    url: '/sendEmailCode',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

// 测试邮件发送
export function testEmail(data) {
  return request({
    url: '/testEmail',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
} 